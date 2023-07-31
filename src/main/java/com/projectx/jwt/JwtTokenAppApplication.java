package com.projectx.jwt;

import com.projectx.jwt.mysqldb.service.UserService;
import com.projectx.jwt.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.projectx.jwt.mysqldb.repository")
@SpringBootApplication
public class JwtTokenAppApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(JwtTokenAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
          userService.addDefaultUser(Constants.setAdminData());
	}
}
