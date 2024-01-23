package B612.foodfood.dto;

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
    private String id;
    private String password;
    private String phoneNumber;
    private String email;

    private double height;
    // BodyComposition
    private Double weight;
    private Double muscle;
    private Double fat;
    // Activity
    private String activity;
    // Goal
    private String goal;
    // AchieveBodyGoal
    private double achieveMuscle;
    private double achieveBodyFat;
    // AccountType
    private String accountType;


    // AvoidFoods
    private List<String> avoidFoods = new ArrayList<>();

    // Disease
    private List<String> diseases;

    // Drug
    private List<String> drugs;
}

