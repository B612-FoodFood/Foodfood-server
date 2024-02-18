package B612.foodfood.dto.joinApiController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class MemberJoinDietWeightResponse{
    private double bmi;
}
