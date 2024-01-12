package B612.foodfood.domain;

import jakarta.persistence.*;

@Entity
public class PersonalInformation {
    @Id
    @GeneratedValue
    @Column(name = "personal_information_id")
    private Long id;

    private String email;
    @Embedded
    private Address address;
    @Embedded
    private Nutrition nutrition;
    @Embedded
    private LogIn logIn;
}
