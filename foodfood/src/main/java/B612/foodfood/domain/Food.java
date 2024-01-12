package B612.foodfood.domain;

import jakarta.persistence.*;

import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
public class Food {
    @Id
    @GeneratedValue
    @Column(name = "food_id")
    private Long id;

    @Embedded
    private Nutrition nutrition;

    @OneToMany(mappedBy = "food", cascade = ALL)
    private List<MealFood> mealFoods;
}
