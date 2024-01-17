package B612.foodfood.service;

import B612.foodfood.domain.Allergy;
import B612.foodfood.exception.DataSaveException;
import B612.foodfood.exception.NoDataExistException;
import B612.foodfood.repository.AllergyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AllergyService {
    private final AllergyRepository allergyRepository;

    @Transactional(readOnly = false)
    public Long save(Allergy allergy) throws DataSaveException {
        Optional<Allergy> findAllergy = allergyRepository.findByName(allergy.getName());
        if (findAllergy.isPresent()) {
            throw new DataSaveException("오류 발생\n" +
                    "발생위치: AllergyService.save(Allergy allergy)\n" +
                    "발생원인: 이미 저장된 알러지입니다.");
        }
        allergyRepository.save(allergy);
        return allergy.getId();
    }

    public Allergy findAllergy(Long allergyId) throws NoDataExistException {
        Optional<Allergy> findAllergy = allergyRepository.findById(allergyId);
        if (findAllergy.isEmpty()) {
            throw new NoDataExistException("오류 발생\n" +
                    "발생위치: AllergyService.findAllergy(Long allergyId)\n" +
                    "발생원인: 해당 알러지를 찾을 수 없습니다.");
        }
        return findAllergy.get();
    }

    public Allergy findAllergyByName(String name) throws NoDataExistException {
        Optional<Allergy> findAllergy = allergyRepository.findByName(name);
        if (findAllergy.isEmpty()) {
            throw new NoDataExistException("오류 발생\n" +
                    "발생위치: AllergyService.findAllergyByName(String name)\n" +
                    "발생원인: 해당 이름의 알러지를 찾을 수 없습니다.");
        }
        return findAllergy.get();
    }

    public List<Allergy> findAllAllergies() {
        return allergyRepository.findAll();
    }

}
