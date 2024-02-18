package B612.foodfood.dto.memberApiController;

import lombok.*;

import static lombok.AccessLevel.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class MemberLogInRequest {
    String username;
    String password;
}
