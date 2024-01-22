package B612.foodfood.repository;

import B612.foodfood.domain.BodyComposition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface BodyCompositionRepository extends JpaRepository<BodyComposition,Long> {

    @Query(value = "select b from BodyComposition b " +
            "left join fetch  b.member m " +
            "where b.date= :date")
    Optional<BodyComposition> findByDate(@Param("date") LocalDate date);
}

