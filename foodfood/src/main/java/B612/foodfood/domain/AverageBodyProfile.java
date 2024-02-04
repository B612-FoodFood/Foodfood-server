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
    private int age;
    @Enumerated(STRING)
    private Sex sex;

    // 신체 정보
    private double weight;
    private double height;
    private double muscle;
    private double fat;

    public AverageBodyProfile(int age, Sex sex, double weight, double height, double muscle,double fat) {
        this.age = age;
        this.sex = sex;
        this.weight = weight;
        this.height = height;
        this.muscle = muscle;
        this.fat = fat;
    }
}
