package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private double muscle; // 골격근량: 단위(kg)
    @Setter(value = PROTECTED)
    private Double bodyFat; // 체지방률: 단위(%)
    private LocalDate date;  // 기록 시간

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    @Setter(value = PROTECTED)
    private Member member;

    public BodyComposition(double weight, double muscle, Double bodyFat) {
        this.weight = weight;
        this.muscle = muscle;
        this.bodyFat = bodyFat;
        date = LocalDate.now();
    }


}
