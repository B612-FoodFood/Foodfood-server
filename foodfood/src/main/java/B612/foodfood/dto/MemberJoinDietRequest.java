package B612.foodfood.dto;

import B612.foodfood.domain.Sex;
import lombok.Data;

@Data
public class MemberJoinDietRequest {
    private String name;
    private Sex sex;
    private int height;
}
