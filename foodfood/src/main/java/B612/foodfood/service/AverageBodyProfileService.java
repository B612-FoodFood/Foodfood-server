package B612.foodfood.service;

import B612.foodfood.domain.AverageBodyProfile;
import B612.foodfood.domain.Sex;
import B612.foodfood.exception.AppException;
import B612.foodfood.exception.ErrorCode;
import B612.foodfood.repository.AverageBodyProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static B612.foodfood.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AverageBodyProfileService {

    private final AverageBodyProfileRepository abRepository;

    @Transactional(readOnly = false)
    public Long save(AverageBodyProfile averageBodyProfile) {
        abRepository.save(averageBodyProfile);
        return averageBodyProfile.getId();
    }

    public AverageBodyProfile findAvgBodyProfileById(Long avgBodyProfileId) {
        Optional<AverageBodyProfile> findBodyProfile =
                abRepository.findById(avgBodyProfileId);
        if (findBodyProfile.isEmpty()) {
            log.error("""
                    오류 발생
                    발생위치: AverageBodyProfileService.findAvgBodyProfileById(Long avgBodyProfileId)
                    발생원인: 해당 평균 신체 정보가 존재하지 않습니다.""");
            throw new AppException(NO_DATA_EXISTED, "해당 평균 신체 정보가 존재하지 않습니다.");
        }
        return findBodyProfile.get();
    }

    public AverageBodyProfile findAvgBodyProfileByAge(int age) {
        Optional<AverageBodyProfile> findBodyProfile;

        if (age < 19) {  // 19세 미만은 19세로 측정
            findBodyProfile = abRepository.findByAge(19);
        } else if (age > 99) { // 99세 초과는 99세로 측정
            findBodyProfile = abRepository.findByAge(99);
        } else {
            findBodyProfile = abRepository.findByAge(age);
        }

        if (findBodyProfile.isEmpty()) {
            log.error("""
                    오류 발생
                    발생위치: AverageBodyProfileService.findAvgBodyProfileByAge(int age)
                    발생원인: 해당 나이의 평균 신체 정보가 존재하지 않습니다.""");
            throw new AppException(NO_DATA_EXISTED, "해당 나이의 평균 신체 정보가 존재하지 않습니다.");
        }
        return findBodyProfile.get();
    }

    public List<AverageBodyProfile> findAvgBodyProfileBySex(Sex sex) {
        List<AverageBodyProfile> findBodyProfile =
                abRepository.findBySex(sex);
        if (findBodyProfile.isEmpty()) {
            log.error("""
                    오류 발생
                    발생위치: AverageBodyProfileService.findAvgBodyProfileBySex(Sex sex)
                    발생원인: 해당 성별의 평균 신체 정보가 존재하지 않습니다.""");
            throw new AppException(NO_DATA_EXISTED, "해당 성별의 평균 신체 정보가 존재하지 않습니다.");
        }
        return findBodyProfile;
    }

    public AverageBodyProfile findAvgBodyProfileByAgeAndSex(int age,Sex sex) {
        Optional<AverageBodyProfile> findBodyProfile;
        if (age < 19) {  // 19세 미만은 19세로 측정
            findBodyProfile = abRepository.findByAgeAndSex(19, sex);
        } else if (age > 99) { // 99세 초과는 99세로 측정
            findBodyProfile = abRepository.findByAgeAndSex(99, sex);
        } else {
            findBodyProfile = abRepository.findByAgeAndSex(age, sex);
        }

        if (findBodyProfile.isEmpty()) {
            log.error("""
                    오류 발생
                    발생위치: AverageBodyProfileService.findAvgBodyProfileByAgeAndSex(int age, Sex sex)
                    발생원인: 해당 평균 신체 정보가 존재하지 않습니다.""");
            throw new AppException(NO_DATA_EXISTED, "해당 평균 신체 정보가 존재하지 않습니다.");
        }
        return findBodyProfile.get();
    }
}
