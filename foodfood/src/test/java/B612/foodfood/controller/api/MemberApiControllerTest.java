package B612.foodfood.controller.api;

import B612.foodfood.domain.Disease;
import B612.foodfood.domain.Drug;
import B612.foodfood.domain.Food;
import B612.foodfood.domain.Nutrition;
import B612.foodfood.dto.MemberJoinRequest;
import B612.foodfood.exception.DataSaveException;
import B612.foodfood.repository.AvoidFoodRepository;
import B612.foodfood.service.DiseaseService;
import B612.foodfood.service.DrugService;
import B612.foodfood.service.FoodService;
import B612.foodfood.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
    public void test1() throws Exception {
        // status 200

        mockMvc.perform(post("/api/v1/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(
                                new MemberJoinRequest(name, sex, birthDate, id, password, phoneNumber, email, city, street,
                                        zipCode, height, weight, muscle, fat, activity, goal, achieveMuscle, achieveBodyFat,
                                        accountType, avoidFoods, diseases, drugs))))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("회원 가입 실패 - id 중복")
    public void test2() throws Exception {
        //status 409

        when(memberService.join(any()))
                .thenThrow(new DataSaveException("해당 memberId가 중복 됩니다"));

        mockMvc.perform(post("/api/v1/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(
                                new MemberJoinRequest(name, sex, birthDate, id, password, phoneNumber, email,
                                        city, street, zipCode, height, weight, muscle, fat, activity, goal,
                                        achieveMuscle, achieveBodyFat, accountType, avoidFoods, diseases, drugs))))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}