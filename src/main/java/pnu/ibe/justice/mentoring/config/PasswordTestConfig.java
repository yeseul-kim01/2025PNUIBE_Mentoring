package pnu.ibe.justice.mentoring.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordTestConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner testPasswordEncoder(PasswordEncoder passwordEncoder) {
        return args -> {
            String rawPassword = "pnuibeadmin+";
            String encodedPassword = passwordEncoder.encode(rawPassword);
            System.out.println("원본 비밀번호: " + rawPassword);
            System.out.println("암호화된 비밀번호: " + encodedPassword);
        };
    }
}

