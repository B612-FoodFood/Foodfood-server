package B612.foodfood.domain.dto;

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
    // Address
    private String city;
    private String street;
    private String zipCode;

    private double height;
    // Activity
    private String activity;
    // Goal
    private String goal;
    // AchieveBodyGoal
    private double achieveMuscle;
    private double achieveBodyFat;
    // AccountType
    private String accountType;
    // BodyComposition
    private Double weight;
    private Double muscle;
    private Double fat;

    // AvoidFoods
    private List<String> avoidFoods = new ArrayList<>();

    // Disease
    private List<String> disease;

    // Drug
    private List<String> drug;
}

