package B612.foodfood.controller.api;

import B612.foodfood.domain.*;
import B612.foodfood.dto.Result;
import B612.foodfood.dto.joinApiController.*;
import B612.foodfood.dto.joinApiController.MemberJoinRequest;
import B612.foodfood.dto.joinApiController.MemberJoinRequest2;
import B612.foodfood.exception.AppException;
import B612.foodfood.exception.ErrorCode;
import B612.foodfood.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static B612.foodfood.domain.AccountType.*;
import static B612.foodfood.domain.BodyGoal.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/join")
@Slf4j
public class JoinApiController {

    private final MemberService memberService;
    private final DiseaseService diseaseService;
    private final DrugService drugService;
    private final IngredientService ingredientService;
    private final AverageBodyProfileService bodyService;
    private final BCryptPasswordEncoder encoder;


    // 가입시 필요 정보 (다이어트, 근육생성, 건강관리 공통 정보)
    private String name;
    private Sex sex;
    private LocalDate birthDate;
    private double height;
    private double weight;
    private Double muscle;
    private Double bodyFat;
    private Activity activity;
    private BodyGoal goal;
    private double achieveWeight;
    private double achieveMuscle;
    private double achieveBodyFat;
    private List<String> diseases = new ArrayList<>();
    private List<String> drugs = new ArrayList<>();

    /**
     * 1. 회원가입 페이지에서 다이어트 클릭
     *
     * @param request name, height, sex
     * @return MemberJoinDietResponse- bmi, recommendedCalories
     */
    @PostMapping("/diet")
    public Result<MemberJoinDietResponse> joinToDiet(@RequestBody MemberJoinDietRequest request) {
        this.name = request.getName();
        this.sex = request.getSex();
        this.birthDate = request.getBirthDate();
        this.height = request.getHeight();
        this.weight = request.getWeight();
        this.muscle = request.getMuscle();
        this.bodyFat = request.getFat();
        this.goal = DIET;
        this.activity = request.getActivity();
        double averageWeight;
        try {
            averageWeight = bodyService.findAvgBodyProfileBySexAndHeight(sex, (int) height).getWeight();
            MemberJoinDietResponse memberJoinDietResponse = new MemberJoinDietResponse(name, sex, height, averageWeight);
            return new Result<>(HttpStatus.OK, null, memberJoinDietResponse);
        } catch (AppException e) {
            MemberJoinDietResponse memberJoinDietResponse = new MemberJoinDietResponse(null, null, null, null);
            return new Result<>(e.getErrorCode().getHttpStatus(), e.getMessage(), memberJoinDietResponse);
        }
    }

    /**
     * 체중 목표 설정시 실행됨. 목표 체중을 설정하면 bmi 반환
     *
     * @param request weight
     * @return MemberJoinDietWeightResponse- bmi
     */
    @PostMapping("/diet/weight")
    public Result<MemberJoinDietWeightResponse> setWeight(@RequestBody MemberJoinDietWeightRequest request) {
        this.achieveWeight = request.getAchieveWeight();

        // bmi 계산
        double bmi = achieveWeight / Math.pow(height / 100, height / 100);

        MemberJoinDietWeightResponse memberJoinDietWeightResponse = new MemberJoinDietWeightResponse(bmi);

        return new Result<>(HttpStatus.OK, null, memberJoinDietWeightResponse);
    }

    /**
     * 목표 체지방률 설정
     *
     * @param request achieveBodyFat
     * @return MemberJoinDietFatResponse- recommendedCalories
     */
    @PostMapping("/diet/fat")
    public Result<MemberJoinDietFatResponse> setAchieveBodyFat(@RequestBody MemberJoinDietFatRequest request) {
        this.achieveBodyFat = request.getAchieveBodyFat();

        // 권장 칼로리 계산을 위한 임시 멤버 객체 생성. (저장안함)
        Member tempUser = new Member(name, sex, birthDate, new PersonalInformation(new LogIn("temp_id", "temp_pw"), "temp_phone"), height, activity, goal, new AchieveBodyGoal(0, 0, 0), USER);
        tempUser.addBodyComposition(new BodyComposition(achieveWeight, null, null));
        int recommendedCalories = (int) tempUser.getRecommendedCalories();

        return new Result<>(HttpStatus.OK, null, new MemberJoinDietFatResponse(recommendedCalories));
    }

    /**
     * 회원 가입 직전 가입 정보 반환
     *
     * @return MemberJoinDietInfoResponse- name, bodyFat, achieveBodyFat, goal
     */
    @GetMapping("/diet/info")
    public Result<MemberJoinDietInfoResponse> getDietInfo() {
        return new Result<>(HttpStatus.OK, null, new MemberJoinDietInfoResponse(name, bodyFat, achieveBodyFat, goal));
    }

    @PostMapping("/muscle")
    public Result<MemberJoinMuscleResponse> joinToMuscle(@RequestBody MemberJoinMuscleRequest request) {
        this.name = request.getName();
        this.sex = request.getSex();
        this.birthDate = request.getBirthDate();
        this.height = request.getHeight();
        this.weight = request.getWeight();
        this.muscle = request.getMuscle();
        this.bodyFat = request.getFat();
        this.goal = MUSCLE;
        this.activity = request.getActivity();

        try {
            AverageBodyProfile bodyProfile = bodyService.findAvgBodyProfileBySexAndHeight(sex, (int) height);
            double averageMuscle = bodyProfile.getMuscle();
            MemberJoinMuscleResponse value = new MemberJoinMuscleResponse(name, sex, height, averageMuscle);
            return new Result<>(HttpStatus.OK, null, value);
        } catch (AppException e) {
            MemberJoinMuscleResponse value = new MemberJoinMuscleResponse(null, null, null, null);
            return new Result<>(e.getErrorCode().getHttpStatus(), e.getMessage(), value);
        }
    }

    /**
     * 목표 골격근량 설정
     *
     * @param request achieveMuscle
     * @return
     */
    @PostMapping("/muscle/weight")
    public Result setMuscleWeight(@RequestBody MemberJoinMuscleWeightRequest request) {
        achieveMuscle = request.getAchieveMuscle();

        return new Result(HttpStatus.OK, null, null);
    }

    /**
     * 사용자 정보 받아서 건강관리 페이지 클릭
     *
     * @param request MemberJoinHealthRequest
     * @return
     */
    @PostMapping("/health")
    public Result joinToHealth(@RequestBody MemberJoinHealthRequest request) {
        this.name = request.getName();
        this.sex = request.getSex();
        this.birthDate = request.getBirthDate();
        this.height = request.getHeight();
        this.weight = request.getWeight();
        this.muscle = request.getMuscle();
        this.bodyFat = request.getFat();
        this.goal = HEALTH_CARE;
        this.activity = request.getActivity();

        return new Result(HttpStatus.OK, null, null);
    }

    /**
     * 질병 키워드를 받아서 해당 키워드가 있는 질환 리스트를 반환함
     *
     * @param request keyword
     * @return 질병 리스트
     */
    @PostMapping("/health/disease")
    public Result<MemberJoinHealthDiseaseResponse> searchDisease(@RequestBody MemberJoinHealthDiseaseRequest request) {
        try {
            List<Disease> diseases = diseaseService.findDiseaseByKeyword(request.getKeyword());
            List<String> collect = diseases.stream().map(Disease::getName)
                    .collect(Collectors.toList());

            MemberJoinHealthDiseaseResponse value
                    = new MemberJoinHealthDiseaseResponse(collect);

            return new Result<>(HttpStatus.OK, null, value);
        } catch (AppException e) {
            return new Result<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null);
        }
    }

    @PostMapping("health/disease/select")
    public Result selectDisease(@RequestBody MemberJoinHealthDiseaseSelectRequest request) {
        try {
            String diseaseName = request.getName();
            boolean isDiseaseFound = diseases.stream()
                    .anyMatch(diseaseName::equals);
            if (isDiseaseFound) {
                throw new AppException(ErrorCode.DATA_ALREADY_EXISTED, "이미 선택된 질병입니다.");
            }

            diseaseService.findDiseaseByName(diseaseName); // 존재하는 질병인지 검사
            diseases.add(diseaseName);
            return new Result(HttpStatus.OK, null, null);
        } catch (AppException e) {
            return new Result(e.getErrorCode().getHttpStatus(), e.getMessage(), null);
        }
    }

    /**
     * 키워드 받아서 키워드가 포함된 약물 검색
     *
     * @param request keyword
     * @return
     */
    @PostMapping("/health/drug")
    public Result<MemberJoinHealthDrugResponse> searchDrug(@RequestBody MemberJoinHealthDrugRequest request) {
        try {
            List<Drug> drugs = drugService.findDrugByKeyword(request.getKeyword());
            List<String> collect = drugs.stream().map(Drug::getName)
                    .collect(Collectors.toList());

            MemberJoinHealthDrugResponse value
                    = new MemberJoinHealthDrugResponse(collect);

            return new Result<>(HttpStatus.OK, null, value);
        } catch (AppException e) {
            return new Result<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null);
        }
    }

    @PostMapping("/health/drug/select")
    public Result selectDrug(@RequestBody MemberJoinHealthDrugSelectRequest request) {
        try {
            String drugName = request.getName();
            boolean isDrugFound = drugs.stream()
                    .anyMatch(drugName::equals);
            if (isDrugFound) {
                throw new AppException(ErrorCode.DATA_ALREADY_EXISTED, "이미 선택된 약물입니다.");
            }

            drugService.findDrugByName(drugName);  // 존재하는 약물인지 검사
            drugs.add(drugName);
            return new Result(HttpStatus.OK, null, null);
        } catch (AppException e) {
            return new Result(e.getErrorCode().getHttpStatus(), e.getMessage(), null);
        }
    }

    /**
     * 회원가입하여 DB에 저장함
     *
     * @param request
     * @return
     */
    @PostMapping("/create")
    public Result<String> createMember(@RequestBody MemberJoinRequest request) {

        LogIn logIn = new LogIn(request.getUsername(), encoder.encode(request.getPassword()));  // 비밀번호를 spring security를 이용해서 hashing
        PersonalInformation personalInformation = new PersonalInformation(logIn, request.getPhoneNumber());
        BodyComposition bodyComposition = new BodyComposition(weight, muscle, bodyFat);
        AchieveBodyGoal bodyGoal = new AchieveBodyGoal(achieveWeight, achieveMuscle, achieveBodyFat);
        AccountType accountType = request.getAccountType();

        // 데이터가 존재하는지 검사(데이터 없을 시 에러 발생하면서 회원가입 실패)
        try {
            for (String avoidIngredientName : request.getAvoidIngredients()) {
                Ingredient avoidIngredient = ingredientService.findIngredientByName(avoidIngredientName);
            }
            /*for (String diseaseName : request.getDiseases()) {
                Disease findDisease = diseaseService.findDiseaseByName(diseaseName);
            }
            for (String drugName : request.getDrugs()) {
                Drug findDrug = drugService.findDrugByName(drugName);
            }*/
        } catch (AppException e) {
            return new Result<>(e.getErrorCode().getHttpStatus(), e.getMessage(), "잘못된 데이터 입력");
        }

        // create Member
        Member member = new Member(name, sex, birthDate, personalInformation,
                height, activity, goal, bodyGoal, accountType);
        Long memberId = null;

        // 회원 등록시 예외 처리 (중복 회원 가입 방지)
        try {
            // 회원 등록
            memberId = memberService.join(member);
        } catch (AppException e) {
            return new Result<>(e.getErrorCode().getHttpStatus(), e.getMessage(), "이미 존재하는 회원");
        }

        // add information
        memberService.updateAddBodyComposition(memberId, bodyComposition);
        for (String avoidIngredientName : request.getAvoidIngredients()) {
            Ingredient avoidIngredient = ingredientService.findIngredientByName(avoidIngredientName);
            memberService.updateAddAvoidIngredient(memberId, avoidIngredient);
        }
        for (String diseaseName : diseases) {
            Disease findDisease = diseaseService.findDiseaseByName(diseaseName);
            memberService.updateAddDisease(memberId, findDisease);
        }
        for (String drugName : drugs) {
            Drug findDrug = drugService.findDrugByName(drugName);
            memberService.updateAddDrug(memberId, findDrug);
        }

        return new Result<>(HttpStatus.OK, "회원가입 성공", memberId.toString());
    }

    /**
     * 유저 회원 가입 데이터 한번에 받기
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/create2")
    public Result join(@RequestBody MemberJoinRequest2 request) throws JsonProcessingException {
        String name = request.getName();
        Sex sex = request.getSex();
        LocalDate birthDate = request.getBirthDate();
        LogIn logIn = new LogIn(request.getUsername(), encoder.encode(request.getPassword()));  // 비밀번호를 spring security를 이용해서 hashing
        // PersonalInformation
        PersonalInformation personalInformation = new PersonalInformation(logIn, request.getPhoneNumber());
        // current body information
        double height = request.getHeight();
        BodyComposition bodyComposition = new BodyComposition(request.getWeight(), request.getMuscle(), request.getFat());
        // Activity
        Activity activity = Activity.valueOf(request.getActivity());
        // Goal
        BodyGoal goal = BodyGoal.valueOf(request.getGoal());
        // AchieveBodyGoal
        AchieveBodyGoal bodyGoal = new AchieveBodyGoal(request.getAchieveWeight(), request.getAchieveMuscle(), request.getAchieveBodyFat());
        // AccountType
        AccountType accountType = request.getAccountType();
        // 데이터가 존재하는지 검사(데이터 없을 시 에러 발생하면서 회원가입 실패)
        try {
            for (String avoidIngredientName : request.getAvoidIngredients()) {
                Ingredient avoidIngredient = ingredientService.findIngredientByName(avoidIngredientName);
            }
            for (String diseaseName : request.getDiseases()) {
                Disease findDisease = diseaseService.findDiseaseByName(diseaseName);
            }
            for (String drugName : request.getDrugs()) {
                Drug findDrug = drugService.findDrugByName(drugName);
            }
        } catch (AppException e) {
            return new Result(e.getErrorCode().getHttpStatus(), e.getMessage(), null);
        }
        // create Member
        Member member = new Member(name, sex, birthDate, personalInformation,
                height, activity, goal, bodyGoal, accountType);
        Long memberId = null;
        // 회원 등록시 예외 처리 (중복 회원 가입 방지)
        try {
            // 회원 등록
            memberId = memberService.join(member);
        } catch (AppException e) {
            return new Result(e.getErrorCode().getHttpStatus(), e.getMessage(), null);
        }
        // add information
        memberService.updateAddBodyComposition(memberId, bodyComposition);
        for (String avoidIngredientName : request.getAvoidIngredients()) {
            Ingredient avoidIngredient = ingredientService.findIngredientByName(avoidIngredientName);
            memberService.updateAddAvoidIngredient(memberId, avoidIngredient);
        }
        for (String diseaseName : request.getDiseases()) {
            Disease findDisease = diseaseService.findDiseaseByName(diseaseName);
            memberService.updateAddDisease(memberId, findDisease);
        }
        for (String drugName : request.getDrugs()) {
            Drug findDrug = drugService.findDrugByName(drugName);
            memberService.updateAddDrug(memberId, findDrug);
        }
        return new Result(HttpStatus.OK, memberId.toString(), null);
    }

    @GetMapping("/info/all")
    public MemberJoinTestRequest allData() {
        MemberJoinTestRequest build = MemberJoinTestRequest.builder()
                .name(name)
                .sex(sex)
                .birthDate(birthDate)
                .height(height)
                .weight(weight)
                .muscle(muscle)
                .bodyFat(bodyFat)
                .goal(goal)
                .activity(activity)
                .achieveWeight(achieveWeight)
                .achieveBodyFat(achieveBodyFat)
                .achieveMuscle(achieveMuscle)
                .diseases(diseases)
                .drugs(drugs)
                .build();

        return build;
    }
}
