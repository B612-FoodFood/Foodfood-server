package B612.foodfood.controller.api;

import B612.foodfood.domain.*;
import B612.foodfood.dto.*;
import B612.foodfood.dto.memberApiController.*;
import B612.foodfood.exception.AppException;
import B612.foodfood.exception.ErrorCode;
import B612.foodfood.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1")
public class MemberApiController {

    private final MemberService memberService;
    private final FoodService foodService;
    private final MealService mealService;
    private final DiseaseService diseaseService;
    private final DrugService drugService;
    private final IngredientService ingredientService;
    private final BCryptPasswordEncoder encoder;
    private final AverageBodyProfileService abpService;
    private final ObjectMapper objectMapper;

    @PostMapping("/login")
    public Result<MemberLogInResponse> logIn(@RequestBody MemberLogInRequest request) {

        String accessToken = null;
        String refreshToken = null;
        try {
            TokenSet tokenSet = memberService.login(request.getUsername(), request.getPassword());
            accessToken = tokenSet.getAccessToken();
            refreshToken = tokenSet.getRefreshToken();
        } catch (AppException e) {
            MemberLogInResponse value = new MemberLogInResponse(null, null);
            return new Result<>(e.getErrorCode().getHttpStatus(), e.getMessage(), value);
        } finally {
            log.info("POST: /api/v1/login");
        }

        MemberLogInResponse value = new MemberLogInResponse(accessToken, refreshToken);
        return new Result<>(HttpStatus.OK, null, value);
    }

    /**
     * 로그인 후 메인페이지에 필요한 각종 정보들을 반환
     *
     * @param authentication
     * @return
     */
    @GetMapping("/member")
    public Result<MemberInfoResponse> memberPage(Authentication authentication) {
        try {
            log.info("/api/v1/member");

            String userName = authentication.getName();
            MemberInfoResponse value = new MemberInfoResponse();

            Member member = memberService.findMemberByLogInUsername(userName);

            String name = member.getName(); // 회원 이름
            BodyGoal goal = member.getGoal();
            AchieveBodyGoal achieveBodyGoal = member.getAchieveBodyGoal();
            double recommendedCalories = member.getRecommendedCalories(); // 회원 권장 칼로리
            double consumedCalories = 0; // 하루동안 섭취 칼로리
            double leftCalories = recommendedCalories; // 하룻동안 남은 칼로리

            List<BodyComposition> bodyCompositions = member.getBodyCompositions();
            BodyComposition currentBodyComposition = bodyCompositions.get(bodyCompositions.size() - 1);  // 현재(최근) 신체정보
            double currentWeight = currentBodyComposition.getWeight();
            Double currentMuscle = currentBodyComposition.getMuscle();
            Double currentBodyFat = currentBodyComposition.getBodyFat();

            // 정보 담기
            value.setName(name);
            value.setRecommendedCalories((int) recommendedCalories);
            value.setAchieveWeight(achieveBodyGoal.getAchieveWeight());
            value.setAchieveMuscle(achieveBodyGoal.getAchieveMuscle());
            value.setAchieveBodyFat(achieveBodyGoal.getAchieveBodyFat());
            value.setMode(goal);
            value.setCurrentWeight(currentWeight);
            value.setCurrentMuscle(currentMuscle);
            value.setCurrentBodyFat(currentBodyFat);

            // 오늘 먹은 식사
            Optional<Meal> mealByDate = member.getMealByDate(LocalDate.now());
            if (mealByDate.isPresent()) {
                Meal meal = mealByDate.get();
                ConsumedNutrients nutritionPerDay = meal.getNutritionPerDay();
                consumedCalories = nutritionPerDay.getCalories();

                // 남은 칼로리
                leftCalories = Math.max(leftCalories - consumedCalories, 0);

                // 아침식사 추가
                List<MealFood> breakFast = meal.getBreakFast();
                for (MealFood mealFood : breakFast) {
                    Food food = mealFood.getFood();
                    Nutrition nutrition = food.getNutrition();
                    value.addBreakFast(food.getName(), nutrition.getServingWeight(), nutrition.getCalories(), nutrition.getCarbonHydrate(), nutrition.getProtein(), nutrition.getFat());
                }
                // 점심식사 추가
                List<MealFood> lunch = meal.getLunch();
                for (MealFood mealFood : lunch) {
                    Food food = mealFood.getFood();
                    Nutrition nutrition = food.getNutrition();
                    value.addLunch(food.getName(), nutrition.getServingWeight(), nutrition.getCalories(), nutrition.getCarbonHydrate(), nutrition.getProtein(), nutrition.getFat());
                }
                // 저녁식사 추가
                List<MealFood> dinner = meal.getDinner();
                for (MealFood mealFood : dinner) {
                    Food food = mealFood.getFood();
                    Nutrition nutrition = food.getNutrition();
                    value.addDinner(food.getName(), nutrition.getServingWeight(), nutrition.getCalories(), nutrition.getCarbonHydrate(), nutrition.getProtein(), nutrition.getFat());
                }
                // 간식추가
                List<MealFood> snack = meal.getSnack();
                for (MealFood mealFood : snack) {
                    Food food = mealFood.getFood();
                    Nutrition nutrition = food.getNutrition();
                    value.addSnack(food.getName(), nutrition.getServingWeight(), nutrition.getCalories(), nutrition.getCarbonHydrate(), nutrition.getProtein(), nutrition.getFat());
                }
            }

            for (BodyComposition bodyComposition : bodyCompositions) {
                LocalDate date = bodyComposition.getDate();
                double weight = bodyComposition.getWeight();
                Double muscle = bodyComposition.getMuscle();
                Double bodyFat = bodyComposition.getBodyFat();

                value.addBodyComposition(date, weight, muscle, bodyFat);  // 사용자 신체 정보 변화
            }
            value.setConsumedCalories((int) consumedCalories);
            value.setLeftCalories((int) leftCalories);
            // 로그인한 멤버의 각종 정보 반환 로직 구현
            return new Result(HttpStatus.OK, null, value);
        } catch (AppException e) {
            return new Result<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null);
        } finally {
            log.info("GET: /api/v1/member");
        }
    }

    /**
     * @param authentication
     * @return 내 건강상태 수정하기 버튼 클릭시 표시될 현재의 목표 골격근량, 현재 골격근량을 반환. 건강상태 수정에서 초기화 버튼 눌러도 사용됨.
     */
    @GetMapping("/member/edit/muscle")
    public Result<MemberEditMuscleResponse> currentMuscle(Authentication authentication) {
        try {
            String userName = authentication.getName();
            Member member = memberService.findMemberByLogInUsername(userName);
            List<BodyComposition> bodyCompositions = member.getBodyCompositions();

            Double muscle = bodyCompositions.get(bodyCompositions.size() - 1).getMuscle(); // 가장 최근의 골격근량 (현재 상태도 이 정보로 유지됨)
            double achieveMuscle = member.getAchieveBodyGoal().getAchieveMuscle();

            MemberEditMuscleResponse value = new MemberEditMuscleResponse(muscle, achieveMuscle);
            return new Result(HttpStatus.OK, null, value);

        } catch (AppException e) {
            return new Result(e.getErrorCode().getHttpStatus(), e.getMessage(), null);
        } finally {
            log.info("GET: /api/v1/edit/muscle");
        }
    }

    /**
     * @param authentication
     * @param request
     * @return 건강 상태 수정에서 수정완료 버튼 클릭 시 골격근량 업데이트됨.
     */
    @PostMapping("/member/edit/muscle")
    public Result editMuscle(Authentication authentication, @RequestBody MemberEditMuscleRequest request) {
        try {
            String userName = authentication.getName();
            Member member = memberService.findMemberByLogInUsername(userName);
            double muscle = request.getMuscle(); // 업데이트할 현재 골격근량
            double achieveMuscle = request.getAchieveMuscle();  // 업데이트할 현재 목표 골격근량

            List<BodyComposition> bodyCompositions = member.getBodyCompositions();
            BodyComposition recentBodyComposition = bodyCompositions.get(bodyCompositions.size() - 1);

            // 기존 신체정보(체중, 체지방률)은 유지
            double weight = recentBodyComposition.getWeight();
            Double bodyFat = recentBodyComposition.getBodyFat();
            BodyComposition updatedBodyComposition = new BodyComposition(weight, muscle, bodyFat);

            // 기존 목표 신체정보(목표체중, 목표체지방률)은 유지
            AchieveBodyGoal achieveBodyGoal = member.getAchieveBodyGoal();
            double achieveWeight = achieveBodyGoal.getAchieveWeight();
            double achieveBodyFat = achieveBodyGoal.getAchieveBodyFat();
            AchieveBodyGoal updatedAchieveBodyGoal = new AchieveBodyGoal(achieveWeight, achieveMuscle, achieveBodyFat);

            // 정보 업데이트
            member.addBodyComposition(updatedBodyComposition);
            memberService.updateAddBodyComposition(member.getId(), updatedBodyComposition);
            memberService.updateAchieveBodyGoal(member.getId(), updatedAchieveBodyGoal);
            return new Result(HttpStatus.OK, null, null);
        } catch (AppException e) {
            return new Result(e.getErrorCode().getHttpStatus(), e.getMessage(), null);
        } finally {
            log.info("POST: /api/v1/member/edit/muscle");
        }
    }

    /**
     * @param authentication
     * @return 내 건강상태 수정하기 버튼 클릭시 표시될 현재의 목표 체지방률, 현재 체지방률을 반환. 건강상태 수정에서 초기화 버튼 눌러도 사용됨.
     */
    @GetMapping("/member/edit/bodyFat")
    public Result<MemberEditBodyFatResponse> currentBodyFat(Authentication authentication) {
        try {

            String userName = authentication.getName();
            Member member = memberService.findMemberByLogInUsername(userName);
            List<BodyComposition> bodyCompositions = member.getBodyCompositions();

            Double bodyFat = bodyCompositions.get(bodyCompositions.size() - 1).getBodyFat(); // 가장 최근의 체지방량 (현재 상태도 이 정보로 유지됨)
            double achieveBodyFat = member.getAchieveBodyGoal().getAchieveBodyFat();

            MemberEditBodyFatResponse value = new MemberEditBodyFatResponse(bodyFat, achieveBodyFat);
            return new Result(HttpStatus.OK, null, value);
        } catch (AppException e) {
            return new Result(e.getErrorCode().getHttpStatus(), e.getMessage(), null);
        } finally {
            log.info("GET: /api/v1/member/edit/bodyFat");
        }
    }

    /**
     * @param authentication
     * @param request
     * @return 건강 상태 수정에서 수정완료 버튼 클릭 시 체지방률 업데이트됨.
     */
    @PostMapping("/member/edit/bodyFat")
    public Result editBodyFat(Authentication authentication, @RequestBody MemberEditBodyFatRequest request) {
        try {
            String userName = authentication.getName();
            Member member = memberService.findMemberByLogInUsername(userName);
            double bodyFat = request.getBodyFat(); // 업데이트할 현재 골격근량
            double achieveBodyFat = request.getAchieveBodyFat();  // 업데이트할 현재 목표 골격근량

            List<BodyComposition> bodyCompositions = member.getBodyCompositions();
            BodyComposition recentBodyComposition = bodyCompositions.get(bodyCompositions.size() - 1);

            // 기존 신체정보(체중, 골격근량)은 유지
            double weight = recentBodyComposition.getWeight();
            Double muscle = recentBodyComposition.getMuscle();
            BodyComposition updatedBodyComposition = new BodyComposition(weight, muscle, bodyFat);

            // 기존 목표 신체정보(목표체중, 목표골격근량)은 유지
            AchieveBodyGoal achieveBodyGoal = member.getAchieveBodyGoal();
            double achieveWeight = achieveBodyGoal.getAchieveWeight();
            double achieveMuscle = achieveBodyGoal.getAchieveMuscle();
            AchieveBodyGoal updatedAchieveBodyGoal = new AchieveBodyGoal(achieveWeight, achieveMuscle, achieveBodyFat);

            // 정보 업데이트
            memberService.updateAddBodyComposition(member.getId(), updatedBodyComposition);
            memberService.updateAchieveBodyGoal(member.getId(), updatedAchieveBodyGoal);
            return new Result(HttpStatus.OK, null, null);
        } catch (AppException e) {
            return new Result(e.getErrorCode().getHttpStatus(), e.getMessage(), null);
        } finally {
            log.info("POST: /api/v1/member/edit/bodyFat");
        }
    }

    /**
     * @param authentication
     * @return 내 건강상태 수정하기 버튼 클릭시 표시될 현재의 목표 체중, 현재 체중을 반환. 건강상태 수정에서 초기화 버튼 눌러도 사용됨.
     */
    @GetMapping("/member/edit/weight")
    public Result<MemberEditWeightResponse> currentWeight(Authentication authentication) {
        try {
            String userName = authentication.getName();
            Member member = memberService.findMemberByLogInUsername(userName);
            List<BodyComposition> bodyCompositions = member.getBodyCompositions();

            Double weight = bodyCompositions.get(bodyCompositions.size() - 1).getWeight(); // 가장 최근의 체중 (현재 상태도 이 정보로 유지됨)
            double achieveWeight = member.getAchieveBodyGoal().getAchieveWeight();

            MemberEditWeightResponse value = new MemberEditWeightResponse(weight, achieveWeight);
            return new Result(HttpStatus.OK, null, value);

        } catch (AppException e) {
            return new Result(e.getErrorCode().getHttpStatus(), e.getMessage(), null);
        } finally {
            log.info("GET:/api/v1/member/edit/weight");
        }
    }

    /**
     * @param authentication
     * @param request
     * @return 건강 상태 수정에서 수정완료 버튼 클릭 시 체중 업데이트됨.
     */
    @PostMapping("/member/edit/weight")
    public Result editBodyFat(Authentication authentication, @RequestBody MemberEditWeightRequest request) {
        try {
            String userName = authentication.getName();
            Member member = memberService.findMemberByLogInUsername(userName);
            double weight = request.getWeight(); // 업데이트할 현재 체중
            double achieveWeight = request.getAchieveWeight();  // 업데이트할 현재 목표 체중

            List<BodyComposition> bodyCompositions = member.getBodyCompositions();
            BodyComposition recentBodyComposition = bodyCompositions.get(bodyCompositions.size() - 1);

            // 기존 신체정보(골격근량, 체지방률)은 유지
            Double muscle = recentBodyComposition.getMuscle();
            Double bodyFat = recentBodyComposition.getBodyFat();
            BodyComposition updatedBodyComposition = new BodyComposition(weight, muscle, bodyFat);

            // 기존 목표 신체정보(목표골격근량, 목표체지방률)은 유지
            AchieveBodyGoal achieveBodyGoal = member.getAchieveBodyGoal();
            double achieveMuscle = achieveBodyGoal.getAchieveMuscle();
            double achieveBodyFat = achieveBodyGoal.getAchieveBodyFat();
            AchieveBodyGoal updatedAchieveBodyGoal = new AchieveBodyGoal(achieveWeight, achieveMuscle, achieveBodyFat);

            // 정보 업데이트
            memberService.updateAddBodyComposition(member.getId(), updatedBodyComposition);
            memberService.updateAchieveBodyGoal(member.getId(), updatedAchieveBodyGoal);
            return new Result(HttpStatus.OK, null, null);
        } catch (AppException e) {
            return new Result(e.getErrorCode().getHttpStatus(), e.getMessage(), null);
        } finally {
            log.info("POST: /api/v1/member/edit/weight");
        }
    }

    /**
     * 특정일자에 먹은 식사 내역을 반환해줌
     *
     * @param authentication
     * @param request
     * @return
     */
    @Transactional(readOnly = false)
    @PostMapping("/member/meal")
    public Result<MemberMealResponse> searchMealByDate(Authentication authentication, @RequestBody MemberMealRequest request) {
        if (request.getDate().isAfter(LocalDate.now())) {
            return new Result<>(HttpStatus.BAD_REQUEST, "잘못된 날짜 정보입니다.", null);
        }

        String userName = authentication.getName();
        Member member = memberService.findMemberByLogInUsername(userName);

        Optional<Meal> mealByDate = member.getMealByDate(request.getDate());
        Meal meal;
        if (mealByDate.isPresent()) {  // 해당일자에 이미 Meal이 존재한다면 해당 Meal을 가져옴
            meal = mealByDate.get();
        } else {  // 해당 날짜에 생성된 Meal이 없다면 해당 일자에 Meal을 생성.
            meal = new Meal(request.getDate());
            member.addMeal(meal);
            mealService.save(meal);
        }

        MemberMealResponse value = new MemberMealResponse();
        for (MealFood breakFast : meal.getBreakFast()) {
            FoodResult result = getFoodResult(breakFast);

            // add breakFast food
            value.addBreakFast(result.name(), result.calories(), result.carbonHydrate(), result.protein(), result.fat());
        }
        for (MealFood lunch : meal.getLunch()) {
            FoodResult result = getFoodResult(lunch);

            // add lunch food
            value.addLunch(result.name(), result.calories(), result.carbonHydrate(), result.protein(), result.fat());
        }
        for (MealFood dinner : meal.getDinner()) {
            FoodResult result = getFoodResult(dinner);

            // add dinner food
            value.addDinner(result.name(), result.calories(), result.carbonHydrate(), result.protein(), result.fat());
        }
        for (MealFood snack : meal.getSnack()) {
            FoodResult result = getFoodResult(snack);

            // add snack food
            value.addSnack(result.name(), result.calories(), result.carbonHydrate(), result.protein(), result.fat());
        }
        log.info("POST: /api/v1/member/meal");

        return new Result<>(HttpStatus.OK, null, value);
    }

    private static FoodResult getFoodResult(MealFood breakFast) {
        Food food = breakFast.getFood();
        Nutrition nutrition = food.getNutrition();

        // food name
        String name = food.getName();

        // food Nutrition
        double calories = nutrition.getCalories();
        double carbonHydrate = nutrition.getCarbonHydrate();
        double protein = nutrition.getProtein();
        double fat = nutrition.getFat();
        FoodResult result = new FoodResult(name, calories, carbonHydrate, protein, fat);
        return result;
    }

    private record FoodResult(String name, double calories, double carbonHydrate, double protein, double fat) {
    }

    /**
     * 키워드로 검색한 음식 데이터를 반환
     *
     * @param request
     * @return
     */
    @PostMapping("/member/meal/search")
    public Result<MemberMealSearchResponse> searchFood(@RequestBody MemberMealSearchRequest request) {
        try {
            List<Food> foods
                    = foodService.findFoodByKeyword(request.getKeyword());
            MemberMealSearchResponse value = new MemberMealSearchResponse();

            for (Food food : foods) {
                String name = food.getName();
                double calories = food.getNutrition().getCalories();
                double carbonHydrate = food.getNutrition().getCarbonHydrate();
                double protein = food.getNutrition().getProtein();
                double fat = food.getNutrition().getFat();

                value.addFoods(name, calories, carbonHydrate, protein, fat);
            }

            return new Result(HttpStatus.OK, null, value);
        } catch (AppException e) {
            return new Result<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null);
        } finally {
            log.info("POST: /api/v1/member/meal/search");
        }
    }

    /**
     * 유저가 특정일자에 먹은 식사 데이터를 아침, 점심, 저녁, 간식으로 구별하여 조회
     *
     * @param authentication
     * @param type
     * @param request
     * @return
     */
    @Transactional
    @PostMapping("/member/meal/add/{type}")  // type = {breakfast,lunch,dinner,snack}
    public Result addFood(Authentication authentication, @PathVariable(name = "type") String type, @RequestBody MemberMealSelectRequest request) {
        try {
            String username = authentication.getName();
            Member member = memberService.findMemberByLogInUsername(username);

            Meal meal;
            Optional<Meal> mealByDate = member.getMealByDate(request.getDate());

            if (mealByDate.isEmpty()) {  // 멤버가 해당날짜에 식사내역이 없는 경우 식사 추가
                meal = new Meal(request.getDate());
                member.addMeal(meal);
            } else {  // 식사내역이 있는 경우 식사내역 가져옴.
                meal = mealByDate.get();
            }

            Food food = foodService.findFoodByName(request.getName());
            double foodWeight = request.getFoodWeight();

            // 요청한 타입(아침,점심,저녁,간식)에 따라 식사 추가
            if (type.equals("breakfast")) {
                log.info("add breakfast: {},{}", food.getName(), foodWeight);
                meal.addBreakFast(food, foodWeight);
            } else if (type.equals("lunch")) {
                meal.addLunch(food, foodWeight);
            } else if (type.equals("dinner")) {
                meal.addDinner(food, foodWeight);
            } else if (type.equals("snack")) {
                meal.addSnack(food, foodWeight);
            } else {
                throw new AppException(ErrorCode.INVALID_URI_ACCESS, "잘못된 uri 접근입니다");
            }

            mealService.save(meal);

            return new Result(HttpStatus.OK, null, null);
        } catch (AppException e) {
            return new Result(e.getErrorCode().getHttpStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return new Result(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        } finally {
            log.info("POST: /api/v1/member/meal/add/{}", type);
        }
    }

    /**
     * <p>음식 직접 입력. 사용자가 직접 Food 엔티티를 생성.</p>
     * <p>해당 Food는 DB에 저장되고, 다른 유저도 조회할 수 있음.</p>
     *
     * @param request
     * @return
     */
    @PostMapping("/member/food/create")
    public Result createFood(@RequestBody MemberMealAddCreateRequest request) {
        try {
            log.info("/api/v1/member/food/create");

            String foodName = request.getName();
            double servingWeight = request.getServingWeight();
            double calories = request.getCalories();
            double carbonHydrate = request.getCarbonHydrate();
            double protein = request.getProtein();
            double fat = request.getFat();

            Nutrition nutrition = new Nutrition(servingWeight, calories, carbonHydrate, protein, fat);
            Food userCreateFood = new Food(foodName, nutrition, true);

            foodService.save(userCreateFood);  // 유저가 보낸 정보를 바탕으로 새로운 Food을 만들어 DB 저장.

            return new Result(HttpStatus.OK, null, null);
        } catch (AppException e) {
            return new Result(e.getErrorCode().getHttpStatus(), e.getMessage(), null);
        } finally {
            log.info("POST: /api/v1/member/food/create");
        }
    }


    @GetMapping("/member/recipe/muscle")
    public Result<MemberRecipeMuscleResponse> recommendMuscleRecoveryFood(Authentication authentication) {
        try {
            String username = authentication.getName();
            Member member = memberService.findMemberByLogInUsername(username);

            // 권장 탄단지 + 칼로리
            double carbonHydrate = member.getRecommendedCarbonHydrate();
            double protein = member.getRecommendedProtein();
            double fat = member.getRecommendedFat();
            double calories = member.getRecommendedCalories();

            // 아침 (30%)
            double morningRatio = 0.3;
            double caloriesMorning = calories * morningRatio;
            double carbonHydrateMorning = carbonHydrate * morningRatio;
            double proteinMorning = protein * morningRatio;
            double fatMorning = fat * morningRatio;

            // 점심 (40%)
            double lunchRatio = 0.4;
            double caloriesLunch = calories * lunchRatio;
            double carbonHydrateLunch = carbonHydrate * lunchRatio;
            double proteinLunch = protein * lunchRatio;
            double fatLunch = fat * lunchRatio;

            // 저녁 (30%)
            double dinnerRatio = 0.3;
            double caloriesDinner = calories * dinnerRatio;
            double carbonHydrateDinner = carbonHydrate * dinnerRatio;
            double proteinDinner = protein * dinnerRatio;
            double fatDinner = fat * dinnerRatio;

            List<Food> breakFastFoods = foodService.findFoodBetweenTwoNutrition(caloriesMorning * 0.5, caloriesMorning * 1.1, 0, carbonHydrateMorning * 1.1, proteinMorning * 0.9, proteinMorning * 1.1, 0, fatLunch * 1.1, 6);
            List<Food> lunchFoods = foodService.findFoodBetweenTwoNutrition(caloriesLunch * 0.5, caloriesLunch * 1.1, 0, carbonHydrateLunch * 1.1, proteinLunch * 0.9, proteinLunch * 1.1, 0, fatLunch * 1.1, 6);
            List<Food> dinnerFoods = foodService.findFoodBetweenTwoNutrition(caloriesDinner * 0.5, caloriesDinner * 1.1, 0, carbonHydrateDinner * 1.1, proteinDinner * 0.9, proteinDinner * 1.1, 0, fatDinner * 1.1, 6);

            MemberRecipeMuscleResponse value = new MemberRecipeMuscleResponse();

            value.addBreakFasts(breakFastFoods);
            value.addLunches(lunchFoods);
            value.addDinners(dinnerFoods);

            System.out.println("size: " + breakFastFoods.size());
            for (Food dinnerFood : dinnerFoods) {
                System.out.println("dinnerFood = " + dinnerFood);
            }

            return new Result<>(HttpStatus.OK,null,value);
        } catch (AppException e) {
            return new Result<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null);
        } finally {
            log.info("GET: /recipe/muscle");
        }
    }
}