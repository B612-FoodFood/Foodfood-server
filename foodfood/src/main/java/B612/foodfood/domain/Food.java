package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.*;

@Entity
@Getter
@ToString(exclude = "mealFoods")
@NoArgsConstructor(access = PROTECTED)
public class Food {
    @Id
    @GeneratedValue
    @Column(name = "food_id")
    private Long id;

    private String name;

    @Embedded
    private Nutrition nutrition;

    @Enumerated(STRING)
    private Category category;

    public Food(String name, Nutrition nutrition, Category category) {
        this.name = name;
        this.nutrition = nutrition;
        this.category = category;
    }

    @OneToMany(mappedBy = "food", cascade = ALL)
    private List<MealFood> mealFoods = new ArrayList();

    @OneToMany(mappedBy = "food", cascade = ALL)
    private List<FoodIngredient> foodIngredients = new ArrayList<>();

    /**
     * 연관관계 편의 메서드
     */
    protected void addMealFood(MealFood mealFood) {
        mealFood.setFood(this);
        mealFoods.add(mealFood);
    }

    public FoodIngredient addIngredient(Ingredient ingredient) {
        FoodIngredient foodIngredient = FoodIngredient.createFoodIngredient(ingredient);
        foodIngredient.setFood(this);
        foodIngredients.add(foodIngredient);

        return foodIngredient;
    }
}
