package B612.foodfood.domain;

import B612.foodfood.exception.AppException;
import B612.foodfood.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static B612.foodfood.domain.Activity.*;
import static B612.foodfood.domain.BodyGoal.*;
import static B612.foodfood.domain.Obesity.*;
import static B612.foodfood.domain.Sex.FEMALE;
import static B612.foodfood.domain.Sex.MALE;
import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@Slf4j
@ToString(exclude = {"bodyCompositions", "avoidIngredients", "memberDiseases", "memberDrugs", "meals"})
@NoArgsConstructor(access = PROTECTED)
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;
    @Column(columnDefinition = "date")  // 미 지정시 DB에 TimeStamp 타입으로 저장됨. 왜 그런진 모르겠음
    private LocalDate birthDate;
    private int age;
    private double height;

    private double basalMetabolicRate; // 기초대사량, Activity에 따라 변화
    private double recommendedCalories; // 각 BodyGoal 마다 권장되는 칼로리

    // 탄단지 비중 5:3:2
    private double recommendedCarbonHydrate; // 권장되는 탄수화물
    private double recommendedProtein; // 권장되는 단백질
    private double recommendedFat; // 권장되는 지방

    @Enumerated(STRING)
    private Sex sex;
    @Enumerated(STRING)
    private Activity activity;
    @Enumerated(STRING)
    private AccountType accountType;
    @Enumerated(STRING)
    private BodyGoal goal;
    @Enumerated(STRING)
    private Obesity obesity;

    @Embedded
    private AchieveBodyGoal achieveBodyGoal;

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "personal_information_id")
    // 민감한 개인 정보
    private PersonalInformation personalInformation;  // member는 persist 전에 반드시 personalInformation이 Member안에 있어야 함. 안 그러면 member 객체 생성 불가

    public Member(String name, Sex sex, LocalDate birthDate,
                  PersonalInformation personalInformation, double height,
                  Activity activity, BodyGoal goal,
                  AchieveBodyGoal achieveBodyGoal, AccountType accountType) {
        this.name = name;
        this.sex = sex;
        this.birthDate = birthDate;
        this.age = LocalDate.now().getYear() - birthDate.getYear();
        this.personalInformation = personalInformation;
        this.height = height;
        this.activity = activity;
        this.goal = goal;
        this.achieveBodyGoal = achieveBodyGoal;
        this.accountType = accountType;
    }

    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true)
    private List<BodyComposition> bodyCompositions = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true)
    private List<AvoidIngredient> avoidIngredients = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true)
    private List<MemberDisease> memberDiseases = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true)
    private List<MemberDrug> memberDrugs = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL)
    private List<Meal> meals = new ArrayList<>();


    /**
     * 연관관계 편의 메서드
     **/
    public void addMeal(Meal meal) {
        // 한 유저의 동일한 날짜에 대한 Meal은 하나만 존재 가능
        for (Meal memberMeal : meals) {
            if (memberMeal.getDate().equals(meal.getDate())) {
                throw new AppException(ErrorCode.DATA_ALREADY_EXISTED,
                        """
                                오류 발생
                                발생위치: Member.addMeal(Meal meal)
                                발생원인: 해당 유저는 이미 해당 날짜에 대한 식사 내역이 존재합니다.""");
            }
        }

        meal.setMember(this);
        meals.add(meal);
    }

    public void addBodyComposition(BodyComposition bodyComposition) {
        LocalDate date = bodyComposition.getDate();
        if (!bodyCompositions.isEmpty()) {
            // 현재 날짜에 이미 BodyComposition이 있다면 기존의 BodyComposition을 삭제
            for (int idx = 0; idx < bodyCompositions.size(); idx++) {
                BodyComposition composition = bodyCompositions.get(idx);
                if (composition.getDate().equals(date)) {
                    log.info("delete body composition {}",composition.getId());
                    bodyCompositions.remove(idx);
                }
            }
            /*int lastIndex = bodyCompositions.size() - 1;
            log.info("bodyCompositions.get(lastIndex).getDate() {}",bodyCompositions.get(lastIndex).getDate());
            log.info("date {}",date);
            if (bodyCompositions.get(lastIndex).getDate().equals(date)) {
                log.info("delete body composition");
                bodyCompositions.remove(lastIndex);
            }*/
        }

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

        if (bodyComposition.getMuscle() == null) {
            if (bodyCompositions.isEmpty()) {
                // 회원가입시 골격근량 정보가 존재하지 않는 경우 골격근량을 0으로 설정
                bodyComposition.setMuscle(0D);
            } else {
                // 이외에 신체정보 기입 시 골격근량 정보가 없는 경우 직전의 골격근량 정보로 입력됨.(골격근이 유지된다고 가정)
                Double recentMuscle =
                        bodyCompositions.get(bodyCompositions.size() - 1).getMuscle();
                bodyComposition.setMuscle(recentMuscle);
            }
        }

        bodyComposition.setMember(this);
        bodyCompositions.add(bodyComposition);

        // 비만도 설정
        setObesityBySex(bodyComposition);

        // 기초대사량 설정
        basalMetabolicRate = calculateBMR(bodyComposition);
        // 일일 권장 칼로리 설정
        recommendedCalories = calculateRecommendedCalories();
        // 일일 권장 탄수화물량 설정
        recommendedCarbonHydrate = calculateRecommendedCarbonHydrate(recommendedCalories);
        // 일일 권장 단백질량 설정
        recommendedProtein = calculateRecommendedProtein(recommendedCalories);
        // 일일 권장 지방량 설정
        recommendedFat = calculateRecommendedFat(recommendedCalories);

    }

    // 이 메서드는 Member, AvoidFood, Food간의 연관관계 관리를 Member에서 처리할 수 있도록 해줌.
    // 유저에게 음식추천해주는 로직 구현시 여기 안에 들어있는 음식은 추천해주지 않도록 구현
    public AvoidIngredient addAvoidIngredient(Ingredient ingredient) {
        AvoidIngredient avoidIngredient =
                AvoidIngredient.createAvoidIngredient(ingredient);
        avoidIngredient.setMember(this);
        avoidIngredients.add(avoidIngredient);
        return avoidIngredient;
    }

    // 이 메서드는 Member, MemberDisease, Disease간의 연관관계 관리를 Member에서 처리할 수 있도록 해줌.
    public MemberDisease addDisease(Disease disease) {
        MemberDisease memberDisease =
                MemberDisease.createMemberDisease(disease);
        memberDisease.setMember(this);
        memberDiseases.add(memberDisease);
        return memberDisease;
    }

    // 이 메서드는 Member, MemberDrug, Drug간의 연관관계 관리를 Member에서 처리할 수 있도록 해줌.
    public MemberDrug addDrug(Drug drug) {
        MemberDrug memberDrug =
                MemberDrug.createMemberDrug(drug);
        memberDrug.setMember(this);
        memberDrugs.add(memberDrug);

        return memberDrug;
    }

    /**
     * 비즈니스 로직
     */
    public Optional<BodyComposition> findBodyCompositionByDate(LocalDate date) {
        return bodyCompositions.stream()
                .filter(bodyComposition -> bodyComposition.getDate().equals(date))
                .findFirst();
    }

    public Optional<Meal> findMealByDate(LocalDate date) {
        return meals.stream()
                .filter(meal -> meal.getDate().equals(date))
                .findFirst();
    }

    /**
     * @param recommendedCalories
     * @return 섭취 탄수화물량 (g)
     */
    private double calculateRecommendedCarbonHydrate(double recommendedCalories) {
        return (recommendedCalories * 0.5) / 4;
    }

    /**
     * @param recommendedCalories
     * @return 섭취 단백질량 (g)
     */
    private double calculateRecommendedProtein(double recommendedCalories) {
        return (recommendedCalories * 0.3) / 4;
    }

    /**
     * @param recommendedCalories
     * @return 섭취 지방량 (g)
     */
    private double calculateRecommendedFat(double recommendedCalories) {
        return (recommendedCalories * 0.2) / 9;
    }

    /**
     * @return 섭취할 적정 칼로리 (kcal)
     */
    private double calculateRecommendedCalories() {
        double maintenanceCalories = 0;

        // 활동량에 따른 유지 칼로리 계산
        if (activity == VERY_LITTLE) {
            maintenanceCalories = 1.2 * basalMetabolicRate;
        } else if (activity == LITTLE) {
            maintenanceCalories = 1.375 * basalMetabolicRate;
        } else if (activity == NORMAL) {
            maintenanceCalories = 1.55 * basalMetabolicRate;
        } else if (activity == LOT) {
            maintenanceCalories = 1.725 * basalMetabolicRate;
        }

        // 목표에 따른 권장 칼로리 계산 후 반환
        if (goal == DIET) {
            return maintenanceCalories * 0.85;
        } else if (goal == MUSCLE) {
            return maintenanceCalories * 1.2;
        } else {
            return maintenanceCalories;
        }
    }

    /**
     * @param bodyComposition
     * @return 기초 대사량 (kcal)
     */
    private double calculateBMR(BodyComposition bodyComposition) {
        double weight = bodyComposition.getWeight();

        if (this.sex == MALE) {
            return 66.5 + (13.75 * weight) + (5 * height) - (6.78 * age);
        } else if (this.sex == FEMALE) {
            return 655.1 + (9.56 * weight) + (1.85 * height) - (4.86 * age);
        } else {
            throw new IllegalStateException("오류 발생\n" +
                    "발생위치:Member.calculateBMR()" +
                    "발생원인: 기초대사량 계산시 필요한 성별이 지정되지 않습니다.");
        }
    }

    private void setObesityBySex(BodyComposition bodyComposition) throws IllegalStateException {
        if (sex == MALE) {
            setObesity(bodyComposition, 10, 21);
        } else if (sex == FEMALE) {
            setObesity(bodyComposition, 17, 30);
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
    public void updateAchieveBodyGoal(AchieveBodyGoal achieveBodyGoal) {
        this.achieveBodyGoal = achieveBodyGoal;
    }

    public void updatePassword(String password) {
        this.personalInformation.updatePassword(password);
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.personalInformation.updatePhoneNumber(phoneNumber);
    }
}
