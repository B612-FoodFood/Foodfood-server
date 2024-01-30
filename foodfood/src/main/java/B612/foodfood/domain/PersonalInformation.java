package B612.foodfood.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import static lombok.AccessLevel.*;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
public class PersonalInformation {
    @Id
    @GeneratedValue
    @Column(name = "personal_information_id")
    private Long id;

    @Embedded
    @Setter
    private LogIn logIn;

    private String phoneNumber;

    public PersonalInformation(LogIn logIn, String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.logIn = logIn;
    }

    /**
     * 업데이트 로직
     */

    protected void updatePassword(String password) {
        this.logIn = new LogIn(logIn.getUsername(), password);
    }

    protected void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
