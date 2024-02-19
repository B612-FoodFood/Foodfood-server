package B612.foodfood.dto.joinApiController;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinHealthDrugResponse {
    private List<String> drugs;
}
