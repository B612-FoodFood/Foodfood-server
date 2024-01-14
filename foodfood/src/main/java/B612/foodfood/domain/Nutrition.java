package B612.foodfood.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.*;

@Embeddable
@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Nutrition {

    /** 음식의 영양 데이터 **/
    private double servingWeight;
    private double calories;
    private double carbonHydrate;
    private double protein;
    private double fat;
    private double sugar;
    private double natrium;
    private double cholesterol;
    private double saturatedFattyAcid;
    private double transFattyAcid;
}

