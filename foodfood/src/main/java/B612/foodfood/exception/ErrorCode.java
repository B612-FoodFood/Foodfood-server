package B612.foodfood.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DATA_ALREADY_EXISTED(CONFLICT, ""),
    NO_DATA_EXISTED(NOT_FOUND, ""),

    MEMBER_ID_DUPLICATED(CONFLICT, ""),
    MEMBER_ID_NOT_FOUND(NOT_FOUND, ""),
    INVALID_PASSWORD(UNAUTHORIZED, ""),

    NO_MEMBER_ALLOCATED(FAILED_DEPENDENCY, ""),

    KEYWORD_TOO_SHORT(BAD_REQUEST, ""),
    INVALID_VALUE_ASSIGNMENT(BAD_REQUEST, "");


    private final HttpStatus httpStatus;
    private final String message;
}
