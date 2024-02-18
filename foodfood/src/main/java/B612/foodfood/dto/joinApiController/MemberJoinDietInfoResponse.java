package B612.foodfood.dto.joinApiController;

import B612.foodfood.domain.BodyGoal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinDietInfoResponse {
    private String name;
    private double bodyFat;
    private double achieveBodyFat;
    private BodyGoal goal;
}
