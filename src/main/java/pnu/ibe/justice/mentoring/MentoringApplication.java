package pnu.ibe.justice.mentoring;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;
import java.util.TimeZone;


@SpringBootApplication
public class MentoringApplication {

    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        Locale.setDefault(Locale.KOREA);
    }

    public static void main(final String[] args) {
        SpringApplication.run(MentoringApplication.class, args);
    }


}
