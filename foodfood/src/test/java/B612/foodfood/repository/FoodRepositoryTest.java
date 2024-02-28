package B612.foodfood.repository;

import B612.foodfood.domain.Category;
import B612.foodfood.domain.Food;
import B612.foodfood.domain.Nutrition;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class FoodRepositoryTest {

    @Autowired
    FoodRepository foodRepository;

    @BeforeEach
    void setUp() {
        Nutrition nutrition1 = new Nutrition(1, 10, 1, 10, 1, 1D, 1D, 1D, 1D, 1D);
        Nutrition nutrition2 = new Nutrition(1, 10, 10, 1, 10, 1D, 1D, 1D, 1D, 1D);
        Nutrition nutrition3 = new Nutrition(1, 1, 10, 10, 1, 1D, 1D, 1D, 1D, 1D);
        Food fo1 = new Food("food1", nutrition1, Category.견과류및종실류);
        Food fo2 = new Food("food2", nutrition2, Category.견과류및종실류);
        Food fo3 = new Food("food1", nutrition3, Category.견과류및종실류);

        foodRepository.save(fo1);
        foodRepository.save(fo2);
        foodRepository.save(fo3);

        System.out.println("fo1 = " + fo1);
        System.out.println("fo1 = " + fo2);
        System.out.println("fo1 = " + fo3);
    }

    @Test
    @DisplayName("ID로 조회")
    public void test() throws Exception{
        //given
        Nutrition nutrition = new Nutrition(1, 1, 1, 1, 1, 1D, 1D, 1D, 1D, 1D);
        Food fo = new Food("food1", nutrition, Category.견과류및종실류);
        foodRepository.save(fo);

        //when
        Optional<Food> findFood = foodRepository.findById(fo.getId());

        //then
        System.out.println("findFood = " + findFood);
        assertEquals(findFood.get().getName(),"food1");
    }

    @Test
    @DisplayName("이름으로 조회")
    public void test2() throws Exception{
        //given

        //when
        //Optional<Food> food1 = foodRepository.findByName("food1"); // 동일한 이름의 객체가 두개라 에러발생
        Optional<Food> food2 = foodRepository.findByName("food2");

        //then
        assertEquals(food2.get().getName(), "food2");
    }

    @Test
    @DisplayName("영양분으로 조회")
    public void test3() throws Exception{
        //given
        //when
        List<Food> thatHasMoreCalories = foodRepository.findThatHasMoreCalories(5);
        List<Food> thatHasLessCalories = foodRepository.findThatHasLessCalories(5);

        //then
        for (Food thatHasMoreCarlory : thatHasMoreCalories) {
            System.out.println("thatHasMoreCarlory = " + thatHasMoreCarlory);
        }
        for (Food thatHasLessCarlory : thatHasLessCalories) {
            System.out.println("thatHasLessCarlory = " + thatHasLessCarlory);
        }

        assertEquals(thatHasMoreCalories.size(),2);
        assertEquals(thatHasLessCalories.size(),1);
    }
}