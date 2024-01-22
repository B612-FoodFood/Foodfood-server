package B612.foodfood.repository;

import B612.foodfood.domain.BodyComposition;
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
class BodyCompositionRepositoryTest {
    @Autowired
    BodyCompositionRepository bodyCompositionRepository;

    @Test
    public void test() throws Exception{
        //given
        BodyComposition bodyComposition = new BodyComposition(123, 123D, 123D);
        bodyCompositionRepository.save(bodyComposition);

        //when
        Optional<BodyComposition> byDate = bodyCompositionRepository.findByDate(LocalDate.now());

        //then
        System.out.println("byDate.get() = " + byDate.get());
    }
}