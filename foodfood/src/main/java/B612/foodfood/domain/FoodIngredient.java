package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class FoodIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_ingredient_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "food_id")
    @Setter(PROTECTED)
    private Food food;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ingredient_id")
    @Setter(PROTECTED)
    private Ingredient ingredient;

    /**
     * 연관관계 편의 메서드
     */
    protected static FoodIngredient createFoodIngredient(Ingredient ingredient) {
        FoodIngredient foodIngredient = new FoodIngredient();
        ingredient.addFoodIngredient(foodIngredient);

        return foodIngredient;
    }
}
