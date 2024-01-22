package B612.foodfood.service;

import B612.foodfood.domain.Food;
import B612.foodfood.exception.AppException;
import B612.foodfood.exception.DataSaveException;
import B612.foodfood.exception.ErrorCode;
import B612.foodfood.exception.NoDataExistException;
import B612.foodfood.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static B612.foodfood.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FoodService {

    private final FoodRepository foodRepository;

    @Transactional(readOnly = true)
    public Long save(Food food) {
        if (!foodRepository.findByName(food.getName()).isEmpty()) {
            throw new AppException(DATA_ALREADY_EXISTED,
                    "오류 발생\n" +
                    "발생위치: FoodService.save(Food food)\n" +
                    "발생원인: 이미 존재하는 음식입니다.");
        }
        foodRepository.save(food);
        return food.getId();
    }

    public Food findFoodById(Long foodId) {
        Optional<Food> findFood = foodRepository.findById(foodId);
        if (findFood.isEmpty()) {
            throw new AppException(NO_DATA_EXISTED,
                    "오류 발생\n" +
                    "발생위치: FoodService.findFoodById(Long foodId)\n" +
                    "발생원인: 존재하지 않는 음식입니다.");
        }

        return findFood.get();
    }

    public List<Food> findAllFoods() {
        return foodRepository.findAll();
    }

    public Food findFoodByName(String name) {
        Optional<Food> findFood = foodRepository.findByName(name);
        if (findFood.isEmpty()) {
             throw new AppException(NO_DATA_EXISTED,
                     "오류 발생\n" +
                    "발생위치: FoodService.findFoodByName(String name)\n" +
                    "발생원인: 존재하지 않는 음식입니다.");
         }
        return findFood.get();
    }

    public List<Food> findFoodThatHasMoreCarlories(double calories) {
        return foodRepository.findThatHasMoreCarlories(calories);
    }

    public List<Food> findFoodThatHasLessCarlories(double calories) {
        return foodRepository.findThatHasLessCarlories(calories);
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
}
