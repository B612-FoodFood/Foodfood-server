package B612.foodfood.domain;

import jakarta.persistence.*;

@Entity
public class MemberDisease {

    @Id
    @GeneratedValue
    @Column(name = "member_disease_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disease_id")
    private Disease disease;
}
