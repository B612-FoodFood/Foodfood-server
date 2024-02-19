package B612.foodfood.service;

import B612.foodfood.domain.Food;
import B612.foodfood.domain.Meal;
import B612.foodfood.domain.MealFood;
import B612.foodfood.domain.Member;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class MealServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MealService mealService;
    @Autowired
    FoodService foodService;

    @Test
    public void test() throws Exception {
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

        Long id = mealService.save(meal);
        List<Meal> meals = member.getMeals();
        for (MealFood breakFast: meals.get(meals.size()-1).getBreakFast()) {
            System.out.println("breakFast.getMealType() = " + breakFast.getMealType());
            System.out.println("breakFast = " + breakFast.getFood().getName());
        }
        for (MealFood lunch : meals.get(meals.size()-1).getLunch()) {
            System.out.println("lunch.getMealType() = " + lunch.getMealType());
            System.out.println("lunch.getFood().getName() = " + lunch.getFood().getName());
        }

        Meal meal2 = mealService.findMealById(id);
        List<MealFood> breakFast = meal.getBreakFast();
        for (MealFood mealFood : breakFast) {
            System.out.println("mealFood.getMealType() = " + mealFood.getMealType());
            System.out.println("mealFood = " + mealFood.getFood().getName());
        }
        List<MealFood> lunch = meal.getLunch();
        for (MealFood mealFood : lunch) {
            System.out.println("mealFood.getMealType() = " + mealFood.getMealType());
            System.out.println("mealFood.getFood().getName() = " + mealFood.getFood().getName());
        }

    }

    @Test
    public void test2() throws Exception {
        //given
        Food 국밥 = foodService.findFoodByName("국밥");
        Food 국물떡볶이 = foodService.findFoodByName("국물떡볶이");
        Meal meal = new Meal();
        meal.addBreakFast(국밥, 111);
        meal.addLunch(국물떡볶이, 1231);
        meal.addLunch(국물떡볶이, 12345);
        //when

        List<MealFood> breakFast = meal.getBreakFast();
        for (MealFood mealFood : breakFast) {
            System.out.println("mealFood.getMealType() = " + mealFood.getMealType());
            System.out.println("mealFood.getFood().getName() = " + mealFood.getFood().getName());
        }
        List<MealFood> lunch = meal.getLunch();
        for (MealFood mealFood : lunch) {
            System.out.println("mealFood.getMealType() = " + mealFood.getMealType());
            System.out.println("mealFood.getFood().getName() = " + mealFood.getFood().getName());
        }
        //then

    }
}