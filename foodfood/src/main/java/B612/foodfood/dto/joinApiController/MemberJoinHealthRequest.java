package B612.foodfood.dto.joinApiController;

import B612.foodfood.domain.Activity;
import B612.foodfood.domain.Sex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinHealthRequest {
    private String name;
    private Sex sex;
    private LocalDate birthDate;
    private double height;
    private double weight;
    private Double muscle;
    private Double fat;
    private Activity activity;
}
