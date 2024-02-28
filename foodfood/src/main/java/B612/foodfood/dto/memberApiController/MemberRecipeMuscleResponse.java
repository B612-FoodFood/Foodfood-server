package B612.foodfood.dto.memberApiController;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRecipeMuscleResponse {

    private List<FoodDto> breakFast = new ArrayList<>();
    private List<FoodDto> lunch = new ArrayList<>();
    private List<FoodDto> dinner = new ArrayList<>();

    @AllArgsConstructor
    @Getter
    @Setter
    private class FoodDto {
        private String name;
        private double servingWeight;
        private double calories;
        private double carbonHydrate;
        private double protein;
        private double fat;
    }

    public void addBreakFast(String name, double servingWeight, double calories, double carbonHydrate, double protein, double fat) {
        FoodDto foodDto = new FoodDto(name, servingWeight, calories, carbonHydrate, protein,fat);
        breakFast.add(foodDto);
    }

    public void addLunch(String name, double servingWeight, double calories, double carbonHydrate, double protein,double fat) {
        FoodDto foodDto = new FoodDto(name, servingWeight, calories, carbonHydrate, protein,fat);
        lunch.add(foodDto);
    }

    public void addDinner(String name, double servingWeight, double calories, double carbonHydrate, double protein,double fat) {
        FoodDto foodDto = new FoodDto(name, servingWeight, calories, carbonHydrate, protein,fat);
        dinner.add(foodDto);
    }
}
