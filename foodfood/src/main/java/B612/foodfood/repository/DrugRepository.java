package B612.foodfood.repository;

import B612.foodfood.domain.Drug;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DrugRepository extends JpaRepository<Drug,Long> {
    Optional<Drug> findByName(String name);
}
