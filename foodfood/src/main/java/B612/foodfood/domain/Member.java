package B612.foodfood.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;

@Entity
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private Date birthDate;
    private double height;

    @Enumerated
    private Sex sex;
    @Enumerated
    private Activity activity;
    @Enumerated
    private AccountType accountType;

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "personal_information_id")
    private PersonalInformation personalInformation;

    @OneToMany(mappedBy = "member", cascade = ALL)
    private List<Goal> goals = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL)
    private List<BodyComposition> bodyCompositions = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL)
    private List<MemberAllergy> memberAllergies = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL)
    private List<MemberDisease> memberDiseases = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL)
    private List<Meal> meals = new ArrayList<>();
}
