package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@ToString(exclude = {"personalInformation", "goals", "bodyCompositions", "memberAllergies", "memberDiseases", "meals"})
@NoArgsConstructor(access = PROTECTED)
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;
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
    @Setter
    private PersonalInformation personalInformation;

    public Member(String name, Date birthDate, double height, Sex sex, Activity activity, AccountType accountType) {
        this.name = name;
        this.birthDate = birthDate;
        this.height = height;
        this.sex = sex;
        this.activity = activity;
        this.accountType = accountType;
    }

    @OneToMany(mappedBy = "member", cascade = ALL)
    private List<Goal> goals = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true)
    private List<BodyComposition> bodyCompositions = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true)
    private List<MemberAllergy> memberAllergies = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true)
    private List<MemberDisease> memberDiseases = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL)
    private List<Meal> meals = new ArrayList<>();

    /**
     * 연관관계 편의 메서드
     **/
    public void addMeal(Meal meal) {
        meal.setMember(this);
        meals.add(meal);
    }

    public void addBodyComposition(BodyComposition bodyComposition) {
        bodyComposition.setMember(this);
        bodyCompositions.add(bodyComposition);
    }

    // 이 메서드는 Member, MemberAllergy, Allergy간의 연관관계 관리를 Member에서 처리할 수 있도록 해줌.
    public void addAllergy(Allergy allergy) {
        MemberAllergy memberAllergy =
                MemberAllergy.createMemberAllergy(allergy);
        memberAllergy.setMember(this);
        memberAllergies.add(memberAllergy);
    }

    // 이 메서드는 Member, MemberAllergy, Allergy간의 연관관계 관리를 Member에서 처리할 수 있도록 해줌.
    public void addDisease(Disease disease) {
        MemberDisease memberDisease =
                MemberDisease.createMemberDisease(disease);
        memberDisease.setMember(this);
        memberDiseases.add(memberDisease);
    }
}
