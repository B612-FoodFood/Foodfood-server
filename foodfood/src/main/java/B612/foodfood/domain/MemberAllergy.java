package B612.foodfood.domain;

import jakarta.persistence.*;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;

@Entity
public class MemberAllergy {
    @Id
    @GeneratedValue
    @Column(name = "member_allergy_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "allergy_id")
    private Allergy allergy;
}
