package B612.foodfood.controller.api;

import B612.foodfood.domain.*;
import B612.foodfood.dto.MemberJoinRequest;
import B612.foodfood.dto.MemberLogInRequest;
import B612.foodfood.exception.DataSaveException;
import B612.foodfood.exception.NoDataExistException;
import B612.foodfood.repository.AvoidFoodRepository;
import B612.foodfood.repository.MemberRepository;
import B612.foodfood.service.DiseaseService;
import B612.foodfood.service.DrugService;
import B612.foodfood.service.FoodService;
import B612.foodfood.service.MemberService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/members")
@Slf4j
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    private final FoodService foodService;
    private final DiseaseService diseaseService;
    private final DrugService drugService;
    private final AvoidFoodRepository avoidFoodRepository;

    private final BCryptPasswordEncoder encoder;

    /**
     * request: 유저 회원 가입 데이터 받기
     */
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody MemberJoinRequest request) throws NoDataExistException, DataSaveException {

        String name = request.getName();
        Sex sex = Sex.valueOf(request.getSex());
        LocalDate birthDate = LocalDate.parse(request.getBirthDate());
        // LogIn
        LogIn logIn = new LogIn(request.getId(), encoder.encode(request.getPassword()));  // 비밀번호를 spring security를 이용해서 hashing
        // PersonalInformation
        PersonalInformation personalInformation = new PersonalInformation(logIn, request.getPhoneNumber(), request.getEmail());
        // current body information
        double height = request.getHeight();
        BodyComposition bodyComposition = new BodyComposition(request.getWeight(), request.getMuscle(), request.getFat());
        // Activity
        Activity activity = Activity.valueOf(request.getActivity());
        // Goal
        BodyGoal goal = BodyGoal.valueOf(request.getGoal());
        // AchieveBodyGoal
        AchieveBodyGoal bodyGoal = new AchieveBodyGoal(request.getAchieveMuscle(), request.getAchieveBodyFat());
        // AccountType
        AccountType accountType = AccountType.valueOf(request.getAccountType());

        // 데이터가 존재하는지 검사(데이터 없을 시 에러 발생하면서 회원가입 실패)
        for (String avoidFoodName : request.getAvoidFoods()) {
            Food avoidFood = foodService.findFoodByName(avoidFoodName);
        }
        for (String diseaseName : request.getDiseases()) {
            Disease findDisease = diseaseService.findDiseaseByName(diseaseName);
        }
        for (String drugName : request.getDrugs()) {
            Drug findDrug = drugService.findDrugByName(drugName);
        }

        // create Member
        Member member = new Member(name, sex, birthDate, personalInformation,
                height, activity, goal, bodyGoal, accountType);

        Long memberId = memberService.join(member);


        // add information
        member.addBodyComposition(bodyComposition);
        for (String avoidFoodName : request.getAvoidFoods()) {
            Food avoidFood = foodService.findFoodByName(avoidFoodName);
            memberService.updateAddAvoidFood(memberId, avoidFood);
        }
        for (String diseaseName : request.getDiseases()) {
            Disease findDisease = diseaseService.findDiseaseByName(diseaseName);
            memberService.updateAddMemberDisease(memberId, findDisease);
        }
        for (String drugName : request.getDrugs()) {
            Drug findDrug = drugService.findDrugByName(drugName);
            memberService.updateAddMemberDrug(memberId, findDrug);
        }


        return ResponseEntity.ok("회원가입 성공" + " " + memberId);
    }

    @PostMapping("/login")
    public ResponseEntity<String> logIn(@RequestBody MemberLogInRequest request) {
        String token = memberService.login(request.getLogin_id(), request.getLogin_pw());

        return ResponseEntity.ok(token);
    }
}
