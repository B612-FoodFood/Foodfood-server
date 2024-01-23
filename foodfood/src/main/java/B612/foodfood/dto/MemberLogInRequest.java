package B612.foodfood.dto;

import lombok.*;

import static lombok.AccessLevel.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class MemberLogInRequest {
    String login_id;
    String login_pw;
}
