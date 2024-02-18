package B612.foodfood.controller.api;

import B612.foodfood.domain.*;
import B612.foodfood.dto.*;
import B612.foodfood.dto.memberApiController.MemberJoinRequest;
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
}
