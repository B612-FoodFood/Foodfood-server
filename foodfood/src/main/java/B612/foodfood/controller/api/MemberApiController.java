package B612.foodfood.controller.api;

import B612.foodfood.domain.*;
import B612.foodfood.domain.dto.MemberJoinRequest;
import B612.foodfood.exception.NoDataExistException;
import B612.foodfood.service.FoodService;
import B612.foodfood.service.MemberService;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static B612.foodfood.domain.Sex.*;

@RestController
@RequestMapping("/api/v1/members")
@Slf4j
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    private final FoodService foodService;

    /**
     * request: 유저 회원 가입 데이터 받기
     */
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody MemberJoinRequest request) throws NoDataExistException {

        // LogIn
        String id = request.getId();
        String password = request.getPassword();

        String name = request.getName();
        Sex sex = Sex.valueOf(request.getSex());
        LocalDate birthDate = LocalDate.parse(request.getBirthDate());

        // current body information
        double height = request.getHeight();
        double weight = request.getWeight();
        double muscle = request.getMuscle();
        double fat = request.getFat();

        // AchieveBodyGoal
        double achieveMuscle = request.getAchieveMuscle();
        double achieveBodyFat = request.getAchieveBodyFat();

        // Activity
        Activity activity = Activity.valueOf(request.getActivity());

        // AvoidFoods
        List<Food> avoidFoods = new ArrayList<>();
        for (String avoidFood : request.getAvoidFoods()) {
            Food findFood = foodService.findFoodByName(avoidFood);
            avoidFoods.add(findFood);
        }

        // Goal
        BodyGoal goal = BodyGoal.valueOf(request.getGoal());

        return ResponseEntity.ok("회원가입 성공");
    }
}
