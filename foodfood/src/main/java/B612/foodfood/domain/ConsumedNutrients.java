package B612.foodfood.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@ToString
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

    public ConsumedNutrients() {
        calories = 0;
        carbonHydrate = 0;
        protein = 0;
        fat = 0;
        sugar = 0;
        natrium = 0;
        cholesterol = 0;
        saturatedFattyAcid = 0;
        transFattyAcid = 0;
    }
}
