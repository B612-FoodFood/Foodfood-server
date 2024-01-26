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
    private String email;

    /*@Embedded
    private Address address;*/

    public PersonalInformation(LogIn logIn, String phoneNumber, String email) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.logIn = logIn;
    }

    /**
     * 업데이트 로직
     */
    /*protected void updateAddress(Address address) {
        this.address = address;
    }*/

    protected void updatePassword(String password) {
        this.logIn = new LogIn(logIn.getUsername(), password);
    }

    protected void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    protected void updateEmail(String email) {
        this.email = email;
    }
}
