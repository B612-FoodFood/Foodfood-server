package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@ToString
public class Drug {
    @Id
    @GeneratedValue
    @Column(name = "drug_id")
    private Long id;

    @Column(unique = true)
    private String name;

    // 추가할 부분 추가...
    public Drug(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "drug")
    private List<MemberDrug> memberDrugs = new ArrayList<>();

    /**
     * 연관관계 편의 메서드
     **/
    protected void addMemberDrug(MemberDrug memberDrug) {
        memberDrug.setDrug(this);
        memberDrugs.add(memberDrug);
    }
}
