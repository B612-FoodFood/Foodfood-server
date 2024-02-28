package B612.foodfood.service;

import B612.foodfood.domain.Category;
import B612.foodfood.domain.Food;
import B612.foodfood.exception.AppException;
import B612.foodfood.exception.DataSaveException;
import B612.foodfood.exception.ErrorCode;
import B612.foodfood.exception.NoDataExistException;
import B612.foodfood.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static B612.foodfood.exception.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FoodService {

    private final FoodRepository foodRepository;

    @Transactional(readOnly = false)
    public Long save(Food food) {
        if (foodRepository.findByName(food.getName()).isPresent()) {
            log.error("오류 발생\n" +
                    "발생위치: FoodService.save(Food food)\n" +
                    "발생원인: 이미 존재하는 음식입니다.");
            throw new AppException(DATA_ALREADY_EXISTED,
                    "이미 존재하는 음식입니다.");
        }
        foodRepository.save(food);
        return food.getId();
    }

    public Food findFoodById(Long foodId) {
        Optional<Food> findFood = foodRepository.findById(foodId);
        if (findFood.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: FoodService.findFoodById(Long foodId)\n" +
                    "발생원인: 존재하지 않는 음식입니다.");
            throw new AppException(NO_DATA_EXISTED,
                    "존재하지 않는 음식입니다.");
        }

        return findFood.get();
    }

    public List<Food> findAllFoods() {
        return foodRepository.findAll();
    }

    public Food findFoodByName(String name) {
        Optional<Food> findFood = foodRepository.findByName(name);
        if (findFood.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: FoodService.findFoodByName(String name)\n" +
                    "발생원인: 존재하지 않는 음식입니다.");
            throw new AppException(NO_DATA_EXISTED,
                    "존재하지 않는 음식입니다.");
        }
        return findFood.get();
    }

    public List<Food> findFoodThatHasMoreCalories(double calories) {
        return foodRepository.findThatHasMoreCalories(calories);
    }

    public List<Food> findFoodThatHasLessCalories(double calories) {
        return foodRepository.findThatHasLessCalories(calories);
    }

    public List<Food> findFoodThatHasMoreCarbonHydrate(double carbonHydrate) {
        return foodRepository.findThatHasMoreCarbonHydrate(carbonHydrate);
    }

    public List<Food> findFoodThatHasLessCarbonHydrate(double carbonHydrate) {
        return foodRepository.findThatHasLessCarbonHydrate(carbonHydrate);
    }

    public List<Food> findFoodThatHasMoreProtein(double protein) {
        return foodRepository.findThatHasMoreProtein(protein);
    }

    public List<Food> findFoodThatHasLessProtein(double protein) {
        return foodRepository.findThatHasLessProtein(protein);
    }

    public List<Food> findFoodThatHasMoreFat(double fat) {
        return foodRepository.findThatHasMoreFat(fat);
    }

    public List<Food> findFoodThatHasLessFat(double fat) {
        return foodRepository.findThatHasLessFat(fat);
    }

    public List<Food> findFoodByKeyword(String keyword) {
        if (keyword.length() < 2) {
            log.error("오류 발생\n" +
                    "발생위치: FoodService.findFoodByKeyword(String keyword)\n" +
                    "발생원인: 키워드는 두 글자 이상이어야 합니다.");
            throw new AppException(KEYWORD_TOO_SHORT,
                    "키워드는 두 글자 이상이어야 합니다.");
        }
        return foodRepository.findByKeyword(keyword);
    }

    public List<Food> findFoodByCategory(Category category) {
        return foodRepository.findByCategory(category);
    }

    public List<Food> findFoodBetweenTwoCalories(double minCalories, double maxCalories, int pageSize) {
        if (minCalories > maxCalories) {
            log.error("오류 발생\n" +
                    "발생위치: FoodService.findFoodBetweenTwoCalories(double minCalories, double maxCalories, int pageSize)\n" +
                    "발생원인: 사잇값 지정이 잘못되었습니다.");
            throw new AppException(INVALID_VALUE_ASSIGNMENT,
                    "사잇값 지정이 잘못되었습니다.");
        }
        Pageable pageable = Pageable.ofSize(pageSize);
        return foodRepository.findThatBetweenTwoCalories(minCalories, maxCalories, pageable);
    }

    public List<Food> findFoodBetweenTwoCarbonHydrate(double minCarbonHydrate, double maxCarbonHydrate, int pageSize) {
        if (minCarbonHydrate > maxCarbonHydrate) {
            log.error("오류 발생\n" +
                    "발생위치: FoodService.findFoodBetweenTwoCarbonHydrate(double minCarbonHydrate, double maxCarbonHydrate, int pageSize)\n" +
                    "발생원인: 사잇값 지정이 잘못되었습니다.");
            throw new AppException(INVALID_VALUE_ASSIGNMENT,
                    "사잇값 지정이 잘못되었습니다.");
        }
        Pageable pageable = Pageable.ofSize(pageSize);
        return foodRepository.findThatBetweenTwoCarbonHydate(minCarbonHydrate, maxCarbonHydrate, pageable);
    }

    public List<Food> findFoodBetweenTwoProtein(double minProtein, double maxProtein, int pageSize) {
        if (minProtein > maxProtein) {
            log.error("오류 발생\n" +
                    "발생위치: FoodService.findFoodBetweenTwoProtein(double minProtein, double maxProtein, int pageSize)\n" +
                    "발생원인: 사잇값 지정이 잘못되었습니다.");
            throw new AppException(INVALID_VALUE_ASSIGNMENT,
                    "사잇값 지정이 잘못되었습니다.");
        }
        Pageable pageable = Pageable.ofSize(pageSize);
        return foodRepository.findThatBetweenTwoProtein(minProtein, maxProtein, pageable);
    }

    public List<Food> findFoodBetweenTwoFat(double minFat, double maxFat, int pageSize) {
        if (minFat > maxFat) {
            log.error("오류 발생\n" +
                    "발생위치: FoodService.findFoodBetweenTwoFat(double minFat, double maxFat, int pageSize)\n" +
                    "발생원인: 사잇값 지정이 잘못되었습니다.");
            throw new AppException(INVALID_VALUE_ASSIGNMENT,
                    "사잇값 지정이 잘못되었습니다.");
        }
        Pageable pageable = Pageable.ofSize(pageSize);
        return foodRepository.findThatBetweenTwoFat(minFat, maxFat, pageable);
    }

    public List<Food> findFoodBetweenTwoNutrition(double minCalories, double maxCalories,
                                                  double minCarbonHydrate, double maxCarbonHydrate,
                                                  double minProtein, double maxProtein,
                                                  double minFat, double maxFat,
                                                  int pageSize) {
        if (minCalories > maxCalories ||
                minCarbonHydrate > maxCarbonHydrate ||
                minProtein > maxProtein || minFat > maxFat)
        {
            log.error("오류 발생\n" +
                    "발생위치: FoodService.findFoodBetweenTwoNutrition(...)\n" +
                    "발생원인: 사잇값 지정이 잘못되었습니다.");
            throw new AppException(INVALID_VALUE_ASSIGNMENT,
                    "사잇값 지정이 잘못되었습니다.");
        }

        Pageable pageable = Pageable.ofSize(pageSize);
        return foodRepository.findThatBetweenTwoNutrition(minCalories, maxCalories,
                minCarbonHydrate, maxCarbonHydrate,
                minProtein, maxProtein,
                minFat, maxFat,
                pageable);
    }
}
