package B612.foodfood.repository;

import B612.foodfood.domain.AverageBodyProfile;
import B612.foodfood.domain.Sex;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AverageBodyProfileRepository extends JpaRepository<AverageBodyProfile, Long> {

    List<AverageBodyProfile> findBySex(Sex sex);
    List<AverageBodyProfile> findByHeight(int height);
    Optional<AverageBodyProfile> findBySexAndHeight(Sex sex, int height);


}
