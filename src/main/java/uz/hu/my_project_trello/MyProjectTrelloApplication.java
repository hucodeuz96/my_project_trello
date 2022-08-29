package uz.hu.my_project_trello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class MyProjectTrelloApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyProjectTrelloApplication.class, args);
	}
//	@Bean
//	CommandLineRunner runner(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
//		return args -> {
//
//			AuthRole managerRole = AuthRole.builder()
//					.name("MANAGER")
//					.build();
//
//			AuthRole adminRole = AuthRole.builder()
//					.name("ADMIN")
//					.authPermission(List.of(AuthPermission.builder().name("Create Manager").build()))
//					.build();
//
//			AuthRole moderatorRole = AuthRole.builder()
//					.name("MODERATOR")
//					.build();
//
//			roleRepository.saveAll(List.of(managerRole,adminRole,managerRole));
//
//
//			AuthUser admin = AuthUser.builder()
//					.fullName("Husniddin")
//					.email("h@mail.ru")
//					.password(passwordEncoder.encode("123"))
//					.role(List.of(adminRole, managerRole, moderatorRole))
//					.build();
//			userRepository.save(admin);
//
//		};
//
//	}


}



