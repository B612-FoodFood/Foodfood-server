package B612.foodfood.dto.joinApiController;

import B612.foodfood.domain.Activity;
import B612.foodfood.domain.BodyGoal;
import B612.foodfood.domain.Sex;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class MemberJoinTestRequest {
    private String name;
    private Sex sex;
    private LocalDate birthDate;
    private double height;
    private double weight;
    private Double muscle;
    private Double bodyFat;
    private Activity activity;
    private BodyGoal goal;
    private double achieveWeight;
    private double achieveBodyFat;
    private double achieveMuscle;
}
