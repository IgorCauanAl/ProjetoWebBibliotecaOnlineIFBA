package br.ifba.edu.BibliotecaOnline.service;

import br.ifba.edu.BibliotecaOnline.DTO.UsuarioCadastroDTO;
import br.ifba.edu.BibliotecaOnline.DTO.UsuarioRespostaDTO;
import br.ifba.edu.BibliotecaOnline.entities.Role;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.excecao.EmailJaExisteException;
import br.ifba.edu.BibliotecaOnline.repository.RoleRepository;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CadastroService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public CadastroService(UsuarioRepository usuarioRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioRespostaDTO insert(UsuarioCadastroDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EmailJaExisteException("E-mail já cadastrado!");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));


        // Verifica se existe algum usuário no banco de dados
        if (usuarioRepository.count() == 0) {
            // Se for o primeiro, será ADMIN
            Role adminRole = roleRepository.findByName("ADMIN").orElseThrow(() -> new RuntimeException("Role ADMIN não encontrada"));
            usuario.setRoles(Set.of(adminRole));
        } else {
            // Os demais serão USER
            Role userRole = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role USER não encontrada"));
            usuario.setRoles(Set.of(userRole));
        }

        Usuario novoUsuario = usuarioRepository.save(usuario);

        return new UsuarioRespostaDTO(novoUsuario.getId(), novoUsuario.getNome(), novoUsuario.getEmail());
    }
}