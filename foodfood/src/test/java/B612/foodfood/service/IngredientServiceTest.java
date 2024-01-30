package B612.foodfood.service;

import B612.foodfood.domain.Category;
import B612.foodfood.domain.Ingredient;
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
class IngredientServiceTest {
    @Autowired
    IngredientService ingredientService;

    @Test
    public void test1() throws Exception {
        //given
        Ingredient 강낭콩 = ingredientService.findIngredientByName("강낭콩");
        //when
        System.out.println("강낭콩.getId() = " + 강낭콩.getId());
        //then

    }

    @Test
    public void test2() throws Exception {
        //given
        List<Ingredient> allIngredients = ingredientService.findAllIngredients();
        //when
        for (Ingredient allIngredient : allIngredients) {
            System.out.println("Ingredient = " + allIngredient);
        }
        //then

    }

    @Test
    public void test3() throws Exception {
        //given
        List<Ingredient> ingredients = ingredientService.findIngredientByKeyword("가");
        //when
        for (Ingredient ingredient : ingredients) {
            System.out.println("ingredient = " + ingredient);
        }

        //then

    }

    @Test
    public void test4() throws Exception {
        //given
        List<Ingredient> ingredients = ingredientService.findIngredientByCategory(Category.어패류및수산물);
        //when
        for (Ingredient ingredient : ingredients) {
            System.out.println("ingredient = " + ingredient);
        }
        //then

    }
}