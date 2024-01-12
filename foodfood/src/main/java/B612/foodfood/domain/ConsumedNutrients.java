package B612.foodfood.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Embeddable
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ConsumedNutrients {
    /**유저가 하루동안 섭취한 영양소**/
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
