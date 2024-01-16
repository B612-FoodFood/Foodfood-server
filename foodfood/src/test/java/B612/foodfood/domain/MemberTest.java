package B612.foodfood.domain;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class MemberTest {

    @Autowired
    EntityManager em;

    @BeforeEach
    public void init() {
        Member member = new Member("user",new Date(2000,1,1),172,Sex.MALE,Activity.LOT,AccountType.USER);
        em.persist(member);
    }

}