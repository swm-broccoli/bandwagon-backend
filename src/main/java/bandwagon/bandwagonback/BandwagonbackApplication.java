package bandwagon.bandwagonback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BandwagonbackApplication {

	public static void main(String[] args) {
		SpringApplication.run(BandwagonbackApplication.class, args);
	}

}
