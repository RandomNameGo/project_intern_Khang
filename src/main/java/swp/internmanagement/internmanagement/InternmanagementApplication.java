package swp.internmanagement.internmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InternmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(InternmanagementApplication.class, args);
	}

}
