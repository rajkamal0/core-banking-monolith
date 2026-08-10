package fintrack_monolith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FintrackMonolithApplication {
	
	private static final Logger logInfo = LoggerFactory.getLogger(FintrackMonolithApplication.class);

	public static void main(String[] args) {
		logInfo.info("Starting fintrack-monolith application");
		SpringApplication.run(FintrackMonolithApplication.class, args);
		logInfo.info("fintrack-monolith application started");
	}

}
