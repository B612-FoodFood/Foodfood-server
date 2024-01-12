package B612.foodfood.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class LogIn {
    private String login_id;
    private String login_pw;
}
