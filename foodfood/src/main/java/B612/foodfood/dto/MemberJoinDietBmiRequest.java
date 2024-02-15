package B612.foodfood.dto;

import B612.foodfood.domain.Activity;
import B612.foodfood.domain.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MemberJoinDietBmiRequest {
    private double height; // 단위 m
    private double weight; // 단위 kg
    private Activity activity; // 활동량
    private Sex sex;
}
