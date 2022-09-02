package io.emailradar.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("io.emailradar.commons,io.emailradar.bootstrap")
public class EmailDataBootstrapServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailDataBootstrapServiceApplication.class, args);
	}

}
