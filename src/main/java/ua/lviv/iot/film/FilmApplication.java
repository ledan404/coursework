package ua.lviv.iot.film;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class FilmApplication {

    public static void main(final String[] args) {
        SpringApplication.run(FilmApplication.class, args);
    }
}
