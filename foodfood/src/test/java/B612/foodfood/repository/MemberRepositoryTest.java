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

import java.util.Date;
import java.util.Optional;

import static B612.foodfood.domain.AccountType.*;
import static B612.foodfood.domain.Activity.*;
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
        Address address = new Address("Seoul", "Hongdae", "12345");
        LogIn logIn = new LogIn("id", "password");
        PersonalInformation personalInformation = new PersonalInformation("010-1234-5678", "email@gmail.com", address, logIn);
        Member member = new Member("user",new Date(2000, 05, 04), 172, MALE, LOT, USER);
        
        member.setPersonalInformation(personalInformation);
        memberRepository.save(member);
    }
    
    @Test
    @DisplayName("로그인 아이디로 멤버 조회")
    public void findTest() throws Exception{
        //given
        //when
        Optional<Member> findMember = memberRepository.findByLogInId("id");

        //then
        if (findMember.isEmpty()) {
            fail("there is no member exists");
        }

        Member member = findMember.get();
        System.out.println("Name = " + member.getName());
    }
}