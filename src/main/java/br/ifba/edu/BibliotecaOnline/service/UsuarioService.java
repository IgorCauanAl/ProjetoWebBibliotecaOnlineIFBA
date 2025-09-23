package br.ifba.edu.BibliotecaOnline.service;

import br.ifba.edu.BibliotecaOnline.entities.Role;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.repository.RoleRepository;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public void promoverParaAdmin(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para o ID: " + usuarioId));

        boolean jaEAdmin = usuario.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));

        if (jaEAdmin) {
            return;
        }

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Role ADMIN não encontrada."));

        usuario.getRoles().add(adminRole);
        usuarioRepository.save(usuario);
        // O Envers audita esta operação de 'save' automaticamente
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
        
        boolean ehAdmin = usuarioParaDeletar.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));
        
        if (ehAdmin) {
            throw new IllegalStateException("Não é permitido deletar outro administrador.");
        }
        

        usuarioRepository.delete(usuarioParaDeletar);
    }

    public Page<Usuario> listarTodosPaginado(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    public Page<Usuario> buscarPorPalavraChave(String palavraChave, Pageable pageable) {
        return usuarioRepository.findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(palavraChave, palavraChave, pageable);
    }
}