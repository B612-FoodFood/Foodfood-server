package B612.foodfood.repository;

import B612.foodfood.domain.AvoidFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AvoidFoodRepository extends JpaRepository<AvoidFood,Long> {
}
