package B612.foodfood.controller.api;

import B612.foodfood.domain.Disease;
import B612.foodfood.domain.Drug;
import B612.foodfood.domain.Food;
import B612.foodfood.domain.Nutrition;
import B612.foodfood.dto.MemberJoinRequest;
import B612.foodfood.dto.MemberLogInRequest;
import B612.foodfood.exception.AppException;
import B612.foodfood.exception.DataSaveException;
import B612.foodfood.exception.ErrorCode;
import B612.foodfood.repository.AvoidFoodRepository;
import B612.foodfood.service.DiseaseService;
import B612.foodfood.service.DrugService;
import B612.foodfood.service.FoodService;
import B612.foodfood.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @WebMvcTest - JPA 기능은 동작하지 않는다.
 * - 여러 스프링 테스트 어노테이션 중, Web(Spring MVC)에만 집중할 수 있는 어노테이션
 * - @Controller, @ControllerAdvice 사용 가능
 * - 단, @Service, @Repository등은 사용할 수 없다.
 */

@WebMvcTest
class MemberApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    // MockBean으로 의존성 주입(흉내냄).
    @MockBean
    MemberService memberService;
    @MockBean
    FoodService foodService;
    @MockBean
    DiseaseService diseaseService;
    @MockBean
    DrugService drugService;
    @MockBean
    AvoidFoodRepository avoidFoodRepository;
    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ObjectMapper objectMapper;  //java 객체를 json으로 만들어주는 객체

    @BeforeEach
    public void each() throws DataSaveException {
        // WebMvcTest라서 동작 안함.
        foodService.save(new Food("food1", new Nutrition(1, 1, 1, 1, 1, 1, 1, 1, 1, 1)));
        foodService.save(new Food("food2", new Nutrition(1, 1, 1, 1, 1, 1, 1, 1, 1, 1)));
        diseaseService.save(new Disease("질병1"));
        diseaseService.save(new Disease("질병2"));
        drugService.save(new Drug("drug1"));
    }

    String id = "userName";
    String password = "1q2w3d4r";
    String email = "asdf@asfd.com";
    String phoneNumber = "010-1111-1111";
    String city = "seoul";
    String street = "street";
    String zipCode = "12345";
    String name = "name";
    String sex = "FEMALE";
    String birthDate = "2000-01-01";
    double height = 172.3;
    double weight = 65.3;
    double muscle = 35;
    double fat = 11;
    double achieveMuscle = 40;
    double achieveBodyFat = 10;
    String accountType = "ADMIN";
    String activity = "LOT";
    String goal = "MUSCLE";
    List<String> avoidFoods = new ArrayList<>();
    List<String> diseases = new ArrayList<>();
    List<String> drugs = new ArrayList<>();

    @Test
    @DisplayName("회원 가입 성공")
    /**
     * @WithMockUser
     * Spring Security를 사용하는 웹 애플리케이션의 테스트에서 인증된 사용자에 대한 시나리오를 만들고 테스트할 때 유용하게 사용됩니다.
     * 실제로 로그인을 수행하지 않고도 특정 사용자의 권한이나 인증 정보를 시뮬레이션하여 테스트할 수 있습니다.
     */
    @WithMockUser
    public void test1() throws Exception {
        // status 200

        mockMvc.perform(post("/api/v1/members/join")
                        .with(csrf())  // Spring Security Test 설정
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(
                                new MemberJoinRequest(name, sex, birthDate, id, password, phoneNumber, email,
                                        height, weight, muscle, fat, activity, goal, achieveMuscle, achieveBodyFat,
                                        accountType, avoidFoods, diseases, drugs))))
                .andDo(print())
                .andExpect(status().isOk());

    }


    /**
     * @WithAnonymousUser
     * Spring Security에서는 익명 사용자(Anonymous User)가 인증되지 않은 사용자를 나타냅니다.
     * 이 어노테이션을 사용하면 로그인을 수행하지 않은 상태에서의 동작을 테스트할 수 있습니다.
     */
    @Test
    @DisplayName("회원 가입 실패 - id 중복")
    @WithMockUser
    public void test2() throws Exception {
        //status 409
        when(memberService.join(any()))
                .thenThrow(new RuntimeException("회원 가입 실패"));  // ExceptionManager에서 RuntimeException은 HttpStauts.CONFILICT를 반환하도록 지정해놨음.

        mockMvc.perform(post("/api/v1/members/join")
                        .with(csrf())  //Spring Security 설정
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(
                                new MemberJoinRequest(name, sex, birthDate, id, password, phoneNumber, email,
                                        height, weight, muscle, fat, activity, goal,
                                        achieveMuscle, achieveBodyFat, accountType, avoidFoods, diseases, drugs))))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("로그인 성공")
    @WithMockUser
    public void test3() throws Exception {
        when(memberService.login(any(), any()))
                .thenReturn("token");

        mockMvc.perform(post("/api/v1/members/login")
                        .with(csrf())  // Spring Security Test 설정
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(
                                new MemberLogInRequest(id, password))))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("로그인 실패 - id 없음")
    @WithMockUser
    public void test4() throws Exception {
        when(memberService.login(any(), any()))
                .thenThrow(new AppException(ErrorCode.MEMBER_ID_NOT_FOUND, ""));

        mockMvc.perform(post("/api/v1/members/login")
                        .with(csrf())  // Spring Security Test 설정
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(
                                new MemberLogInRequest(id, password))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인 실패 - password 틀림")
    @WithMockUser
    public void test5() throws Exception {
        when(memberService.login(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PASSWORD, ""));

        mockMvc.perform(post("/api/v1/members/login")
                        .with(csrf())  // Spring Security Test 설정
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(
                                new MemberLogInRequest(id, password))))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }
}