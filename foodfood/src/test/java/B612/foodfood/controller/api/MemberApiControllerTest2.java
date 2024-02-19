package B612.foodfood.controller.api;

import B612.foodfood.domain.*;
import B612.foodfood.dto.joinApiController.MemberJoinRequest;
import B612.foodfood.exception.DataSaveException;
import B612.foodfood.service.DiseaseService;
import B612.foodfood.service.DrugService;
import B612.foodfood.service.FoodService;
import B612.foodfood.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @AutoConfigureMockMvc : WebMvcTest가 아닌 SpringBootTest에서도 MockMvc를 사용 가능하게 만들어줌 (Service, Repository 사용 가능)
 * @WebMvcTest : controller 테스트만 진행하는 경우(Service, Repository 사용 불가)
 */
@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class MemberApiControllerTest2 {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberService memberService;
    @Autowired
    FoodService foodService;
    @Autowired
    DiseaseService diseaseService;
    @Autowired
    DrugService drugService;

    @Autowired
    ObjectMapper objectMapper;  //java 객체를 json으로 만들어주는 객체

    @Before
    public void each() throws DataSaveException {
        // WebMvcTest라서 동작 안함.
        foodService.save(new Food("food-apple", new Nutrition(1, 1, 1, 1, 1, 1, 1, 1, 1, 1), Category.견과류및종실류));
        foodService.save(new Food("food-banana", new Nutrition(1, 1, 1, 1, 1, 1, 1, 1, 1, 1),Category.견과류및종실류));
        diseaseService.save(new Disease("disease-a"));
        diseaseService.save(new Disease("disease-b"));
        drugService.save(new Drug("drug-a"));
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
        //given
        avoidFoods.add("food-apple");
        avoidFoods.add("food-banana");
        diseases.add("disease-a");
        diseases.add("disease-b");
        drugs.add("drug-a");

        mockMvc.perform(post("/api/v1/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(
                                new MemberJoinRequest(name, sex, birthDate, id, password, phoneNumber,
                                        height, weight, muscle, fat, activity, goal, achieveMuscle, achieveBodyFat,
                                        accountType, avoidFoods, diseases, drugs))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 가입 실패 - id 중복")
    public void test2() throws Exception {
        //given
        avoidFoods.add("food-apple");
        avoidFoods.add("food-banana");
        diseases.add("disease-a");
        diseases.add("disease-b");
        drugs.add("drug-a");

        //when
        mockMvc.perform(post("/api/v1/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(
                        new MemberJoinRequest(name, sex, birthDate, id, password, phoneNumber,
                                height, weight, muscle, fat, activity, goal,
                                achieveMuscle, achieveBodyFat, accountType, avoidFoods, diseases, drugs))));

        //then
        mockMvc.perform(post("/api/v1/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(
                                new MemberJoinRequest(name, sex, birthDate, id, password, phoneNumber,
                                        height, weight, muscle, fat, activity, goal,
                                        achieveMuscle, achieveBodyFat, accountType, avoidFoods, diseases, drugs))))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}
