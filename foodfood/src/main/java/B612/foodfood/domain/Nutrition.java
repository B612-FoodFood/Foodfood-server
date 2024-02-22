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
    private double servingWeight;  // 1회제공량 (g, 액체의 경우 ml)
    private double calories;  // 열량 (kcal)
    private double carbonHydrate;  // 탄수화물 (g)
    private double protein;  // 단백질 (g)
    private double fat;  // 지방 (g)
    private Double sugar;  // 당류 (g)
    private Double natrium;  // 나트륨 (mg)
    private Double cholesterol; // 콜레스테롤 (mg)
    private Double saturatedFattyAcid; // 포화지방산 (g)
    private Double transFattyAcid;  // 트랜스지방산 (g)

    public Nutrition(double servingWeight, double calories, double carbonHydrate, double protein, double fat) {
        this.servingWeight = servingWeight;
        this.calories = calories;
        this.carbonHydrate = carbonHydrate;
        this.protein = protein;
        this.fat = fat;

        sugar = null;
        natrium = null;
        cholesterol = null;
        cholesterol = null;
        transFattyAcid = null;
    }
}

