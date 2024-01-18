package B612.foodfood.service;

import B612.foodfood.domain.*;
import B612.foodfood.exception.DataSaveException;
import B612.foodfood.repository.FoodRepository;
import B612.foodfood.repository.MealRepository;
import B612.foodfood.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static B612.foodfood.domain.AccountType.*;
import static B612.foodfood.domain.Activity.*;
import static B612.foodfood.domain.Sex.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @BeforeEach
    public void each() throws DataSaveException {
        PersonalInformation personalInformation = new PersonalInformation("010-1234-5678", "asfd@naver.com", new Address("seoul", "mapo", "12345"), new LogIn("aa", "bb"));
        AchieveBodyGoal bodyGoal = new AchieveBodyGoal(35, 11);
        Member member = new Member("member", new Date(1, 1, 1), 172, MALE, LOT, USER, bodyGoal, personalInformation);
        memberService.join(member);
    }

    @Test
    @DisplayName("member 조회 테스트")
    public void test1() throws Exception {
        //given
        PersonalInformation personalInformation = new PersonalInformation("010-1234-5678", "asfd@naver.com", new Address("seoul", "mapo", "12345"), new LogIn("id", "bb"));
        AchieveBodyGoal bodyGoal = new AchieveBodyGoal(35, 11);
        Member member = new Member("member", new Date(1, 1, 1), 172, MALE, LOT, USER, bodyGoal, personalInformation);

        //when
        memberService.join(member);
        List<Member> allMembers = memberService.findAllMembers();

        //then
        for (Member allMember : allMembers) {
            System.out.println("allMember = " + allMember);
        }

    }

    @Test
    @DisplayName("업데이트 로직 테스트")
    public void test2() throws Exception {
        //given
        Member aa1 = memberService.findMemberByLogInId("aa");

        //when
        memberService.updateMemberPhoneNumber(aa1.getId(), "1111-1111");
        System.out.println(aa1.getPersonalInformation().getPhoneNumber());

        //then
        Member aa2 = memberService.findMemberByLogInId("aa");
        assertEquals(aa2.getPersonalInformation().getPhoneNumber(), "1111-1111");
    }

    @Autowired
    MealRepository mealRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FoodRepository foodRepository;

    @Test
    @DisplayName("Member로 Meal 저장 테스트1")
    public void test3() throws Exception {
        //given
        PersonalInformation personalInformation = new PersonalInformation("010-1234-5678", "asfd@naver.com", new Address("seoul", "mapo", "12345"), new LogIn("id", "bb"));
        AchieveBodyGoal bodyGoal = new AchieveBodyGoal(35, 11);
        Member member = new Member("member", new Date(1, 1, 1), 172, MALE, LOT, USER, bodyGoal, personalInformation);
        Long memberId = memberService.join(member);

        Member findMember = memberService.findMemberById(memberId);

        Food food1 = new Food("food1", new Nutrition(1, 1, 1, 11, 1, 11, 1, 1, 1, 1));
        Food food2 = new Food("food2", new Nutrition(1, 1, 1, 11, 1, 11, 1, 1, 1, 1));
        foodRepository.save(food1);
        foodRepository.save(food2);

        Meal meal = new Meal();
        meal.addBreakFast(food1, 100);
        meal.addBreakFast(food2, 200);

        findMember.addMeal(meal);
        //when
        mealRepository.save(meal);

        //then
        Optional<Meal> findMealById = mealRepository.findById(meal.getId());
        Meal meal1 = findMealById.get();
        System.out.println("meal1 = " + meal1);

        Optional<Meal> findMealByDate = mealRepository.findByDate(meal.getDate());
        Meal meal2 = findMealByDate.get();
        System.out.println("meal2 = " + meal2);
    }

}