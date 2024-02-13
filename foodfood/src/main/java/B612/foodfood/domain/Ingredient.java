package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.EnumType.*;

@Entity
@NoArgsConstructor
@ToString(exclude = {"avoidIngredients", "foodIngredients"})
@Getter
public class Ingredient {
    @Id
    @GeneratedValue
    @Column(name = "ingredient_id")
    private Long id;

    @Column(unique = true)
    private String name;

    @Enumerated(STRING)
    private Category category;

    public Ingredient(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    @OneToMany(mappedBy = "ingredient")
    private List<AvoidIngredient> avoidIngredients = new ArrayList<>();

    @OneToMany(mappedBy = "ingredient")
    private List<FoodIngredient> foodIngredients = new ArrayList<>();

    /**
     * 연관관계 편의 메서드
     */
    protected void addAvoidIngredient(AvoidIngredient avoidIngredient) {
        avoidIngredient.setIngredient(this);
        avoidIngredients.add(avoidIngredient);
    }

    protected void addFoodIngredient(FoodIngredient foodIngredient) {
        foodIngredient.setIngredient(this);
        foodIngredients.add(foodIngredient);
    }
}
