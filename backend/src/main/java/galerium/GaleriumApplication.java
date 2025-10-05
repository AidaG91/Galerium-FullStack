package galerium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GaleriumApplication {

	public static void main(String[] args) {
		SpringApplication.run(GaleriumApplication.class, args);
	}

}
