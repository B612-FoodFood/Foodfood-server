package B612.foodfood;

import B612.foodfood.domain.*;

import B612.foodfood.service.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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
        private final BodyCompositionService bodyCompositionService;
        // 필요시 더 추가할 것

        void mealInit() {
            Food food1 = foodService.findFoodByName("국밥");
            Food food2 = foodService.findFoodByName("가래떡");
            Food food3 = foodService.findFoodByName("감자볶음");
            Food food4 = foodService.findFoodByName("감자전");
            Food food5 = foodService.findFoodByName("대구포");
            Food food6 = foodService.findFoodByName("라떼");


            Member member = memberService.findMemberByLogInUsername("joonsik@naver.com");
            Meal meal = new Meal();
            member.addMeal(meal);

            meal.addBreakFast(food1, 500);
            meal.addLunch(food2, 250);
            meal.addLunch(food3, 180);
            meal.addDinner(food4, 400);
            meal.addDinner(food5, 100);
            meal.addDinner(food6, 210);

            mealService.save(meal);
        }
        void bodyCompositionInit() {
            Member member = memberService.findMemberByLogInUsername("joonsik@naver.com");
            BodyComposition bodyComposition = new BodyComposition(65, 36D, 11D);
            bodyCompositionService.save(bodyComposition);
            member.addBodyComposition(bodyComposition);
            System.out.println("member = " + member);
        }

    }
}

