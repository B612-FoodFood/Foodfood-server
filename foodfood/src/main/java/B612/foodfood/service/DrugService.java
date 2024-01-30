package B612.foodfood.service;

import B612.foodfood.domain.Disease;
import B612.foodfood.domain.Drug;
import B612.foodfood.exception.AppException;
import B612.foodfood.exception.DataSaveException;
import B612.foodfood.exception.ErrorCode;
import B612.foodfood.exception.NoDataExistException;
import B612.foodfood.repository.DrugRepository;
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
public class DrugService {

    private final DrugRepository drugRepository;

    @Transactional(readOnly = false)
    public Long save(Drug drug) {
        Optional<Drug> findDrug = drugRepository.findByName(drug.getName());
        if (findDrug.isPresent()) {
            log.error("오류 발생\n" +
                    "발생위치: DrugService.save(Drug drug)\n" +
                    "발생원인: 이미 저장된 약물입니다.");
            throw new AppException(DATA_ALREADY_EXISTED,
                    "이미 저장된 약물입니다.");
        }
        drugRepository.save(drug);
        return drug.getId();
    }

    public Drug findDrug(Long drugId) {
        Optional<Drug> findDrug = drugRepository.findById(drugId);
        if (findDrug.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: DrugService.findDrug(Long drugId)\n" +
                    "발생원인: 해당 약물을 찾을 수 없습니다.");
            throw new AppException(NO_DATA_EXISTED,
                    "해당 약물을 찾을 수 없습니다.");
        }
        return findDrug.get();
    }

    public Drug findDrugByName(String name) {
        Optional<Drug> findDrug = drugRepository.findByName(name);
        if (findDrug.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: DrugService.findDrugByName(String name)\n" +
                    "발생원인: 해당 이름의 약물을 찾을 수 없습니다.");
            throw new AppException(NO_DATA_EXISTED,
                    "해당 이름의 약물을 찾을 수 없습니다.");
        }
        return findDrug.get();
    }

    public List<Drug> findDrugByKeyword(String keyword) {
        return drugRepository.findByKeyword(keyword);
    }

    public List<Drug> findAllDrugs() {
        return drugRepository.findAll();
    }
}
