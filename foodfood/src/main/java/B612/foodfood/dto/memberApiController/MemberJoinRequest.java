package B612.foodfood.dto.memberApiController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinRequest {  // ㅋㅋ 인자수 보소
    private String name;
    // Sex
    private String sex;
    // LocalDate
    private String birthDate;
    // PersonalInformation
    private String username;
    private String password;
    private String phoneNumber;

    private double height;
    // BodyComposition
    private Double weight;
    private Double muscle;
    private Double fat;
    // Activity
    private String activity;
    // Goal
    private String goal;
    // AchieveBodyGoal  # json 데이터를 받을 때, 목표를 설정하지 않은 경우 (현재 골격근량과 체지방량 or 평균 골격근량, 체지방량)으로 설정함. 추후 구현
    private double achieveWeight;
    private double achieveMuscle;
    private double achieveBodyFat;
    // AccountType
    private String accountType;

    // AvoidIngredients
    private List<String> avoidIngredients = new ArrayList<>();

    // Disease
    private List<String> diseases;

    // Drug
    private List<String> drugs;
}

