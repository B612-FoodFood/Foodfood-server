package B612.foodfood.service;

import B612.foodfood.domain.Ingredient;
import B612.foodfood.exception.AppException;
import B612.foodfood.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static B612.foodfood.exception.ErrorCode.DATA_ALREADY_EXISTED;
import static B612.foodfood.exception.ErrorCode.NO_DATA_EXISTED;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    @Transactional(readOnly = false)
    public Long save(Ingredient ingredient) {
        if (ingredientRepository.findByName(ingredient.getName()).isPresent()) {
            log.error("오류 발생\n" +
                    "발생위치: IngredientService.save(Ingredient ingredient)\n" +
                    "발생원인: 이미 존재하는 식재료입니다.");
            throw new AppException(DATA_ALREADY_EXISTED,
                    "이미 존재하는 식재료입니다.");
        }
        ingredientRepository.save(ingredient);
        return ingredient.getId();
    }

    public Ingredient findIngredientById(Long ingredientId) {
        Optional<Ingredient> findIngredient = ingredientRepository.findById(ingredientId);
        if (findIngredient.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: IngredientService.findIngredientById(Long ingredientId)\n" +
                    "발생원인: 존재하지 않는 식재료입니다.");
            throw new AppException(NO_DATA_EXISTED,
                    "존재하지 않는 식재료입니다.");
        }

        return findIngredient.get();
    }

    public Ingredient findIngredientByName(String name) {
        Optional<Ingredient> findIngredient = ingredientRepository.findByName(name);
        if (findIngredient.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: IngredientService.findIngredientByName(String name)\n" +
                    "발생원인: 존재하지 않는 식재료입니다.");
            throw new AppException(NO_DATA_EXISTED,
                    "존재하지 않는 식재료입니다.");
        }

        return findIngredient.get();
    }

    public List<Ingredient> findAllIngredients() {
        return ingredientRepository.findAll();
    }
}

