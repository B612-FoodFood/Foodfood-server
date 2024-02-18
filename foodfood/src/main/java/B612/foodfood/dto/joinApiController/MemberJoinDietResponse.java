package B612.foodfood.dto.joinApiController;

import B612.foodfood.domain.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class MemberJoinDietResponse {
    private String name;
    private Sex sex;
    private Double height;
    private Double weight;
}
