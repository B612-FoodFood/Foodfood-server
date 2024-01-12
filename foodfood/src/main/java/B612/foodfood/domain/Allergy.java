package B612.foodfood.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@Getter
public class Allergy {
    @Id
    @GeneratedValue
    @JoinColumn(name = "allergy_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "allergy", cascade = ALL)
    private List<MemberAllergy> memberAllergies = new ArrayList<>();
    // 해당 알러지 대해 피해야할 식품들 구성 필요... 로직 구성
}
