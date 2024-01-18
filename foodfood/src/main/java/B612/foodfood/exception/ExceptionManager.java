package B612.foodfood.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {

    // DataSaveException이 발생하는 경우 response로 메시지를 보내줌
    @ExceptionHandler(DataSaveException.class)
    public ResponseEntity<?> dataSaveExceptionHandler(DataSaveException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }
}
