package B612.foodfood;

import B612.foodfood.domain.*;
import B612.foodfood.exception.DataSaveException;
import B612.foodfood.exception.NoDataExistException;
import B612.foodfood.repository.IngredientRepository;
import B612.foodfood.repository.MealRepository;
import B612.foodfood.service.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static B612.foodfood.domain.AccountType.USER;
import static B612.foodfood.domain.Activity.LOT;
import static B612.foodfood.domain.BodyGoal.HEALTH_CARE;
import static B612.foodfood.domain.BodyGoal.MUSCLE;
import static B612.foodfood.domain.Category.*;
import static B612.foodfood.domain.Sex.FEMALE;
import static B612.foodfood.domain.Sex.MALE;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct  // 빈으로 등록될 시 자동으로 실행됨
    public void init() {
        initService.memberInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final MemberService memberService;
        private final MealService mealService;
        private final FoodService foodService;
        private final DiseaseService diseaseService;
        private final DrugService drugService;
        private final IngredientService ingredientService;
        private final AverageBodyProfileService bfService;
        // 필요시 더 추가할 것

        void memberInit() {

        }

    }
}

