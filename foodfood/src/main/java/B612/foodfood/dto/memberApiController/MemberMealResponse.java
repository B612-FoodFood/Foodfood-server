package B612.foodfood.dto.memberApiController;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class MemberMealResponse {

    private List<FoodDto> breakFast = new ArrayList<>();
    private List<FoodDto> lunch = new ArrayList<>();
    private List<FoodDto> dinner = new ArrayList<>();
    private List<FoodDto> snack = new ArrayList<>();

    @AllArgsConstructor
    @Getter
    @Setter
    private class FoodDto {
        private String name;
        private double calories;
        private double carbonHydrate;
        private double protein;
        private double fat;
    }

    public void addBreakFast(String name, double calories, double carbonHydrate, double protein, double fat) {
        FoodDto breakFastFood = new FoodDto(name, calories, carbonHydrate, protein, fat);
        breakFast.add(breakFastFood);
    }

    public void addLunch(String name, double calories, double carbonHydrate, double protein, double fat) {
        FoodDto lunchFood = new FoodDto(name, calories, carbonHydrate, protein, fat);
        lunch.add(lunchFood);
    }

    public void addDinner(String name, double calories, double carbonHydrate, double protein, double fat) {
        FoodDto dinnerFood = new FoodDto(name, calories, carbonHydrate, protein, fat);
        breakFast.add(dinnerFood);
    }

    public void addSnack(String name, double calories, double carbonHydrate, double protein, double fat) {
        FoodDto snackFood = new FoodDto(name, calories, carbonHydrate, protein, fat);
        breakFast.add(snackFood);
    }
}

