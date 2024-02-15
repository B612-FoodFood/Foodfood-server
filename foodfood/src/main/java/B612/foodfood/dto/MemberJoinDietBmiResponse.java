package B612.foodfood.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberJoinDietBmiResponse {
    private double bmi;
    private double recommendedCalories;
}
