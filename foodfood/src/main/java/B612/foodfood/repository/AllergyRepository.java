package B612.foodfood.repository;

import B612.foodfood.domain.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AllergyRepository extends JpaRepository<Allergy,Long> {
    Optional<Allergy> findByName(String name);

}
