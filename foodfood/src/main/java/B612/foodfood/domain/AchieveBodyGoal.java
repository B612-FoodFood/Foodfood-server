package B612.foodfood.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

import static lombok.AccessLevel.*;

@Embeddable
@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class AchieveBodyGoal {
    // 목표 골격근량, 체지방률
    private double achieveMuscle;
    private double achieveBodyFat;
}
