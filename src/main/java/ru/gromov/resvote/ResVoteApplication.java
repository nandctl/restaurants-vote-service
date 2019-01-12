package ru.gromov.resvote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ResVoteApplication {
	public static void main(String[] args) {
		SpringApplication.run(ResVoteApplication.class, args);
	}
}

