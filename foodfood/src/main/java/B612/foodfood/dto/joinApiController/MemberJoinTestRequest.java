package B612.foodfood.dto.joinApiController;

import B612.foodfood.domain.*;
import jdk.jfr.Name;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Data
@Builder
@Setter
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
    private List<String> diseases;
    private List<String> drugs;
}
