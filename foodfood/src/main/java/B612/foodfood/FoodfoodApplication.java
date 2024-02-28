package B612.foodfood;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
@Slf4j
@SpringBootApplication
@EntityScan(
        basePackageClasses = {Jsr310JpaConverters.class},
        basePackages = {"B612.foodfood.domain"}
)
public class FoodfoodApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodfoodApplication.class, args);
        log.info("Food_Food_Version_0.9.1 (released 2024-02-28)");
    }

}