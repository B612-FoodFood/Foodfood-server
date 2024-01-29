package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.*;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
public class FoodIngredient {
    @Id
    @GeneratedValue
    @Column(name = "food_ingredient_id")
    private Long id;

    @ManyToOne()
    private Ingredient ingredient;
}
