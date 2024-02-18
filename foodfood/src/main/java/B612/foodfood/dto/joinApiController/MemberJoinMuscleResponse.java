package B612.foodfood.dto.joinApiController;

import B612.foodfood.domain.Sex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinMuscleResponse {
    private String name;
    private Sex sex;
    private Double height;
    private Double muscle;
}
