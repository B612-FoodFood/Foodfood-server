package B612.foodfood.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Embeddable
@Getter
@NoArgsConstructor(access = PROTECTED)
public class LogIn {
    private String login_id;
    private String login_pw;

    public LogIn(String login_id, String login_pw) {
        this.login_id = login_id;
        this.login_pw = login_pw;
    }
}
