package B612.foodfood.controller.api;

import B612.foodfood.domain.*;
import B612.foodfood.dto.*;
import B612.foodfood.dto.joinApiController.MemberJoinRequest;
import B612.foodfood.dto.memberApiController.MemberJoinRequest2;
import B612.foodfood.dto.memberApiController.MemberJoinResponse;
import B612.foodfood.dto.memberApiController.MemberLogInRequest;
import B612.foodfood.dto.memberApiController.MemberLogInResponse;
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

@RestController
@RequestMapping("/api/v1/members")
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

    /**
     * request: 유저 회원 가입 데이터 받기
     */
    @PostMapping("/join2")
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

    @PostMapping("/info")
    public Result memberPage(Authentication authentication) {
        String userName = authentication.getName();
        // 로그인한 멤버의 각종 정보 반환 로직 구현
        return new Result(HttpStatus.OK, null,null);
    }
}
