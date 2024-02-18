package B612.foodfood.controller.api;

import B612.foodfood.domain.*;
import B612.foodfood.dto.HttpResponse;
import B612.foodfood.dto.joinApiController.*;
import B612.foodfood.exception.AppException;
import B612.foodfood.service.AverageBodyProfileService;
import B612.foodfood.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static B612.foodfood.domain.AccountType.*;
import static B612.foodfood.domain.Activity.*;
import static B612.foodfood.domain.Activity.LOT;
import static B612.foodfood.domain.BodyGoal.*;
import static B612.foodfood.domain.Sex.FEMALE;
import static B612.foodfood.domain.Sex.MALE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members/join")
@Slf4j
public class JoinApiController {

    private final MemberService memberService;
    private final AverageBodyProfileService bodyService;

    // 가입시 필요 정보 (다이어트, 근육생성, 건강관리 공통 정보)
    private String name;
    private Sex sex;
    private LocalDate birthDate;
    private double height;
    private double weight;
    private Double muscle;
    private Double fat;
    private Activity activity;
    private BodyGoal goal;

    /**
     * 1. 회원가입 페이지에서 다이어트 클릭
     *
     * @param request : name, height, sex
     * @return MemberJoinDietResponse : bmi, recommendedCalories
     */
    @PostMapping("/diet")
    public MemberJoinDietResponse joinToDiet(@RequestBody MemberJoinDietRequest request) {
        this.name = request.getName();
        this.sex = request.getSex();
        this.birthDate = request.getBirthDate();
        this.height = request.getHeight();
        this.weight = request.getWeight();
        this.muscle = request.getMuscle();
        this.fat = request.getFat();
        this.goal = DIET;
        this.activity = request.getActivity();
        double averageWeight;
        try {
            System.out.println("(int)height = " + (int)height);
            averageWeight = bodyService.findAvgBodyProfileBySexAndHeight(sex, (int) height).getWeight();
        } catch (AppException e) {
            return new MemberJoinDietResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, null, null, null);
        }

        return new MemberJoinDietResponse(HttpStatus.OK, null, name, sex, height, averageWeight);
    }

    /**
     * 체중 목표 설정시 실행됨. 목표 체중을 설정하고
     * @param request : weight
     * @return MemberJoinDietWeightResponse : bmi, recommendedCalories
     */
    @PostMapping("/diet/weight")
    public MemberJoinDietWeightResponse setWeight(@RequestBody MemberJoinDietWeightRequest request) {
        this.weight = request.getAchieveWeight();

        // bmi 계산
        double bmi = weight / Math.pow(height / 100, height / 100);


        // 권장 칼로리 계산을 위한 임시 멤버 객체 생성. (저장안함)
        Member tempUser = new Member(name, sex, birthDate, new PersonalInformation(new LogIn("temp_id", "temp_pw"), "temp_phone"), height, activity, goal, new AchieveBodyGoal(0,0, 0), USER);
        tempUser.addBodyComposition(new BodyComposition(weight, null, null));
        int recommendedCalories = (int) tempUser.getRecommendedCalories();
        return new MemberJoinDietWeightResponse(HttpStatus.OK, null, bmi, recommendedCalories);
    }

    @GetMapping("/info/all")
    public MemberJoinTestRequest allData() {

        return new MemberJoinTestRequest()
                .builder()
                .name(name)
                .sex(sex)
                .birthDate(birthDate)
                .height(height)
                .weight(weight)
                .muscle(muscle)
                .fat(fat)
                .goal(goal)
                .activity(activity)
                .build();
    }


}
