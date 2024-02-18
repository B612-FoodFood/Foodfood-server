package B612.foodfood.controller.api;

import B612.foodfood.domain.Activity;
import B612.foodfood.domain.BodyGoal;
import B612.foodfood.domain.Sex;
import B612.foodfood.dto.jwtTestController.JwtTestResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static B612.foodfood.domain.BodyGoal.*;

@RestController
@RequestMapping("/api/v1/jwt")
public class JwtTestController {

    private String name;
    private Sex sex;
    private LocalDate birthDate;
    private double Height;
    private double weight;
    private Double muscle;
    private Double bodyFat;
    private Activity activity;
    private BodyGoal goal;

    @PostMapping("/name")
    public JwtTestResponse test(Authentication authentication) {
        System.out.println("name: " + authentication.getName());
        System.out.println("detail: " + authentication.getDetails());
        System.out.println("credential: " + authentication.getCredentials());
        System.out.println("authority: " + authentication.getAuthorities());

        // username을 json으로 변환하여 반환함
        return new JwtTestResponse(authentication.getName());
    }

    @PostMapping("/goal")
    public BodyGoal test2() {
        goal = DIET;
        return goal;
    }

    @GetMapping("/goal/1")
    public BodyGoal test3() {
        return goal;
    }
}
