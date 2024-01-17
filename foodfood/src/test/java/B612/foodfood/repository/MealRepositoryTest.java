package B612.foodfood.repository;

import B612.foodfood.domain.Meal;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class MealRepositoryTest {
    @Autowired
    MealRepository mealRepository;

    @Test
    public void test1() throws Exception{
        //given
        Meal meal = new Meal();
        mealRepository.save(meal);

        LocalDate date1 = LocalDate.of(2024, 01, 17);
        //when
        Optional<Meal> byDate = mealRepository.findByDate(date1);

        //then
        if (byDate.isEmpty()) {
            fail();
        }
        System.out.println("byDate = " + byDate.get());
    }
}