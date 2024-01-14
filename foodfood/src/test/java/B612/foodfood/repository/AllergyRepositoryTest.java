package B612.foodfood.repository;

import B612.foodfood.domain.Allergy;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class AllergyRepositoryTest {
    @Autowired
    AllergyRepository allergyRepository;

    @Test
    public void save() throws Exception{
        //given
        Allergy allergy = new Allergy("피부알러지");

        //when
        allergyRepository.save(allergy);

        //then
        Optional<Allergy> findAllergy = allergyRepository.findById(allergy.getId());
        Optional<Allergy> byName = allergyRepository.findByName(allergy.getName());

        System.out.println("findAllergy.get().getName() = " + findAllergy.get().getName());
        System.out.println("byName.get() = " + byName.get().getName());
    }
}