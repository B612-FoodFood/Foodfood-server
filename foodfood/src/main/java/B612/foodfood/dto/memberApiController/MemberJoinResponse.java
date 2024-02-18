package B612.foodfood.dto.memberApiController;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class MemberJoinResponse {
    private HttpStatus Status;
    private String message;
}
