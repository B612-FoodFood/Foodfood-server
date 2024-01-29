package B612.foodfood.repository;

import B612.foodfood.domain.Food;
import B612.foodfood.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Long> {

    Optional<Food> findByName(String name);

    /**
     * 영양소별 검색 (칼로리+탄단지)
     */
    // 칼로리
    @Query("select f from Food f " +
            "where f.nutrition.calories >= :calories")
    List<Food> findThatHasMoreCarlories(@Param("calories") double calories);

    @Query("select f from Food f " +
            "where f.nutrition.calories <= :calories")
    List<Food> findThatHasLessCarlories(@Param("calories") double calories);

    // 탄수화물
    @Query("select f from Food f " +
            "where f.nutrition.carbonHydrate >= : carbonHydrate")
    List<Food> findThatHasMoreCarbonHydrate(@Param("carbonHydrate") double carbonHydrate);

    @Query("select f from Food f " +
            "where f.nutrition.carbonHydrate <= : carbonHydrate")
    List<Food> findThatHasLessCarbonHydrate(@Param("carbonHydrate") double carbonHydrate);

    // 단백질
    @Query("select f from Food f " +
            "where f.nutrition.protein >= :protein")
    List<Food> findThatHasMoreProtein(@Param("protein") double protein);

    @Query("select f from Food f " +
            "where f.nutrition.protein <= :protein")
    List<Food> findThatHasLessProtein(@Param("protein") double protein);

    //지방
    @Query("select f from Food f " +
            "where f.nutrition.fat >= :fat")
    List<Food> findThatHasMoreFat(double fat);

    @Query("select f from Food f " +
            "where f.nutrition.fat <= :fat")
    List<Food> findThatHasLessFat(double fat);
}