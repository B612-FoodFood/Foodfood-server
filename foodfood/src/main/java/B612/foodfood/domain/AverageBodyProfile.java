package B612.foodfood.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import static jakarta.persistence.EnumType.*;
import static lombok.AccessLevel.*;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class AverageBodyProfile {
    @Id
    @GeneratedValue
    @Column(name = "average_body_profile_id")
    private Long id;

    // 구분자
    @Enumerated(STRING)
    private Sex sex;
    private double height;

    // 신체 정보
    private double weight;
    private double muscle;
    private double fat;

    public AverageBodyProfile(Sex sex, double height, double weight,  double muscle, double fat) {
        this.sex = sex;
        this.weight = weight;
        this.height = height;
        this.muscle = muscle;
        this.fat = fat;
    }
}
