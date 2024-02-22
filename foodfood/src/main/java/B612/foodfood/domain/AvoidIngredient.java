package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class AvoidIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avoid_ingredient_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    @Setter(PROTECTED)
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ingredient_id")
    @Setter(PROTECTED)
    private Ingredient ingredient;

    public static AvoidIngredient createAvoidIngredient(Ingredient ingredient) {
        AvoidIngredient avoidIngredient = new AvoidIngredient();
        ingredient.addAvoidIngredient(avoidIngredient);
        return avoidIngredient;
    }
}
