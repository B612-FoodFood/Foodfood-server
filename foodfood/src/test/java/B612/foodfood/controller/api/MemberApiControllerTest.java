package B612.foodfood.controller.api;

import B612.foodfood.domain.Address;
import B612.foodfood.domain.dto.MemberJoinRequest;
import B612.foodfood.service.FoodService;
import B612.foodfood.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
class MemberApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberService memberService;
    @MockBean
    FoodService foodService;

    @Autowired
    ObjectMapper objectMapper;  //java 객체를 json으로 만들어주는 객체


    @Test
    @DisplayName("회원 가입 성공")
    public void test1() throws Exception {
        //given
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
        List<String> avoidFoods = new ArrayList<>();
        avoidFoods.add("stone");
        avoidFoods.add("mellon");
        String goal = "MUSCLE";
        List<String> disease = new ArrayList<>();
        disease.add("질병1");
        disease.add("질병2");
        List<String> drug = new ArrayList<>();
        drug.add("drug1");
        //when
        mockMvc.perform(post("/api/v1/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(
                                new MemberJoinRequest(name, sex, birthDate, id, password,
                                        phoneNumber, email, city, street, zipCode, height,
                                        activity, goal, achieveMuscle, achieveBodyFat, accountType,
                                        weight, muscle, fat, avoidFoods, disease, drug))))
                .andDo(print())
                .andExpect(status().isOk());

        //then

    }

    @Test
    @DisplayName("회원 가입 실패 - id 중복")
    public void test2() throws Exception {
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
        List<String> avoidFoods = new ArrayList<>();
        avoidFoods.add("stone");
        avoidFoods.add("mellon");
        String goal = "MUSCLE";
        List<String> disease = new ArrayList<>();
        disease.add("질병1");
        disease.add("질병2");
        List<String> drug = new ArrayList<>();
        drug.add("drug1");

        //when
        mockMvc.perform(post("/api/v1/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(
                                new MemberJoinRequest(name, sex, birthDate, id, password,
                                        phoneNumber, email, city, street, zipCode, height,
                                        activity, goal, achieveMuscle, achieveBodyFat, accountType,
                                        weight, muscle, fat, avoidFoods, disease, drug))))
                .andDo(print())
                .andExpect(status().isConflict());

        //then

    }
}