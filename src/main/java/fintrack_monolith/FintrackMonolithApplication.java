package fintrack_monolith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FintrackMonolithApplication {

	public static void main(String[] args) {
		SpringApplication.run(FintrackMonolithApplication.class, args);
	}

}
