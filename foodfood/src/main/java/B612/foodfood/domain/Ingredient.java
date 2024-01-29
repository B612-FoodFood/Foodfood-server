package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class Ingredient {
    @Id
    @GeneratedValue
    @Column(name = "ingredient_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "ingredient", cascade = ALL)
    private List<AvoidIngredient> avoidIngredients = new ArrayList<>();

    @OneToMany(mappedBy = "ingredient", cascade = ALL)
    private List<FoodIngredient> foodIngredients = new ArrayList<>();
}
