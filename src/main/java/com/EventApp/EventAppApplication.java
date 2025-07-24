package com.EventApp;

import com.EventApp.model.User;
import com.EventApp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EventAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventAppApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// Optional: clear all users first (for development/testing only)
			userRepository.deleteAll();

			User user = new User();
			user.setUsername("test");
			user.setEmail("oren@gmail.com");
			user.setPassword(passwordEncoder.encode("1234"));
			user.setRole("ROLE_USER");
			userRepository.save(user);

			User admin = new User();
			admin.setUsername("admin");
			admin.setEmail("admin@gmail.com");
			admin.setPassword(passwordEncoder.encode("admin123"));
			admin.setRole("ROLE_ADMIN");
			userRepository.save(admin);

			System.out.println("Default users inserted.");

		};
	}

}
