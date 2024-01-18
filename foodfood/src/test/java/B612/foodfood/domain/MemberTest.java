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
        Address address = new Address("Seoul", "Hongdae", "12345");
        LogIn logIn = new LogIn("id", "password");
        PersonalInformation personalInformation = new PersonalInformation(logIn, "010-1234-5678", "email@gmail.com", address);
        AchieveBodyGoal bodyGoal = new AchieveBodyGoal(35, 11);
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
}