package br.ifba.edu.BibliotecaOnline.config;

import br.ifba.edu.BibliotecaOnline.entities.Role;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.repository.RoleRepository;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@Order(1) // <-- EXECUTAR EM PRIMEIRO LUGAR
public class UserDataInitializer {

    @Bean
    public CommandLineRunner initializeUsers(RoleRepository roleRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Criar Roles (cargos) se não existirem
            Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> roleRepository.save(new Role("ADMIN")));
            roleRepository.findByName("USER").orElseGet(() -> roleRepository.save(new Role("USER")));

            // 2. Criar o usuário administrador se nenhum usuário existir
            if (usuarioRepository.count() == 0) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setEmail("admin@biblioteca.com");
                // Lembre-se de criptografar a senha!
                admin.setSenha(passwordEncoder.encode("admin123")); 
                admin.setRoles(Set.of(adminRole));

                usuarioRepository.saveAndFlush(admin); 
            }
        };
    }
}