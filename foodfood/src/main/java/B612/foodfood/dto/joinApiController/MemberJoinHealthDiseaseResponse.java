package B612.foodfood.dto.joinApiController;

import B612.foodfood.domain.Disease;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinHealthDiseaseResponse {
    List<String> diseases;
}
