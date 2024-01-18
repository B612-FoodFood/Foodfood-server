package B612.foodfood.service;

import B612.foodfood.domain.Disease;
import B612.foodfood.exception.DataSaveException;
import B612.foodfood.exception.NoDataExistException;
import B612.foodfood.repository.DiseaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiseaseService {
    private final DiseaseRepository diseaseRepository;

    @Transactional(readOnly = false)
    public Long save(Disease disease) throws DataSaveException {
        Optional<Disease> findDisease = diseaseRepository.findByName(disease.getName());
        if (findDisease.isPresent()) {
            throw new DataSaveException("오류 발생\n" +
                    "발생위치: DiseaseService.save(Disease disease)\n" +
                    "발생원인: 이미 저장된 질병입니다.");
        }
        diseaseRepository.save(disease);
        return disease.getId();
    }

    public Disease findDisease(Long diseaseId) throws NoDataExistException {
        Optional<Disease> findDisease = diseaseRepository.findById(diseaseId);
        if (findDisease.isEmpty()) {
            throw new NoDataExistException("오류 발생\n" +
                    "발생위치: DiseaseService.findDisease(Long diseaseId)\n" +
                    "발생원인: 해당 질병을 찾을 수 없습니다.");
        }
        return findDisease.get();
    }

    public Disease findDiseaseByName(String name) throws NoDataExistException {
        Optional<Disease> findDisease = diseaseRepository.findByName(name);
        if (findDisease.isEmpty()) {
            throw new NoDataExistException("오류 발생\n" +
                    "발생위치: DiseaseService.findDiseaseByName(String name)\n" +
                    "발생원인: 해당 이름의 질병을 찾을 수 없습니다.");
        }
        return findDisease.get();
    }

    public List<Disease> findAllDiseases() {
        return diseaseRepository.findAll();
    }
}
