package B612.foodfood.domain;

import B612.foodfood.exception.AppException;
import B612.foodfood.exception.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.*;

import static lombok.AccessLevel.*;

@Embeddable
@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
public class AchieveBodyGoal {

    // 목표 체중, 골격근량, 체지방률
    private double achieveWeight;
    private double achieveMuscle;
    private double achieveBodyFat;

    public AchieveBodyGoal(double achieveWeight, double achieveMuscle, double achieveBodyFat) {
        this.achieveWeight = achieveWeight;
        this.achieveMuscle = achieveMuscle;
        this.achieveBodyFat = achieveBodyFat;

        if (achieveWeight <= 0 || achieveMuscle <= 0 || achieveBodyFat <= 0 || achieveBodyFat > 100) {
            throw new AppException(ErrorCode.INVALID_VALUE_ASSIGNMENT, "잘못된 값이 입력되었습니다");
        }
    }
}
