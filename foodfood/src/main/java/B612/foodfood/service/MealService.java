package B612.foodfood.service;

import B612.foodfood.domain.Meal;
import B612.foodfood.domain.Member;
import B612.foodfood.exception.AppException;
import B612.foodfood.exception.DataSaveException;
import B612.foodfood.exception.ErrorCode;
import B612.foodfood.exception.NoDataExistException;
import B612.foodfood.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static B612.foodfood.exception.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MealService {
    private final MealRepository mealRepository;

     /* Meal과 Member의 생성 순서 조건
     (1) new Meal()               ... meal 객체 생성
     (2) member.addMeal(meal)     ... member에 meal 추가. member는 이미 persist 완료된 상태
     (3) mealService.save(meal)   ... meal을 persist. meal이 save되는 시점에는 meal은 항상 member를 가지고 있다.
     (4) meal.addLunch(food)      ... meal에 food 추가.   food는 이미 persist 완료된 상태*/
    public Long save(Meal meal) {
        // meal을 저정하려고 할 때, 연관된 member가 존재하지 않는 경우 에러가 발생함.
        if (meal.getMember() == null) {
            throw new AppException(NO_MEMBER_ALLOCATED,
                    "오류 발생\n" +
                    "발생위치: MealService.save(Meal meal)\n" +
                    "발생원인: 어떤 유저의 식사 내역인지가 정해지지 않았습니다");
        }

        mealRepository.save(meal);
        return meal.getId();
    }

    public Meal findMealById(Long mealId) {
        Optional<Meal> findMeal = mealRepository.findById(mealId);
        if (findMeal.isEmpty()) {
            throw new AppException(NO_DATA_EXISTED,
                    "오류 발생\n" +
                    "발생위치:MealService.findMealById(Long mealId)\n" +
                    "발생원인: 존재하지 않은 식사 정보입니다.");
        }
        return findMeal.get();
    }

    public Meal findMealByDate(LocalDate date) {
        Optional<Meal> findMeal = mealRepository.findByDate(date);
        if (findMeal.isEmpty()) {
            throw new AppException(NO_DATA_EXISTED,
                    "오류 발생\n" +
                    "발생위치:MealService.findMealByDate(LocalDate date)\n" +
                    "발생원인: 해당 날짜에는 식사 정보가 존재하지 않습니다.");
        }
        return findMeal.get();
    }

    public List<Meal> findAllMeals() {
        return mealRepository.findAll();
    }
}
