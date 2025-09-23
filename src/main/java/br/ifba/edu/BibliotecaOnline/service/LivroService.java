package br.ifba.edu.BibliotecaOnline.service;

import br.ifba.edu.BibliotecaOnline.DTO.LivroDTO;
import br.ifba.edu.BibliotecaOnline.entities.LivroEntity;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.excecao.AnoPublicacaoInvalidoException;
import br.ifba.edu.BibliotecaOnline.excecao.LivroDuplicadoException;
import br.ifba.edu.BibliotecaOnline.mapper.LivroMapper;
import br.ifba.edu.BibliotecaOnline.repository.LivroRepository;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final LivroMapper livroMapper;
    private final UsuarioRepository usuarioRepository;

    public void salvar(LivroDTO dto) {
     
        if (dto.getId() == null) {
            if (livroRepository.existsByNome(dto.getNome())) {
                throw new LivroDuplicadoException("Já existe um livro com esse nome");
            }
        } else {
            if (livroRepository.existsByNomeAndIdNot(dto.getNome(), dto.getId())) {
                throw new LivroDuplicadoException("Já existe um livro com esse nome");
            }
        }
        int anoAtual = LocalDate.now().getYear();
        if (dto.getAnoPublicacao() < 1500 || dto.getAnoPublicacao() > anoAtual) {
            throw new AnoPublicacaoInvalidoException("Ano de publicação inválido!");
        }

        LivroEntity entity = livroMapper.toEntity(dto);

        
        String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario admin = usuarioRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Admin não encontrado para o e-mail: " + adminEmail));

        entity.setPublicadoPor(admin);
        livroRepository.save(entity); // Envers audita esta ação (criação ou edição) automaticamente.
    }

    @Transactional
    public void deletar(Long id) {
        LivroEntity livroParaDeletar = livroRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Livro não encontrado para o ID: " + id));

        livroRepository.delete(livroParaDeletar);
    }

    public List<LivroDTO> listar() {
        return livroRepository.findAll().stream()
                .map(livroMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<LivroDTO> buscarPorNomeOuAutor(String query) {
        return livroRepository.findByNomeContainingIgnoreCaseOrAutorContainingIgnoreCase(query, query)
                .stream()
                .map(livroMapper::toDTO)
                .collect(Collectors.toList());
    }

    public LivroDTO buscarPorId(Long id) {
        return livroRepository.findById(id)
                .map(livroMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
    }
}