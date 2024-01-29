package B612.foodfood.dto;

import B612.foodfood.domain.Food;
import B612.foodfood.domain.Nutrition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Objects;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "음식 데이터")
public class FoodDto {
    @ApiModelProperty(name = "식품명", example = "라떼")
    private String name;

    @ApiModelProperty(name = "1회제공량", example = "285")
    private String servingWeight;

    @ApiModelProperty(name = "에너지(㎉)", example = "124")
    private String calories;

    @ApiModelProperty(name = "탄수화물(g)", example = "-")
    private String carbonHydrate;  // 탄수화물 (g)

    @ApiModelProperty(name = "protein(g)", example = "3")
    private String protein;  // 단백질 (g)

    @ApiModelProperty(name = "지방(g)", example = "-")
    private String fat;  // 지방 (g)

    @ApiModelProperty(name = "당류(g)", example = "4")
    private String sugar;  // 당류 (g)

    @ApiModelProperty(name = "나트륨(mg)", example = "78")
    private String natrium;  // 나트륨 (mg)

    @ApiModelProperty(name = "콜레스테롤(mg)", example = "-")
    private String cholesterol; // 콜레스테롤 (mg)

    @ApiModelProperty(name = "포화 지방산(g)", example = "7.1")
    private String saturatedFattyAcid; // 포화지방산 (g)

    @ApiModelProperty(name = "트랜스 지방산(mg)", example = "-")
    private String transFattyAcid;  // 트랜스지방산 (g)

    @ApiModelProperty(name = "식품상세분류", example = "커피류")
    private String foodType;

    public Food toEntity() {
        Double sw = Objects.equals(servingWeight, "-") ? Double.valueOf(servingWeight) : 0;
        Double ca = Objects.equals(calories, "-") ? Double.valueOf(calories) : 0;
        Double cb = Objects.equals(carbonHydrate, "-") ? Double.valueOf(carbonHydrate) : 0;
        Double pr = Objects.equals(protein, "-") ? Double.valueOf(protein) : 0;
        Double fa = Objects.equals(fat, "-") ? Double.valueOf(fat) : 0;
        Double su = Objects.equals(sugar, "-") ? Double.valueOf(sugar) : 0;
        Double na = Objects.equals(natrium, "-") ? Double.valueOf(natrium) : 0;
        Double ch = Objects.equals(cholesterol, "-") ? Double.valueOf(cholesterol) : 0;
        Double sf = Objects.equals(saturatedFattyAcid, "-") ? Double.valueOf(saturatedFattyAcid) : 0;
        Double tr = Objects.equals(transFattyAcid, "-") ? Double.valueOf(transFattyAcid) : 0;

        Nutrition nutrition = new Nutrition(sw, ca, cb, pr, fa, su, na, ch, sf, tr);
        Food food = new Food(this.name, nutrition);
        setFoodType(foodType);

        return food;
    }
}
