package B612.foodfood.controller.api;

import B612.foodfood.domain.*;
import B612.foodfood.dto.MemberJoinRequest;
import B612.foodfood.dto.MemberJoinResponse;
import B612.foodfood.dto.MemberLogInRequest;
import B612.foodfood.dto.MemberLogInResponse;
import B612.foodfood.exception.AppException;
import B612.foodfood.exception.DataSaveException;
import B612.foodfood.exception.ErrorCode;
import B612.foodfood.exception.NoDataExistException;
import B612.foodfood.repository.AvoidFoodRepository;
import B612.foodfood.repository.MemberRepository;
import B612.foodfood.service.DiseaseService;
import B612.foodfood.service.DrugService;
import B612.foodfood.service.FoodService;
import B612.foodfood.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.ErrorResponse;
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
    private final ObjectMapper objectMapper;

    /**
     * request: 유저 회원 가입 데이터 받기
     */
    @PostMapping("/join")
    public MemberJoinResponse join(@RequestBody MemberJoinRequest request) throws JsonProcessingException {

        String name = request.getName();
        Sex sex = Sex.valueOf(request.getSex());
        LocalDate birthDate = LocalDate.parse(request.getBirthDate());
        LogIn logIn = new LogIn(request.getUsername(), encoder.encode(request.getPassword()));  // 비밀번호를 spring security를 이용해서 hashing
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
        try {
            for (String avoidFoodName : request.getAvoidFoods()) {
                Food avoidFood = foodService.findFoodByName(avoidFoodName);
            }
            for (String diseaseName : request.getDiseases()) {
                Disease findDisease = diseaseService.findDiseaseByName(diseaseName);
            }
            for (String drugName : request.getDrugs()) {
                Drug findDrug = drugService.findDrugByName(drugName);
            }
        } catch (AppException e) {

            return new MemberJoinResponse(e.getErrorCode().getHttpStatus(),e.getMessage());
        }


        // create Member
        Member member = new Member(name, sex, birthDate, personalInformation,
                height, activity, goal, bodyGoal, accountType);
        Long memberId;

        // 회원 등록시 예외 처리 (중복 회원 가입 방지)
        try {
            // 회원 등록
            memberId = memberService.join(member);
        } catch (AppException e) {
            return new MemberJoinResponse(e.getErrorCode().getHttpStatus(),e.getMessage());
        }


        // add information
        member.addBodyComposition(bodyComposition);
        for (
                String avoidFoodName : request.getAvoidFoods()) {
            Food avoidFood = foodService.findFoodByName(avoidFoodName);
            memberService.updateAddAvoidFood(memberId, avoidFood);
        }
        for (
                String diseaseName : request.getDiseases()) {
            Disease findDisease = diseaseService.findDiseaseByName(diseaseName);
            memberService.updateAddMemberDisease(memberId, findDisease);
        }
        for (
                String drugName : request.getDrugs()) {
            Drug findDrug = drugService.findDrugByName(drugName);
            memberService.updateAddMemberDrug(memberId, findDrug);
        }

        return new MemberJoinResponse(HttpStatus.OK, memberId.toString());
    }

    @PostMapping("/login")
    public MemberLogInResponse logIn(@RequestBody MemberLogInRequest request) {
        String token = memberService.login(request.getUsername(), request.getPassword());

        return new MemberLogInResponse(token);
    }
}
