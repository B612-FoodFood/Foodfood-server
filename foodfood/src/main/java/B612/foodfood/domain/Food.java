package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Food {
    @Id
    @GeneratedValue
    @Column(name = "food_id")
    private Long id;

    private String name;

    @Embedded
    private Nutrition nutrition;

    @OneToMany(mappedBy = "food", cascade = ALL)
    private List<MealFood> mealFoods;

    public Food(String name, Nutrition nutrition) {
        this.name = name;
        this.nutrition = nutrition;
    }

    /**
     * 연관관계 편의 메서드
     */
    protected void addMealFood(MealFood mealFood) {
        mealFood.setFood(this);
        mealFoods.add(mealFood);
    }
}
