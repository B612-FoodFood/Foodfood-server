package B612.foodfood.controller.api;

import B612.foodfood.domain.*;
import B612.foodfood.dto.*;
import B612.foodfood.dto.joinApiController.MemberJoinDietInfoResponse;
import B612.foodfood.dto.joinApiController.MemberJoinRequest;
import B612.foodfood.dto.memberApiController.*;
import B612.foodfood.exception.AppException;
import B612.foodfood.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static B612.foodfood.domain.MealType.*;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    private final FoodService foodService;
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
        }

        MemberLogInResponse value = new MemberLogInResponse(accessToken, refreshToken);
        return new Result<>(HttpStatus.OK, null, value);
    }

    @GetMapping("/member")
    public Result<MemberInfoResponse> memberPage(Authentication authentication) {
        try {
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
            value.setRecommendedCalories(recommendedCalories);
            value.setAchieveWeight(achieveBodyGoal.getAchieveWeight());
            value.setAchieveMuscle(achieveBodyGoal.getAchieveMuscle());
            value.setAchieveBodyFat(achieveBodyGoal.getAchieveBodyFat());
            value.setMode(goal);
            value.setCurrentWeight(currentWeight);
            value.setCurrentMuscle(currentMuscle);
            value.setCurrentBodyFat(currentBodyFat);

            // 오늘 먹은 식사
            Optional<Meal> mealByDate = member.findMealByDate(LocalDate.now());
            if (mealByDate.isPresent()) {
                Meal meal = mealByDate.get();
                ConsumedNutrients nutritionPerDay = meal.getNutritionPerDay();
                consumedCalories = nutritionPerDay.getCalories();

                // 남은 칼로리
                leftCalories = Math.max(leftCalories - consumedCalories, 0);

                // 아침식사 추가
                List<MealFood> breakFast = meal.getBreakFast();
                for (MealFood mealFood : breakFast) {
                    if(mealFood.getMealType()!= BREAKFAST) continue;

                    Food food = mealFood.getFood();
                    Nutrition nutrition = food.getNutrition();
                    value.addBreakFast(food.getName(), nutrition.getServingWeight(), nutrition.getCalories(), nutrition.getCarbonHydrate(), nutrition.getProtein());
                }
                // 점심식사 추가
                List<MealFood> lunch = meal.getLunch();
                for (MealFood mealFood : lunch) {
                    if(mealFood.getMealType()!= LUNCH) continue;

                    Food food = mealFood.getFood();
                    Nutrition nutrition = food.getNutrition();
                    value.addLunch(food.getName(), nutrition.getServingWeight(), nutrition.getCalories(), nutrition.getCarbonHydrate(), nutrition.getProtein());
                }
                // 저녁식사 추가
                List<MealFood> dinner = meal.getDinner();
                for (MealFood mealFood : dinner) {
                    if(mealFood.getMealType()!= DINNER) continue;

                    Food food = mealFood.getFood();
                    Nutrition nutrition = food.getNutrition();
                    value.addDinner(food.getName(), nutrition.getServingWeight(), nutrition.getCalories(), nutrition.getCarbonHydrate(), nutrition.getProtein());
                }
                // 간식추가
                List<MealFood> snack = meal.getSnack();
                for (MealFood mealFood : snack) {
                    if(mealFood.getMealType()!= SNACK) continue;

                    Food food = mealFood.getFood();
                    Nutrition nutrition = food.getNutrition();
                    value.addDinner(food.getName(), nutrition.getServingWeight(), nutrition.getCalories(), nutrition.getCarbonHydrate(), nutrition.getProtein());
                }
            }

            for (BodyComposition bodyComposition : bodyCompositions) {
                LocalDate date = bodyComposition.getDate();
                double weight = bodyComposition.getWeight();
                Double muscle = bodyComposition.getMuscle();
                Double bodyFat = bodyComposition.getBodyFat();

                value.addBodyComposition(date, weight, muscle, bodyFat);  // 사용자 신체 정보 변화
            }
            value.setConsumedCalories(consumedCalories);
            value.setLeftCalories(leftCalories);
            // 로그인한 멤버의 각종 정보 반환 로직 구현
            return new Result(HttpStatus.OK, null, value);
        } catch (AppException e) {
            return new Result<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null);
        }
    }

}
