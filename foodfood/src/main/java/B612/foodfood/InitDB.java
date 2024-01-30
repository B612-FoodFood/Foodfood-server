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

import static B612.foodfood.domain.AccountType.USER;
import static B612.foodfood.domain.Activity.LOT;
import static B612.foodfood.domain.BodyGoal.MUSCLE;
import static B612.foodfood.domain.Category.*;
import static B612.foodfood.domain.Sex.MALE;

//@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct  // 빈으로 등록될 시 자동으로 실행됨
    public void init() throws NoDataExistException, DataSaveException {
        initService.mealInit();
        initService.foodInit();
        initService.diseaseInit();
        initService.drugInit();
        initService.memberInit();
    }

//    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final MemberService memberService;
        private final MealService mealService;
        private final FoodService foodService;
        private final DiseaseService diseaseService;
        private final DrugService drugService;
        private final IngredientService ingredientService;
        // 필요시 더 추가할 것

        void mealInit() throws NoDataExistException, DataSaveException {
            double height = 172;
            LogIn logIn = new LogIn("id", "password");
            PersonalInformation personalInformation = new PersonalInformation(logIn, "010-1234-5678");
            AchieveBodyGoal bodyGoal = new AchieveBodyGoal(35, 11);
            LocalDate birthDate = LocalDate.of(2000, 5, 4);
            Member member = new Member("member", MALE, birthDate, personalInformation, height, LOT, MUSCLE, bodyGoal, USER);
            Long memberId = memberService.join(member);

            Food food = new Food("food3", new Nutrition(1, 1, 1, 1, 1, 1, 1, 1, 1, 1), 견과류및종실류);
            foodService.save(food);

            Meal meal = new Meal();
            meal.addBreakFast(food,250);
            meal.addBreakFast(food,250);
            meal.addBreakFast(food,250);
            meal.addLunch(food,1000);
            member.addMeal(meal);

            mealService.save(meal);

        }

        void foodInit() throws DataSaveException {
            Food food1 = new Food("food1", new Nutrition(1, 1, 1, 1, 1, 1, 1, 1, 1, 1), 감자및전분류);

            Ingredient ingredient1 = new Ingredient("ingredient1", 곡류);
            Ingredient ingredient2 = new Ingredient("ingredient2",곡류);
            ingredientService.save(ingredient1);
            ingredientService.save(ingredient2);

            food1.addIngredient(ingredient1);
            food1.addIngredient(ingredient2);

            foodService.save(food1);
            foodService.save(new Food("food2", new Nutrition(1, 1, 1, 1, 1, 1, 1, 1, 1, 1),곡류));
        }

        void diseaseInit() throws DataSaveException {
            diseaseService.save(new Disease("disease1"));
            diseaseService.save(new Disease("disease2"));
        }

        void drugInit() throws DataSaveException {
            drugService.save(new Drug("drug1"));
        }

        public void memberInit() {
            double height = 172;
            LogIn logIn = new LogIn("username", "password");
            PersonalInformation personalInformation = new PersonalInformation(logIn, "010-1234-5678");
            AchieveBodyGoal bodyGoal = new AchieveBodyGoal(100, 100);
            LocalDate birthDate = LocalDate.of(1592, 1, 1);
            Member member = new Member("joonsik", MALE, birthDate, personalInformation, height, LOT, MUSCLE, bodyGoal, USER);

            Ingredient ingredient3 = new Ingredient("ingredient3",곡류);
            Ingredient ingredient4 = new Ingredient("ingredient4",곡류);
            Ingredient ingredient5 = new Ingredient("ingredient5",곡류);
            ingredientService.save(ingredient3);
            ingredientService.save(ingredient4);
            ingredientService.save(ingredient5);

            member.addAvoidIngredient(ingredient3);
            member.addAvoidIngredient(ingredient4);
            Long memberId = memberService.join(member);

            memberService.updateAddAvoidIngredient(memberId,ingredient5);
        }
    }
}

