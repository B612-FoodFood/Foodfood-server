package B612.foodfood.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

@Embeddable
public class Nutrition {
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
