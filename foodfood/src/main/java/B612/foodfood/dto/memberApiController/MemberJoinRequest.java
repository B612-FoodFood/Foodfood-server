package B612.foodfood.dto.memberApiController;

import B612.foodfood.domain.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinRequest {  // ㅋㅋ 인자수 보소
    private String username;
    private String password;
    private String phoneNumber;

    // AccountType
    private AccountType accountType;

    // AvoidIngredients
    private List<String> avoidIngredients = new ArrayList<>();

    // Disease
    private List<String> diseases;

    // Drug
    private List<String> drugs;
}

