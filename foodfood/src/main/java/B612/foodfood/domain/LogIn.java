package B612.foodfood.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.PROTECTED;

@Embeddable
@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
public class LogIn {
    private String username;
    private String password;

    public LogIn(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
