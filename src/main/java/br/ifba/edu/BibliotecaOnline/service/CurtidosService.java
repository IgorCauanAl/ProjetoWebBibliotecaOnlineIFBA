package br.ifba.edu.BibliotecaOnline.service;

import br.ifba.edu.BibliotecaOnline.DTO.CurtidosDTO;
import br.ifba.edu.BibliotecaOnline.DTO.LivroDTO;
import br.ifba.edu.BibliotecaOnline.entities.LivroEntity;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.repository.LivroRepository;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CurtidosService {


    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;



    public Usuario getUsuarioLogado(Authentication authentication) {
        return usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public List<CurtidosDTO> listarCurtidos(Authentication authentication) {
        return getUsuarioLogado(authentication).getLivrosCurtidos()
                .stream()
                .map(livro -> {
                    CurtidosDTO dto = new CurtidosDTO();
                    dto.setId(livro.getId());
                    dto.setNome(livro.getNome());
                    dto.setCapaUrl(livro.getCapaUrl());
                    dto.setAutorNome(livro.getAutor() != null ? livro.getAutor().getNomeAutor() : "Desconhecido");
                    return dto;
                })
                .toList();
    }


    public void curtirLivro(Long livroId, Authentication authentication) {
        Usuario usuario = getUsuarioLogado(authentication);
        LivroEntity livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        if (!usuario.getLivrosCurtidos().contains(livro)) {
            usuario.getLivrosCurtidos().add(livro);
            usuarioRepository.save(usuario);
        }
    }

    public void descurtirLivro(Long livroId, Authentication authentication) {
        Usuario usuario = getUsuarioLogado(authentication);
        LivroEntity livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        usuario.getLivrosCurtidos().remove(livro);
        usuarioRepository.save(usuario);
    }
}

