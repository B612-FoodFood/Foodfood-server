package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
public class MemberAllergy {
    @Id
    @GeneratedValue
    @Column(name = "member_allergy_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    @Setter(PROTECTED)
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "allergy_id")
    @Setter(PROTECTED)
    private Allergy allergy;

    static MemberAllergy createMemberAllergy(Allergy allergy) {
        MemberAllergy memberAllergy = new MemberAllergy();
        allergy.addMemberAllergy(memberAllergy);
        return memberAllergy;
    }
}
