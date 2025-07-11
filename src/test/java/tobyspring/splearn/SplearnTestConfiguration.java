package tobyspring.splearn;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.PasswordEncoder;

@TestConfiguration
public class SplearnTestConfiguration {
    @Bean
    public tobyspring.splearn.application.required.EmailSender emailSender() {
        return (email, subject, body) -> System.out.println("Sending email: " + email);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return MemberFixture.createPasswordEncoder();
    }
}
