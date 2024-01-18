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
@ToString
@NoArgsConstructor(access = PROTECTED)
public class AvoidFood {
    @Id
    @GeneratedValue
    @Column(name = "avoid_food_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    @Setter(PROTECTED)
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "food_id")
    @Setter(PROTECTED)
    private Food food;

    public static AvoidFood createAvoidFood(Food food) {
        AvoidFood avoidFood = new AvoidFood();
        food.addAvoidFood(avoidFood);
        return avoidFood;
    }
}
