package B612.foodfood.dto.memberApiController;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberEditWeightRequest {
    private double weight;
    private double achieveWeight;
}
