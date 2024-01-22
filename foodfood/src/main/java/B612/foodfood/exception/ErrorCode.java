package B612.foodfood.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    MEMBER_ID_DUPLICATED(HttpStatus.CONFLICT," "),
    NO_MEMBER_ALLOCATED(HttpStatus.CONFLICT," "),
    DATA_ALREADY_EXISTED(HttpStatus.CONFLICT," "),
    NO_DATA_EXISTED(HttpStatus.CONFLICT, "");

    private HttpStatus httpStatus;
    private String message;
}
