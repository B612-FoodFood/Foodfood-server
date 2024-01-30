package B612.foodfood.repository;

import B612.foodfood.domain.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DiseaseRepository extends JpaRepository<Disease, Long> {
    Optional<Disease> findByName(String name);

    @Query("select d from Disease d " +
            "where d.name like concat('%',:keyword,'%')")
    List<Disease> findByKeyword(@Param("keyword") String keyword);
}