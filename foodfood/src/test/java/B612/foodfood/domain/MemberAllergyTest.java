package B612.foodfood.domain;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class MemberAllergyTest {
    @Test
    public void test() throws Exception {
        //given
        Member member = new Member("member", new Date(1,1,1), 172, Sex.MALE, Activity.LOT, AccountType.USER);
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