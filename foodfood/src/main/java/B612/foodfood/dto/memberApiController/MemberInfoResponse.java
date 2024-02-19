package B612.foodfood.dto.memberApiController;

import B612.foodfood.domain.BodyGoal;
import B612.foodfood.domain.Food;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponse {
    private String name;

    private List<FoodDto> breakFast = new ArrayList<>();
    private List<FoodDto> lunch = new ArrayList<>();
    private List<FoodDto> dinner = new ArrayList<>();
    private List<FoodDto> snack = new ArrayList<>();

    private double recommendedCalories;
    private Double consumedCalories;
    private Double leftCalories;

    private double achieveWeight;
    private double achieveMuscle;
    private double achieveBodyFat;

    private double currentWeight;
    private double currentMuscle;
    private double currentBodyFat;

    List<BodyCompositionDto> bodyChanges = new ArrayList<>();

    private BodyGoal mode;


    @AllArgsConstructor
    @Getter
    @Setter
    private class FoodDto {
        private String name;
        private double servingWeight;
        private double calories;
        private double carbonHydrate;
        private double protein;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private class BodyCompositionDto {
        LocalDate date;
        double weight;
        Double muscle;
        Double bodyFat;
    }

    public void addBodyComposition(LocalDate date, double weight, double muscle, double bodyFat) {
        BodyCompositionDto bodyCompositionDto = new BodyCompositionDto(date, weight, muscle, bodyFat);
        bodyChanges.add(bodyCompositionDto);
    }

    public void addBreakFast(String name, double servingWeight, double calories, double carbonHydrate, double protein) {
        FoodDto foodDto = new FoodDto(name, servingWeight, calories, carbonHydrate, protein);
        breakFast.add(foodDto);
    }

    public void addLunch(String name, double servingWeight, double calories, double carbonHydrate, double protein) {
        FoodDto foodDto = new FoodDto(name, servingWeight, calories, carbonHydrate, protein);
        lunch.add(foodDto);
    }

    public void addDinner(String name, double servingWeight, double calories, double carbonHydrate, double protein) {
        FoodDto foodDto = new FoodDto(name, servingWeight, calories, carbonHydrate, protein);
        dinner.add(foodDto);
    }

    public void addSnack(String name, double servingWeight, double calories, double carbonHydrate, double protein) {
        FoodDto foodDto = new FoodDto(name, servingWeight, calories, carbonHydrate, protein);
        snack.add(foodDto);
    }


}
