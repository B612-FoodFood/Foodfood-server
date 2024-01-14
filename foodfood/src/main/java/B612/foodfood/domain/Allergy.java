package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@ToString(exclude = "memberAllergies")
@NoArgsConstructor(access = PROTECTED)
public class Allergy {
    @Id
    @GeneratedValue
    @JoinColumn(name = "allergy_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "allergy", cascade = ALL)
    private List<MemberAllergy> memberAllergies = new ArrayList<>();

    public Allergy(String name) {
        this.name = name;
    }

    /**
     * 연관관계 편의 메서드
     **/
    protected void addMemberAllergy(MemberAllergy memberAllergy) {
        memberAllergy.setAllergy(this);
        memberAllergies.add(memberAllergy);
    }

    // 해당 알러지 대해 피해야할 식품들 구성 필요... 로직 구성

}
