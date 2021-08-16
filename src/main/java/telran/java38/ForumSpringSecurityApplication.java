package telran.java38;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import telran.java38.user.dao.AccountRepository;
import telran.java38.user.model.UserProfile;

@SpringBootApplication
public class ForumSpringSecurityApplication implements CommandLineRunner {

	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ForumSpringSecurityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (!accountRepository.existsById("admin")) {
			String hashPassword = passwordEncoder.encode("admin");
			UserProfile admin = new UserProfile("admin", hashPassword, "", "", LocalDate.now().plusYears(25));
			admin.addRole("Moderator");
			admin.addRole("Administrator");
			accountRepository.save(admin);
		}

	}

}
