package br.ifba.edu.BibliotecaOnline.service;

import br.ifba.edu.BibliotecaOnline.entities.Role;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.entities.UsuarioExcluidoLog;
import br.ifba.edu.BibliotecaOnline.repository.RoleRepository;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioExcluidoLogRepository;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final UsuarioExcluidoLogRepository logRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public void promoverParaAdmin(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para o ID: " + usuarioId));

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Role ADMIN não encontrada."));

        usuario.getRoles().add(adminRole);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void deletarUsuario(Long usuarioId) {
        String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        
        
        Usuario adminLogado = usuarioRepository.findByEmail(adminEmail).orElseThrow(() -> new RuntimeException("Admin não encontrado"));
        if (adminLogado.getId().equals(usuarioId)) {
            throw new IllegalStateException("Um administrador não pode deletar a própria conta.");
        }

        Usuario usuarioParaDeletar = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para o ID: " + usuarioId));

        
        UsuarioExcluidoLog logEntry = new UsuarioExcluidoLog(
                usuarioParaDeletar.getId(),
                usuarioParaDeletar.getNome(),
                usuarioParaDeletar.getEmail(),
                adminEmail
        );
        logRepository.save(logEntry);

    
        usuarioRepository.delete(usuarioParaDeletar);
    }
}
