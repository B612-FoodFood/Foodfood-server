package B612.foodfood.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class TestController {
    @RequestMapping("/test")
    public String home() {
        log.info("/test");
        return "index";
    }
}
