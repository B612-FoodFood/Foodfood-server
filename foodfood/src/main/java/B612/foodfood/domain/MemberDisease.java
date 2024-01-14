package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.*;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
public class MemberDisease {

    @Id
    @GeneratedValue
    @Column(name = "member_disease_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Setter(value = PROTECTED)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disease_id")
    @Setter(value = PROTECTED)
    private Disease disease;

    static MemberDisease createMemberDisease(Disease disease) {
        MemberDisease memberDisease = new MemberDisease();
        disease.addMemberDisease(memberDisease);
        return memberDisease;
    }
}
