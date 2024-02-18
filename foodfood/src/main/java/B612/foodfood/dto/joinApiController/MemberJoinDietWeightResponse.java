package B612.foodfood.dto.joinApiController;

import B612.foodfood.dto.HttpResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public class MemberJoinDietWeightResponse extends HttpResponse{
    private double bmi;
    private int recommendedCalories;

    public MemberJoinDietWeightResponse(HttpStatus status, String message, double bmi, int recommendedCalories) {
        super(status, message);
        this.bmi = bmi;
        this.recommendedCalories = recommendedCalories;
    }
}
