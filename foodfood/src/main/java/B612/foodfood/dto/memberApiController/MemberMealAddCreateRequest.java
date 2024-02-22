package B612.foodfood.dto.memberApiController;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberMealAddCreateRequest {
    // 음식 정보
    private String name; // 음식이름
    private double servingWeight;
    private double calories;
    private double carbonHydrate;
    private double protein;
    private double fat;
}
