package br.ifba.edu.BibliotecaOnline.service;

import br.ifba.edu.BibliotecaOnline.DTO.LivroDTO;
import br.ifba.edu.BibliotecaOnline.entities.Autor;
import br.ifba.edu.BibliotecaOnline.entities.LivroEntity;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.excecao.AnoPublicacaoInvalidoException;
import br.ifba.edu.BibliotecaOnline.excecao.LivroDuplicadoException;
import br.ifba.edu.BibliotecaOnline.mapper.LivroMapper;
import br.ifba.edu.BibliotecaOnline.repository.AutorRepository;
import br.ifba.edu.BibliotecaOnline.repository.LivroRepository;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final LivroMapper livroMapper;
    private final UsuarioRepository usuarioRepository;
    private final AutorRepository autorRepository; // Injetar o novo repositório

    @Transactional
    public void salvar(LivroDTO dto) {
        // Validação de livro duplicado
        if (dto.getId() == null) {
            if (livroRepository.existsByNome(dto.getNome())) {
                throw new LivroDuplicadoException("Já existe um livro com esse nome");
            }
        } else {
            if (livroRepository.existsByNomeAndIdNot(dto.getNome(), dto.getId())) {
                throw new LivroDuplicadoException("Já existe um livro com esse nome");
            }
        }
        
        // Validação do ano de publicação
        int anoAtual = LocalDate.now().getYear();
        if (dto.getAnoPublicacao() < 1500 || dto.getAnoPublicacao() > anoAtual) {
            throw new AnoPublicacaoInvalidoException("Ano de publicação inválido!");
        }

        LivroEntity entity = livroMapper.toEntity(dto);

        // Lógica para associar ou criar o autor
        Autor autor;
        if (dto.getAutorId() != null) {
            // Usa um autor existente
            autor = autorRepository.findById(dto.getAutorId())
                    .orElseThrow(() -> new RuntimeException("Autor não encontrado para o ID: " + dto.getAutorId()));
        } else {
            // Cria um novo autor
            if (dto.getNovoAutorNome() == null || dto.getNovoAutorNome().isBlank()) {
                throw new IllegalArgumentException("O nome do novo autor é obrigatório.");
            }
            // Verifica se um autor com o mesmo nome já existe
            autor = autorRepository.findByNomeAutorIgnoreCase(dto.getNovoAutorNome())
                    .orElseGet(() -> {
                        Autor novoAutor = new Autor(dto.getNovoAutorNome(), dto.getNovoAutorDescricao());
                        return autorRepository.save(novoAutor);
                    });
        }
        entity.setAutor(autor);

        // Associa o admin que publicou
        String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario admin = usuarioRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Admin não encontrado para o e-mail: " + adminEmail));
        entity.setPublicadoPor(admin);

        livroRepository.save(entity);
    }

    // O restante da classe LivroService (deletar, listar, etc.) permanece o mesmo...

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

    public Page<LivroDTO> listarPaginado(Pageable pageable) {
        Page<LivroEntity> paginaDeLivros = livroRepository.findAll(pageable);
        return paginaDeLivros.map(livroMapper::toDTO);
    }

    public Page<LivroDTO> buscarPorPalavraChave(String keyword, Pageable pageable) {
        // Ajuste na busca para pesquisar pelo nome do autor na entidade relacionada
        Page<LivroEntity> paginaDeLivros = livroRepository.findByNomeContainingIgnoreCaseOrAutorNomeAutorContainingIgnoreCase(keyword, keyword, pageable);
        return paginaDeLivros.map(livroMapper::toDTO);
    }

    public LivroDTO buscarPorId(Long id) {
        return livroRepository.findById(id)
                .map(livroMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado para o ID: " + id));
    }
}