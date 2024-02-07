package B612.foodfood.controller.api;

import B612.foodfood.service.MemberService;
import B612.foodfood.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/login/oauth2", produces = "application/json;charset=utf-8")
@RequiredArgsConstructor
public class OAuthLoginController {

    private final OAuthService oAuthService;

    @GetMapping("/code/{registrationId}")
    public void googleLogin(@RequestParam("code") String code, @PathVariable("registrationId") String registrationId) {
        System.out.println("code = " + code);
        System.out.println("registrationId = " + registrationId);

        oAuthService.socialLogin(code,registrationId);
    }
}
