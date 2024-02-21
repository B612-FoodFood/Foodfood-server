package B612.foodfood.dto.memberApiController;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberMealSelectRequest {
    private LocalDate date;
    private String name;
    private double foodWeight;
}
