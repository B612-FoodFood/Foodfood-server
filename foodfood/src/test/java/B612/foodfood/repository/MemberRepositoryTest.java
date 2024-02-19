package B612.foodfood.repository;

import B612.foodfood.domain.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static B612.foodfood.domain.AccountType.*;
import static B612.foodfood.domain.Activity.*;
import static B612.foodfood.domain.BodyGoal.MUSCLE;
import static B612.foodfood.domain.Sex.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class MemberRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    public void init() {
        double height = 172;
        Address address = new Address("Seoul", "Hongdae", "12345");
        LogIn logIn = new LogIn("id", "password");
        PersonalInformation personalInformation = new PersonalInformation(logIn, "010-1234-5678");
        AchieveBodyGoal bodyGoal = new AchieveBodyGoal(0,35, 11);
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Member member = new Member("member", MALE, birthDate, personalInformation, height, LOT, MUSCLE, bodyGoal, USER);

        memberRepository.save(member);
    }

    @Test
    @DisplayName("로그인 아이디로 멤버 조회")
    public void findTest() throws Exception {
        //given
        //when
        Optional<Member> findMember = memberRepository.findByLogInUsername("id");

        //then
        if (findMember.isEmpty()) {
            fail("there is no member exists");
        }

        Member member = findMember.get();
        System.out.println("Name = " + member.getName());
    }

    @Autowired
    BodyCompositionRepository bcRepository;
    @Test
    public void test3() throws Exception {
        //given
        double height = 172;
//        Address address = new Address("Seoul", "Hongdae", "12345");
        LogIn logIn = new LogIn("id", "password");
        PersonalInformation personalInformation = new PersonalInformation(logIn, "010-1234-5678");
        AchieveBodyGoal bodyGoal = new AchieveBodyGoal(0,35, 11);
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Member member = new Member("member", MALE, birthDate, personalInformation, height, LOT, MUSCLE, bodyGoal, USER);
        memberRepository.save(member);

        BodyComposition bc1 = new BodyComposition(65, 33D, null);
        BodyComposition bc2 = new BodyComposition(65, 33D, null);
        BodyComposition bc3 = new BodyComposition(65, 33D, 10D);
        BodyComposition bc4 = new BodyComposition(65, 33D, 20D);
        BodyComposition bc5 = new BodyComposition(65, 33D, null);
        bcRepository.save(bc1);
        bcRepository.save(bc2);
        bcRepository.save(bc3);
        bcRepository.save(bc4);
        bcRepository.save(bc5);


        //when
        Optional<Member> findMember = memberRepository.findById(member.getId());
        Member member1 = findMember.get();

        member1.addBodyComposition(bc1);
        System.out.println("member1.getObesity() = " + member1.getObesity());
        member1.addBodyComposition(bc2);
        System.out.println("member1.getObesity() = " + member1.getObesity());
        member1.addBodyComposition(bc3);
        System.out.println("member1.getObesity() = " + member1.getObesity());
        member1.addBodyComposition(bc4);
        System.out.println("member1.getObesity() = " + member1.getObesity());
        member1.addBodyComposition(bc5);
        System.out.println("member1.getObesity() = " + member1.getObesity());

        //then
        List<BodyComposition> bodyCompositions = member1.getBodyCompositions();
        for (BodyComposition bodyComposition : bodyCompositions) {
            System.out.println("bodyComposition = " + bodyComposition);
        }
    }
}