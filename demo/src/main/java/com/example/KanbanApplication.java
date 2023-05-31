package com.example;

import com.example.security.auth.AuthenticationService;
import com.example.security.auth.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import static com.example.model.Role.ADMIN;
import static com.example.model.Role.MANAGER;

@SpringBootApplication
public class KanbanApplication {

    public static void main(String[] args) {
        SpringApplication.run(KanbanApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(
//            AuthenticationService service
//    ) {
//        return args -> {
//            var admin = RegisterRequest.builder()
//                    .firstname("Admin4")
//                    .lastname("Admin4")
//                    .email("admin4@mail.com")
//                    .password("password4")
//                    .role(ADMIN)
//                    .build();
//            System.out.println("Admin token: " + service.register(admin).getAccessToken());

////            var manager = RegisterRequest.builder()
////                    .firstname("Admin")
////                    .lastname("Admin")
////                    .email("manager@mail.com")
////                    .password("password")
////                    .role(MANAGER)
////                    .build();
////            System.out.println("Manager token: " + service.register(manager).getAccessToken());
//
//        };
//   }
}