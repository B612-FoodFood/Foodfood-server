package B612.foodfood.domain;

import B612.foodfood.exception.AppException;
import B612.foodfood.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.*;

@Entity
@Getter
@ToString(exclude = "member")
@NoArgsConstructor(access = PROTECTED)
public class BodyComposition {
    @Id
    @GeneratedValue
    @Column(name = "body_composition_id")
    private Long id;

    private double weight; // 체중: 단위(kg)
    @Setter(value = PROTECTED)
    private Double muscle; // 골격근량: 단위(kg)

    @Setter(value = PROTECTED)  // member의 obestiy 계산을 위하여 bodyFat만 별도로 setter를 허용함.
    private Double bodyFat; // 체지방률: 단위(%)

    @Column(columnDefinition = "DATE")
    private LocalDate date;  // 기록 시간

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    @Setter(value = PROTECTED)
    private Member member;

    public BodyComposition(double weight, Double muscle, Double bodyFat) {
        if (weight <= 0 || muscle <= 0 || bodyFat <= 0 || bodyFat > 100) {
            throw new AppException(ErrorCode.INVALID_VALUE_ASSIGNMENT, "잘못된 값이 입력되었습니다");
        }
        this.weight = weight;
        this.muscle = muscle;
        this.bodyFat = bodyFat;
        date = LocalDate.now();
    }
}
