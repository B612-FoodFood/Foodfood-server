package B612.foodfood;

import B612.foodfood.domain.*;

import B612.foodfood.service.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static B612.foodfood.domain.AccountType.ADMIN;
import static B612.foodfood.domain.Activity.*;
import static B612.foodfood.domain.BodyGoal.*;
import static B612.foodfood.domain.Sex.MALE;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct  // 빈으로 등록될 시 자동으로 실행됨
    public void init() {
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

