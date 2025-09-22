package br.ifba.edu.BibliotecaOnline.config;

import br.ifba.edu.BibliotecaOnline.entities.Role;
import br.ifba.edu.BibliotecaOnline.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration


public class DateRoleInitialize {
    @Bean
    public CommandLineRunner initializeRoles(RoleRepository roleRepository){
        return data -> {
            if(roleRepository.findByName("ADMIN").isEmpty()){
                roleRepository.save(new Role("ADMIN"));
            }

             if(roleRepository.findByName("USER").isEmpty()){
                roleRepository.save(new Role("USER"));
            }

        };
    }

}
