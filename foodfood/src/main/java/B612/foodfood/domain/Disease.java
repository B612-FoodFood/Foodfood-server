package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Disease {
    @Id
    @GeneratedValue
    @Column(name = "disease_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "disease", cascade = ALL)
    private List<MemberDisease> memberDiseases = new ArrayList<>();

    public Disease(String name) {
        this.name = name;
    }

    /**
     * 연관관계 편의 메서드
     */
    protected void addMemberDisease(MemberDisease memberDisease) {
        memberDisease.setDisease(this);
        memberDiseases.add(memberDisease);
    }
    // 해당 질병에 대해 피해야할 식품들 구성 필요...
}
