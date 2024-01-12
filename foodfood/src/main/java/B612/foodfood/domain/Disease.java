package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@Getter
public class Disease {
    @Id
    @GeneratedValue
    @Column(name = "disease_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "disease", cascade = ALL)
    private List<MemberDisease> memberDiseases = new ArrayList<>();


    // 해당 질병에 대해 피해야할 식품들 구성 필요...
}
