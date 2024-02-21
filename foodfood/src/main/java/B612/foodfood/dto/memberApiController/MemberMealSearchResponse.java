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
public class MemberMealSearchResponse {
    List<FoodDto> foods = new ArrayList<>();

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

    public void addFoods(String name, double calories, double carbonHydrate, double protein, double fat) {
        FoodDto foodDto = new FoodDto(name, calories, carbonHydrate, protein, fat);
        foods.add(foodDto);
    }
}
