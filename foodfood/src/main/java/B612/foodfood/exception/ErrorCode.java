package B612.foodfood.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DATA_ALREADY_EXISTED(HttpStatus.CONFLICT,""),
    NO_DATA_EXISTED(HttpStatus.NOT_FOUND, ""),

    MEMBER_ID_DUPLICATED(HttpStatus.CONFLICT,""),
    MEMBER_ID_NOT_FOUND(HttpStatus.NOT_FOUND,""),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,""),

    NO_MEMBER_ALLOCATED(HttpStatus.FAILED_DEPENDENCY,""),

    KEYWORD_TOO_SHORT(HttpStatus.BAD_REQUEST,"");

    private final HttpStatus httpStatus;
    private final String message;
}
