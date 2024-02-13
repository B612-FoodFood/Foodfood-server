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

    public List<AverageBodyProfile> findAvgBodyProfileByHeight(int height) {
        List<AverageBodyProfile> findBodyProfile;

        if (90 <= height && height < 150) {  // 90 ~ 149는 150으로 측정
            findBodyProfile = abRepository.findByHeight(150);
        } else if (220 >= height && height > 190) { // 191 ~ 220는 190으로 측정
            findBodyProfile = abRepository.findByHeight(190);
        } else {
            findBodyProfile = abRepository.findByHeight(height);
        }

        if (findBodyProfile.isEmpty()) {  // 90cm 이하는 오류
            log.error("""
                    오류 발생
                    발생위치: AverageBodyProfileService.findAvgBodyProfileByAge(int age)
                    발생원인: 신장 220cm 초과, 90cm 미만의 정보는 존재하지 않습니다.""");
            throw new AppException(NO_DATA_EXISTED, "신장 220cm 초과, 90cm 미만의 정보는 존재하지 않습니다.");
        }

        return findBodyProfile;
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

    public AverageBodyProfile findAvgBodyProfileBySexAndHeight(Sex sex,int height) {
        Optional<AverageBodyProfile> findBodyProfile;
        if (90 <= height && height < 150) {
            findBodyProfile = abRepository.findBySexAndHeight(sex,150);
        } else if (220 >= height && height > 190) {
            findBodyProfile = abRepository.findBySexAndHeight(sex,190);
        } else {
            findBodyProfile = abRepository.findBySexAndHeight(sex,height);
        }

        if (findBodyProfile.isEmpty()) {
            log.error("""
                    오류 발생
                    발생위치: AverageBodyProfileService.findAvgBodyProfileByAgeAndSex(int age, Sex sex)
                    발생원인: 신장 220cm 초과, 90cm 미만의 정보는 존재하지 않습니다.""");
            throw new AppException(NO_DATA_EXISTED, "신장 220cm 초과, 90cm 미만의 정보는 존재하지 않습니다.");
        }
        return findBodyProfile.get();
    }
}
