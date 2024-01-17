package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class MealFood {
    @Id
    @GeneratedValue
    @Column(name = "meal_food_id")
    private Long id;

    private double foodWeight; // 먹은 음식 무게(g)

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "meal_id")
    @Setter(value = PROTECTED)
    private Meal meal;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "food_id")
    @Setter(value = PROTECTED)
    private Food food;

    // MealFood는 Meal의 연관관계 편의 메서드(eg.addBreakFast())로 추가되어야함.
    protected static MealFood createMealFood(Food food, double foodWeight) {
        MealFood mealFood = new MealFood();
        food.addMealFood(mealFood);
        mealFood.foodWeight = foodWeight;

        return mealFood;
    }
}
