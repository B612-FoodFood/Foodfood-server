package B612.foodfood.service;

import B612.foodfood.domain.Food;
import B612.foodfood.exception.AppException;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class FoodServiceTest {

    @Autowired
    FoodService foodService;

    @Test
    @DisplayName("두 칼로리 사이의 음식 조회")
    public void test() throws Exception {
        //given
        List<Food> foodBetweenTwoCalories = foodService.findFoodBetweenTwoCalories(100, 150, 20);

        //when
        int cnt = 0;
        for (Food foodBetweenTwoCalory : foodBetweenTwoCalories) {
            cnt++;
            System.out.println(cnt + ": food = " + foodBetweenTwoCalory);
        }

        Assert.assertThrows(AppException.class, () -> foodService.findFoodBetweenTwoCalories(100, 0, 1));
    }

    @Test
    @DisplayName("두 탄수화물 사이의 음식 조회")
    public void test2() throws Exception {
        //given
        List<Food> foodBetweenTwoCarbonHydrate = foodService.findFoodBetweenTwoCarbonHydrate(10, 20, 10);
        //when
        int cnt = 0;
        for (Food food : foodBetweenTwoCarbonHydrate) {
            cnt++;
            System.out.println(cnt + ":food = " + food);
        }

        Assert.assertThrows(AppException.class, () -> foodService.findFoodBetweenTwoCarbonHydrate(100, 0, 1));

    }

    @Test
    @DisplayName("두 단백질 사이의 음식 조회")
    public void test3() throws Exception {
        //given
        List<Food> foodBetweenTwoProtein = foodService.findFoodBetweenTwoProtein(1, 5, 10);
        //when
        int cnt = 0;
        for (Food food : foodBetweenTwoProtein) {
            cnt++;
            System.out.println(cnt + ":food = " + food);
        }
        Assert.assertThrows(AppException.class, () -> foodService.findFoodBetweenTwoProtein(100, 0, 1));
    }

    @Test
    @DisplayName("두 지방 사이의 음식 조회")
    public void test4() throws Exception {
        //given
        List<Food> foodBetweenTwoFat = foodService.findFoodBetweenTwoFat(10, 50, 10);
        //when
        int cnt = 0;
        for (Food food : foodBetweenTwoFat) {
            cnt++;
            System.out.println(cnt + ":food = " + food);
        }
        Assert.assertThrows(AppException.class, () -> foodService.findFoodBetweenTwoFat(100, 0, 1));
    }

    @Test
    public void test5() throws Exception {
        //given
        List<Food> foodBetweenTwoNutrition =
                foodService.findFoodBetweenTwoNutrition(100, 500, 10, 50, 0, 100, 10, 100, 10);
        //when
        int cnt = 0;
        for (Food food : foodBetweenTwoNutrition) {
            cnt++;
            System.out.println(cnt + ":food = " + food);
        }

        //then
        Assert.assertThrows(AppException.class, () -> foodService.findFoodBetweenTwoNutrition(100, 0, 100, 0, 100, 0, 100, 0, 1));

    }
}