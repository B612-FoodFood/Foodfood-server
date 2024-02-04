package B612.foodfood.repository;

import B612.foodfood.domain.AverageBodyProfile;
import B612.foodfood.domain.Sex;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AverageBodyProfileRepository extends JpaRepository<AverageBodyProfile, Long> {

    Optional<AverageBodyProfile> findByAge(int age);
    List<AverageBodyProfile> findBySex(Sex sex);
    Optional<AverageBodyProfile> findByAgeAndSex(int age, Sex sex);
}
