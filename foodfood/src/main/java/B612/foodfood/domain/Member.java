package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static B612.foodfood.domain.Obesity.*;
import static B612.foodfood.domain.Sex.FEMALE;
import static B612.foodfood.domain.Sex.MALE;
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
    @Enumerated
    private Obesity obesity;

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

    public void addGoals(Goal goal) {
        goal.setMember(this);
        goals.add(goal);
    }

    public void addBodyComposition(BodyComposition bodyComposition) {
        if (bodyComposition.getBodyFat() == null) {
            if (bodyCompositions.isEmpty()) {
                // 회원가입시 체지방률 정보가 존재하지 않는 경우 체지방률을 0으로 설정
                bodyComposition.setBodyFat(0D);
            } else {
                // 이외에 신체정보 기입 시 체지방률 정보가 없는 경우 직전의 체지방률 정보로 입력됨.(체지방률이 유지된다고 가정)
                Double recentBodyFat =
                        bodyCompositions.get(bodyCompositions.size() - 1).getBodyFat();
                bodyComposition.setBodyFat(recentBodyFat);
            }
        }

        bodyComposition.setMember(this);
        bodyCompositions.add(bodyComposition);
        setObesityBySex(bodyComposition);
    }

    public void addBodyComposition(double weight, double height, Double bodyFat) {
        if (bodyFat == null) {
            if (bodyCompositions.isEmpty()) {
                // 회원가입시 체지방률 정보가 존재하지 않는 경우 체지방률을 0으로 설정
                bodyFat = 0D;
            } else {
                // 이외에 신체정보 기입 시 체지방률 정보가 없는 경우 직전의 체지방률 정보로 입력됨.(체지방률이 유지된다고 가정)
                bodyFat = bodyCompositions.get(bodyCompositions.size() - 1).getBodyFat();
            }
        }

        BodyComposition bodyComposition =
                new BodyComposition(weight, height, bodyFat);
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

    /**
     * 비즈니스 로직
     */
    private void setObesityBySex(BodyComposition bodyComposition) throws IllegalStateException {
        if (sex == MALE) {
            setObesity(bodyComposition, 10, 20);
        } else if (sex == FEMALE) {
            setObesity(bodyComposition, 15, 25);
        } else {
            throw new IllegalStateException("""
                    오류발생
                    발생지점: setObesityBySex(Sex sex)
                    발생이유: 성별이 정해지지 않았습니다.""");
        }
    }

    private void setObesity(BodyComposition bodyComposition, int underWeightPoint, int overWeightPoint) {
        if (bodyComposition.getBodyFat() < underWeightPoint) {
            this.obesity = UNDER;
        } else if (bodyComposition.getBodyFat() < overWeightPoint) {
            this.obesity = STANDARD;
        } else {
            this.obesity = OVER;
        }
    }

    /**
     * 업데이트 로직
     */

    public void updatePassword(String password) {
        this.personalInformation.updatePassword(password);
    }

    public void updateAddress(String city, String street, String zipcode) {
        Address address = new Address(city, street, zipcode);
        this.personalInformation.updateAddress(address);
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.personalInformation.updatePhoneNumber(phoneNumber);
    }

    public void updateEmail(String email) {
        this.personalInformation.updateEmail(email);
    }
}
