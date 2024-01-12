package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
public class BodyComposition {
    @Id
    @GeneratedValue
    @Column(name = "body_composition_id")
    private Long id;

    private double weight; // 체중: 단위(kg)
    private double muscle; // 골격근량: 단위(kg)
    private double bodyFat; // 체지방률: 단위(%)
    private LocalDateTime date;  // 기록 시간

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
