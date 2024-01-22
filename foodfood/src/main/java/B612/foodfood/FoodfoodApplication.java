package B612.foodfood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
@EntityScan(
        basePackageClasses = {Jsr310JpaConverters.class},
        basePackages = {"B612.foodfood.domain"}
)
public class FoodfoodApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodfoodApplication.class, args);
    }

}