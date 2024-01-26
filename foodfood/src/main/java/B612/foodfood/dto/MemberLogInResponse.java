package B612.foodfood.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class MemberLogInResponse{
    private HttpStatus Status;
    private String message;
    private String token;
}
