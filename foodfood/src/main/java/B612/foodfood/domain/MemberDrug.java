package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
public class MemberDrug {
    @Id
    @GeneratedValue
    @Column(name = "member_drug_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    @Setter(value = PROTECTED)
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "drug_id")
    @Setter(PROTECTED)
    private Drug drug;

    static MemberDrug createMemberDrug(Drug drug) {
        MemberDrug memberDrug = new MemberDrug();
        drug.addMemberDrug(memberDrug);
        return memberDrug;
    }
}
