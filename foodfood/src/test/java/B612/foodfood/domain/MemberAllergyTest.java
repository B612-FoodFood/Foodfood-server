package B612.foodfood.domain;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static B612.foodfood.domain.AccountType.*;
import static B612.foodfood.domain.Activity.*;
import static B612.foodfood.domain.Sex.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class MemberAllergyTest {
    @Test
    public void test() throws Exception {
        //given
        Address address = new Address("Seoul", "Hongdae", "12345");
        LogIn logIn = new LogIn("id", "password");
        PersonalInformation personalInformation = new PersonalInformation("010-1234-5678", "email@gmail.com", address, logIn);
        AchieveBodyGoal bodyGoal = new AchieveBodyGoal(35, 11);
        Member member = new Member("member", new Date(1, 1, 1), 172, MALE, LOT, USER, bodyGoal, personalInformation);

        //when
        member.addAllergy(new Allergy("allergy1"));
        member.addAllergy(new Allergy("allergy2"));

        //then
        List<MemberAllergy> memberAllergies = member.getMemberAllergies();
        for (MemberAllergy memberAllergy : memberAllergies) {
            System.out.println("memberAllergy = " + memberAllergy);
        }
    }
}