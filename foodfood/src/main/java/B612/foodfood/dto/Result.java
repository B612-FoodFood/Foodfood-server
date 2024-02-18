package B612.foodfood.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class Result<T> {
    private HttpStatus status;
    private String message;
    private T value;

    public Result(HttpStatus status, String message, T value) {
        this.status = status;
        this.message = message;
        this.value = value;
    }
}
