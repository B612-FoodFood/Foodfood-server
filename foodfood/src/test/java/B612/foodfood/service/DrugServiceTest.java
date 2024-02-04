package B612.foodfood.service;

import B612.foodfood.domain.Drug;
import B612.foodfood.exception.AppException;
import B612.foodfood.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class DrugServiceTest {

    @Autowired
    DrugService drugService;
    

    @Test
    @DisplayName("drug 중복 저장 테스트")
    public void test() throws Exception{
        //given
        Drug drug = new Drug("drug-abc");

        //when

        //then
        AppException appException = assertThrows(AppException.class, () -> drugService.save(drug));

        ErrorCode errorCode = appException.getErrorCode();
        String message = appException.getMessage();

        System.out.println("errorCode = " + errorCode);
        System.out.println("message = " + message);
    }
    
    @Test
    public void test2() throws Exception{
        //given
        List<Drug> allDrugs = drugService.findAllDrugs();
        //when
        for (Drug allDrug : allDrugs) {
            System.out.println("allDrug = " + allDrug);
        }
        //then
      
    }
}