package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static B612.foodfood.domain.MealType.*;
import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.*;

@Entity
@Getter
@ToString(exclude = {"member"})
public class Meal {
    @Id
    @GeneratedValue
    @Column(name = "meal_id")
    private Long id;

    @Column(columnDefinition = "DATE")
    private LocalDate date;  // 식사 기록 날짜

    @Embedded
    private ConsumedNutrients nutritionPerDay;  // 하룻동안 섭취한 영양소 정보

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    @Setter(value = PROTECTED)
    private Member member;

    @OneToMany(mappedBy = "meal", cascade = ALL)
    private List<MealFood> breakFast = new ArrayList<>();

    @OneToMany(mappedBy = "meal", cascade = ALL)
    private List<MealFood> lunch = new ArrayList<>();

    @OneToMany(mappedBy = "meal", cascade = ALL)
    private List<MealFood> dinner = new ArrayList<>();

    @OneToMany(mappedBy = "meal", cascade = ALL)
    private List<MealFood> snack = new ArrayList<>();

    public Meal() {
        this.nutritionPerDay = new ConsumedNutrients();
        this.date = LocalDate.now();
    }

    public Meal(LocalDate date) {
        this.nutritionPerDay = new ConsumedNutrients();
        this.date = date;
    }

    /**
     * getter
     */
    public List<MealFood> getBreakFast() {
        List<MealFood> breakFast = new ArrayList<>();
        for (MealFood mealFood : this.breakFast) {
            if (mealFood.getMealType().equals(BREAKFAST)) {
                breakFast.add(mealFood);
            }
        }

        return breakFast;
    }
    public List<MealFood> getLunch() {
        List<MealFood> lunch = new ArrayList<>();
        for (MealFood mealFood : this.lunch) {
            if (mealFood.getMealType().equals(LUNCH)) {
                lunch.add(mealFood);
            }
        }

        return lunch;
    }
    public List<MealFood> getDinner() {
        List<MealFood> dinner = new ArrayList<>();
        for (MealFood mealFood : this.dinner) {
            if (mealFood.getMealType().equals(DINNER)) {
                dinner.add(mealFood);
            }
        }

        return dinner;
    }
    public List<MealFood> getSnack() {
        List<MealFood> snack = new ArrayList<>();
        for (MealFood mealFood : this.snack) {
            if (mealFood.getMealType().equals(SNACK)) {
                snack.add(mealFood);
            }
        }

        return snack;
    }

    /**
     * 연관관계 편의 메서드
     */
    public void addBreakFast(Food food, double weight) {
        MealFood breakFastFood = MealFood.createMealFood(food, weight);
        breakFastFood.setMealType(BREAKFAST);
        breakFastFood.setMeal(this);
        this.breakFast.add(breakFastFood);

        sumFoodNutritionData(breakFastFood);
    }

    public void addLunch(Food food, double weight) {
        MealFood lunchFood = MealFood.createMealFood(food, weight);
        lunchFood.setMealType(LUNCH);
        lunchFood.setMeal(this);
        this.lunch.add(lunchFood);

        sumFoodNutritionData(lunchFood);
    }

    public void addDinner(Food food, double weight) {
        MealFood dinnerFood = MealFood.createMealFood(food, weight);
        dinnerFood.setMealType(DINNER);
        dinnerFood.setMeal(this);
        this.dinner.add(dinnerFood);

        sumFoodNutritionData(dinnerFood);
    }

    public void addSnack(Food food, double weight) {
        MealFood snackFood = MealFood.createMealFood(food, weight);
        snackFood.setMealType(SNACK);
        snackFood.setMeal(this);
        this.snack.add(snackFood);

        sumFoodNutritionData(snackFood);
    }

    /**
     * 비즈니스 로직
     */

    // 먹은 음식에 대한 영양소를 계산해서 하룻동안 섭취한 영양소에 더해줌
    private void sumFoodNutritionData(MealFood mealFood) {
        double gram = mealFood.getFoodWeight();  // 해당 음식을 섭취한 그램수
        Nutrition foodNutrition = mealFood.getFood().getNutrition(); // 해당 음식의 영양 정보

        double gramPerServeWeight = gram / foodNutrition.getServingWeight();  // (섭취한 무게 / 일 회 제공량) => 섭취한 무게에 따른 영양소를 계산하기 위함

        // 회원의 일일 섭취 영양소 = 회원의 기존 일일 섭취 영양소 + 새롭게 섭취한 영양소(= 음식의 영양소 * 섭취한 무게 / 일 회 제공량)
        double calories = nutritionPerDay.getCalories() + foodNutrition.getCalories() * gramPerServeWeight;
        double carbonHydrate = nutritionPerDay.getCarbonHydrate() + foodNutrition.getCarbonHydrate() * gramPerServeWeight;
        double protein = nutritionPerDay.getProtein() + foodNutrition.getProtein() * gramPerServeWeight;
        double fat = nutritionPerDay.getFat() + foodNutrition.getFat() * gramPerServeWeight;
        double sugar = nutritionPerDay.getSugar() + foodNutrition.getSugar() * gramPerServeWeight;
        double natrium = nutritionPerDay.getNatrium() + foodNutrition.getNatrium() * gramPerServeWeight;
        double cholesterol = nutritionPerDay.getCholesterol() + foodNutrition.getCholesterol() * gramPerServeWeight;
        double saturatedFattyAcid = nutritionPerDay.getSaturatedFattyAcid() + foodNutrition.getSaturatedFattyAcid() * gramPerServeWeight;
        double transFattyAcid = nutritionPerDay.getTransFattyAcid() + foodNutrition.getTransFattyAcid() * gramPerServeWeight;

        this.nutritionPerDay = new ConsumedNutrients(calories, carbonHydrate, protein, fat, sugar, natrium, cholesterol, saturatedFattyAcid, transFattyAcid);
    }
}
