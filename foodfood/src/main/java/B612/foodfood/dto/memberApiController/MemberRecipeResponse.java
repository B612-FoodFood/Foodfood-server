package B612.foodfood.dto.memberApiController;

import B612.foodfood.domain.Food;
import B612.foodfood.domain.Nutrition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRecipeResponse {

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

    public void addBreakFasts(List<Food> foods) {
        for (Food food : foods) {
            String name = food.getName();
            Nutrition nutrition = food.getNutrition();

            double servingWeight = nutrition.getServingWeight();
            double calories = nutrition.getCalories();
            double carbonHydrate = nutrition.getCarbonHydrate();
            double protein = nutrition.getProtein();
            double fat = nutrition.getFat();

            this.addBreakFast(name, servingWeight, calories, carbonHydrate, protein, fat);
        }
    }

    public void addLunches(List<Food> foods) {
        for (Food food : foods) {
            String name = food.getName();
            Nutrition nutrition = food.getNutrition();

            double servingWeight = nutrition.getServingWeight();
            double calories = nutrition.getCalories();
            double carbonHydrate = nutrition.getCarbonHydrate();
            double protein = nutrition.getProtein();
            double fat = nutrition.getFat();

            this.addLunch(name, servingWeight, calories, carbonHydrate, protein, fat);
        }
    }

    public void addDinners(List<Food> foods) {
        for (Food food : foods) {
            String name = food.getName();
            Nutrition nutrition = food.getNutrition();

            double servingWeight = nutrition.getServingWeight();
            double calories = nutrition.getCalories();
            double carbonHydrate = nutrition.getCarbonHydrate();
            double protein = nutrition.getProtein();
            double fat = nutrition.getFat();

            this.addDinner(name, servingWeight, calories, carbonHydrate, protein, fat);
        }
    }

    public void addBreakFast(String name, double servingWeight, double calories, double carbonHydrate, double protein, double fat) {
        FoodDto foodDto = new FoodDto(name, servingWeight, calories, carbonHydrate, protein, fat);
        breakFast.add(foodDto);
    }

    public void addLunch(String name, double servingWeight, double calories, double carbonHydrate, double protein, double fat) {
        FoodDto foodDto = new FoodDto(name, servingWeight, calories, carbonHydrate, protein, fat);
        lunch.add(foodDto);
    }

    public void addDinner(String name, double servingWeight, double calories, double carbonHydrate, double protein, double fat) {
        FoodDto foodDto = new FoodDto(name, servingWeight, calories, carbonHydrate, protein, fat);
        dinner.add(foodDto);
    }
}
