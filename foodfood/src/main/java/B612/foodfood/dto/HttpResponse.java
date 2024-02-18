package B612.foodfood.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class HttpResponse {
    private HttpStatus status;
    private String message;

    public HttpResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
