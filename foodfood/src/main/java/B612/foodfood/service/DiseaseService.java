package B612.foodfood.service;

import B612.foodfood.domain.Disease;
import B612.foodfood.exception.AppException;
import B612.foodfood.exception.DataSaveException;
import B612.foodfood.exception.NoDataExistException;
import B612.foodfood.repository.DiseaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static B612.foodfood.exception.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiseaseService {
    private final DiseaseRepository diseaseRepository;

    @Transactional(readOnly = false)
    public Long save(Disease disease) throws DataSaveException {
        Optional<Disease> findDisease = diseaseRepository.findByName(disease.getName());
        if (findDisease.isPresent()) {
            log.error("오류 발생\n" +
                    "발생위치: DiseaseService.save(Disease disease)\n" +
                    "발생원인: 이미 저장된 질병입니다.");
            throw new AppException(DATA_ALREADY_EXISTED,
                    "이미 저장된 질병입니다.");
        }
        diseaseRepository.save(disease);
        return disease.getId();
    }

    public Disease findDisease(Long diseaseId) {
        Optional<Disease> findDisease = diseaseRepository.findById(diseaseId);
        if (findDisease.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: DiseaseService.findDisease(Long diseaseId)\n" +
                    "발생원인: 해당 질병을 찾을 수 없습니다.");
            throw new AppException(NO_DATA_EXISTED,
                    "해당 질병을 찾을 수 없습니다.");
        }
        return findDisease.get();
    }

    public Disease findDiseaseByName(String name) {
        Optional<Disease> findDisease = diseaseRepository.findByName(name);
        if (findDisease.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: DiseaseService.findDiseaseByName(String name)\n" +
                    "발생원인: 해당 이름의 질병을 찾을 수 없습니다.");
            throw new AppException(NO_DATA_EXISTED,
                    "해당 이름의 질병을 찾을 수 없습니다.");
        }
        return findDisease.get();
    }

    public List<Disease> findDiseaseByKeyword(String keyword) {
        if (keyword.length() < 2) {
            log.error("오류 발생\n" +
                    "발생위치: DiseaseService.findDiseaseByKeyword(String keyword)\n" +
                    "발생원인: 키워드는 두 글자 이상이어야 합니다.");
            throw new AppException(KEYWORD_TOO_SHORT,
                    "키워드는 두 글자 이상이어야 합니다.");
        }
        return diseaseRepository.findByKeyword(keyword);
    }

    public List<Disease> findAllDiseases() {
        return diseaseRepository.findAll();
    }
}
