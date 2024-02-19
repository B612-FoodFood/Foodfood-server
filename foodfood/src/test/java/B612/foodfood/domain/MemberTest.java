package B612.foodfood.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;

import static B612.foodfood.domain.AccountType.USER;
import static B612.foodfood.domain.Activity.LOT;
import static B612.foodfood.domain.BodyGoal.DIET;
import static B612.foodfood.domain.BodyGoal.MUSCLE;
import static B612.foodfood.domain.Sex.MALE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class MemberTest {

    @Test
    public void test1() throws Exception {
        //given
        double height = 172;
        LogIn logIn = new LogIn("id", "password");
        PersonalInformation personalInformation = new PersonalInformation(logIn, "010-1234-5678");
        AchieveBodyGoal bodyGoal = new AchieveBodyGoal(0, 35, 11);
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Member member = new Member("member", MALE, birthDate, personalInformation, height, LOT, MUSCLE, bodyGoal, USER);

        Meal meal1 = new Meal();
        Meal meal2 = new Meal();

        //when
        member.addMeal(meal1);

        //then
        IllegalStateException illegalStateException =
                assertThrows(IllegalStateException.class, () -> member.addMeal(meal2));
        System.out.println(illegalStateException.getMessage());
    }

    @Test
    public void test2() throws Exception{
        //given
        double height = 172;
        LogIn logIn = new LogIn("id", "password");
        PersonalInformation personalInformation = new PersonalInformation(logIn, "010-1234-5678");
        AchieveBodyGoal bodyGoal = new AchieveBodyGoal(0, 35, 11);
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Member member = new Member("member", MALE, birthDate, personalInformation, height, LOT, MUSCLE, bodyGoal, USER);

        //when
        member.addAvoidIngredient(new Ingredient("ingredient1",Category.견과류및종실류));
        member.addAvoidIngredient(new Ingredient("ingredient2",Category.견과류및종실류));
        //then
        IllegalStateException illegalStateException =
                assertThrows(IllegalStateException.class, () -> member.addAvoidIngredient(new Ingredient("ingredient1",Category.견과류및종실류)));
        System.out.println(illegalStateException.getMessage());
    }

    @Test
    public void test3() throws Exception{
        //given
        double height = 172;
        LogIn logIn = new LogIn("test", "password");
        PersonalInformation personalInformation = new PersonalInformation(logIn, "010-1234-5678");
        AchieveBodyGoal bodyGoal = new AchieveBodyGoal(0,35, 11);
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Member member = new Member("member", MALE, birthDate, personalInformation, height, LOT, DIET, bodyGoal, USER);

        //when
        BodyComposition bodyComposition = new BodyComposition(65, 33D, 12D);
        member.addBodyComposition(bodyComposition);

        //then
        double basalMetabolicRate = member.getBasalMetabolicRate();
        System.out.println("basalMetabolicRate = " + basalMetabolicRate);

        double recommendedCalories = member.getRecommendedCalories();
        System.out.println("recommendedCalories = " + recommendedCalories);
    }
}