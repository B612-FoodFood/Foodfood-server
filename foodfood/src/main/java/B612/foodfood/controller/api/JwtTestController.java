package B612.foodfood.controller.api;

import B612.foodfood.dto.JwtTestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/jwt")
public class JwtTestController {

    @PostMapping("/name")
    public JwtTestResponse test(Authentication authentication) {
        System.out.println("name: " + authentication.getName());
        System.out.println("detail: " + authentication.getDetails());
        System.out.println("credential: " + authentication.getCredentials());
        System.out.println("authority: " + authentication.getAuthorities());

        // username을 json으로 변환하여 반환함
        return new JwtTestResponse(authentication.getName());
    }
}
