package play;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class PlayDbLocksApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlayDbLocksApplication.class, args);
	}

}
