package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.*;

@Entity
@Getter
public class Meal {
    @Id
    @GeneratedValue
    @Column(name = "meal_id")
    private Long id;

    private LocalDateTime date;  // 식사 기록 날짜

    @OneToMany(mappedBy = "meal",cascade = ALL)
    private List<MealFood> breakFast;

    @OneToMany(mappedBy = "meal",cascade = ALL)
    private List<MealFood> lunch;

    @OneToMany(mappedBy = "meal",cascade = ALL)
    private List<MealFood> dinner;

    @OneToMany(mappedBy = "meal",cascade = ALL)
    private List<MealFood> snack;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
