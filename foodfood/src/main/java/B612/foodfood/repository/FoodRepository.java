package B612.foodfood.repository;

import B612.foodfood.domain.Category;
import B612.foodfood.domain.Disease;
import B612.foodfood.domain.Food;
import B612.foodfood.domain.Ingredient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Long> {

    Optional<Food> findByName(String name);

    @Query("select f from Food f " +
            "where f.name like concat('%',:keyword,'%')")
    List<Food> findByKeyword(@Param("keyword") String keyword);

    @Query("select f from Food f " +
            "where f.category =: category")
    List<Food> findByCategory(@Param("category") Category category);

    /**
     * 영양소별 검색 (칼로리+탄단지)
     */
    // 칼로리
    @Query("select f from Food f " +
            "where f.nutrition.calories >= :calories")
    List<Food> findThatHasMoreCalories(@Param("calories") double calories);

    @Query("select f from Food f " +
            "where f.nutrition.calories <= :calories")
    List<Food> findThatHasLessCalories(@Param("calories") double calories);

    @Query("select f from Food f " +
            "where f.nutrition.calories between :minCalories and :maxCalories")
    List<Food> findThatBetweenTwoCalories(@Param("minCalories") double minCalories, @Param("maxCalories") double maxCalories, Pageable pageable);

    // 탄수화물
    @Query("select f from Food f " +
            "where f.nutrition.carbonHydrate >= : carbonHydrate")
    List<Food> findThatHasMoreCarbonHydrate(@Param("carbonHydrate") double carbonHydrate);

    @Query("select f from Food f " +
            "where f.nutrition.carbonHydrate <= : carbonHydrate")
    List<Food> findThatHasLessCarbonHydrate(@Param("carbonHydrate") double carbonHydrate);

    @Query("select f from Food f " +
            "where f.nutrition.carbonHydrate between :minCarbonHydrate and :maxCarbonHydrate")
    List<Food> findThatBetweenTwoCarbonHydate(@Param("minCarbonHydrate") double minCarbonHydrate, @Param("maxCarbonHydrate") double maxCarbonHydrate, Pageable pageable);

    // 단백질
    @Query("select f from Food f " +
            "where f.nutrition.protein >= :protein")
    List<Food> findThatHasMoreProtein(@Param("protein") double protein);

    @Query("select f from Food f " +
            "where f.nutrition.protein <= :protein")
    List<Food> findThatHasLessProtein(@Param("protein") double protein);

    @Query("select f from Food f " +
            "where f.nutrition.protein between :minProtein and :maxProtein")
    List<Food> findThatBetweenTwoProtein(@Param("minProtein") double minProtein, @Param("maxProtein") double maxProtein, Pageable pageable);

    //지방
    @Query("select f from Food f " +
            "where f.nutrition.fat >= :fat")
    List<Food> findThatHasMoreFat(@Param("fat") double fat);

    @Query("select f from Food f " +
            "where f.nutrition.fat <= :fat")
    List<Food> findThatHasLessFat(@Param("fat") double fat);

    @Query("select f from Food f " +
            "where f.nutrition.fat between :minFat and :maxFat")
    List<Food> findThatBetweenTwoFat(@Param("minFat") double minFat, @Param("maxFat") double maxFat, Pageable pageable);

    @Query("select f from Food f " +
            "where f.nutrition.calories between :minCalories and :maxCalories " +
            "and f.nutrition.carbonHydrate between :minCarbonHydrate and :maxCarbonHydrate " +
            "and f.nutrition.protein between :minProtein and :maxProtein " +
            "and f.nutrition.fat between :minFat and :maxFat ")
    List<Food> findThatBetweenTwoNutrition(@Param("minCalories") double minCalories, @Param("maxCalories") double maxCalories,
                                           @Param("minCarbonHydrate") double minCarbonHydrate, @Param("maxCarbonHydrate") double maxCarbonHydrate,
                                           @Param("minProtein") double minProtein, @Param("maxProtein") double maxProtein,
                                           @Param("minFat") double minFat, @Param("maxFat") double maxFat,
                                           Pageable pageable);
}