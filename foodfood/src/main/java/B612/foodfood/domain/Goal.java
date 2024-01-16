package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
public class Goal {
    @Id
    @GeneratedValue
    @Column(name = "goal_id")
    private Long id;

    @Enumerated
    private BodyGoal bodyGoal;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    @Setter(PROTECTED)
    private Member member;
}
