package B612.foodfood.repository;

import B612.foodfood.domain.Disease;
import B612.foodfood.domain.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DrugRepository extends JpaRepository<Drug,Long> {
    Optional<Drug> findByName(String name);

    @Query("select d from Drug d " +
            "where d.name like concat('%',:keyword,'%')")
    List<Drug> findByKeyword(@Param("keyword") String keyword);
}
