package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
public class MealFood {
    @Id
    @GeneratedValue
    @Column(name = "meal_food_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "meal_id")
    private Meal meal;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "food_id")
    private Food food;
}
