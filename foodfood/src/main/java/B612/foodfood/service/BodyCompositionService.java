package B612.foodfood.service;

import B612.foodfood.domain.BodyComposition;
import B612.foodfood.exception.AppException;
import B612.foodfood.exception.NoDataExistException;
import B612.foodfood.repository.BodyCompositionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static B612.foodfood.exception.ErrorCode.NO_DATA_EXISTED;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BodyCompositionService {

    private final BodyCompositionRepository bodyCompositionRepository;

    public Long save(BodyComposition bodyComposition) {
        bodyCompositionRepository.save(bodyComposition);
        return bodyComposition.getId();
    }

    public BodyComposition findBodyCompositionById(Long bodyCompId) {
        Optional<BodyComposition> findBodyComp = bodyCompositionRepository.findById(bodyCompId);
        if (findBodyComp.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: BodyCompositionService.findBodyCompositionById(Long bodyCompId)\n" +
                    "발생원인: 해당 신체 정보를 찾을 수 없습니다.");
            throw new AppException(NO_DATA_EXISTED,
                    "해당 신체 정보를 찾을 수 없습니다.");
        }
        return findBodyComp.get();
    }

    public BodyComposition findBodyCompositionByDate(LocalDate date) {
        Optional<BodyComposition> findBodyComp = bodyCompositionRepository.findByDate(date);
        if (findBodyComp.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: BodyCompositionService.findBodyCompositionByDate(LocalDate date)\n" +
                    "발생원인: 해당 날짜에 해당하는 신체 정보를 찾을 수 없습니다.");
            throw new AppException(NO_DATA_EXISTED,
                    "해당 날짜에 해당하는 신체 정보를 찾을 수 없습니다.");
        }
        return findBodyComp.get();
    }

    public List<BodyComposition> findAllBodyCompositions() {
        return bodyCompositionRepository.findAll();
    }
}
