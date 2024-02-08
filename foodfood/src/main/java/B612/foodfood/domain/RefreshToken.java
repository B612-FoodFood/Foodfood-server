package B612.foodfood.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class RefreshToken {
    @Id
    @Column(name = "refresh_token_username")
    private String username;

    private String value;

    public void updateValue(String value) {
        this.value = value;
    }
}
