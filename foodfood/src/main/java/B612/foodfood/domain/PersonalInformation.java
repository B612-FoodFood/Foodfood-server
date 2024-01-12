package B612.foodfood.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class PersonalInformation {
    @Id
    @GeneratedValue
    @Column(name = "personal_information_id")
    private Long id;

    private String phoneNumber;
    private String email;

    @Embedded
    private Address address;

    @Embedded
    private LogIn logIn;

    public PersonalInformation(String phoneNumber, String email, Address address, LogIn logIn) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.logIn = logIn;
    }
}
