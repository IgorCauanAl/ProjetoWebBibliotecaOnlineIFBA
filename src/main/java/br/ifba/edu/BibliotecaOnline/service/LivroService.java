package br.ifba.edu.BibliotecaOnline.service;

import br.ifba.edu.BibliotecaOnline.DTO.LivroDTO;
import br.ifba.edu.BibliotecaOnline.entities.Autor;
import br.ifba.edu.BibliotecaOnline.entities.LivroEntity;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.excecao.AnoPublicacaoInvalidoException;
import br.ifba.edu.BibliotecaOnline.excecao.LivroDuplicadoException;
import br.ifba.edu.BibliotecaOnline.mapper.LivroMapper;
import br.ifba.edu.BibliotecaOnline.model.GeneroEnum;
import br.ifba.edu.BibliotecaOnline.repository.AutorRepository;
import br.ifba.edu.BibliotecaOnline.repository.LivroRepository;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final UsuarioRepository usuarioRepository;
    private final LivroMapper livroMapper;

    @Transactional
    public LivroDTO salvar(LivroDTO livroDTO) {
        if (livroDTO.getAnoPublicacao() == null || livroDTO.getAnoPublicacao() > LocalDate.now().getYear()) {
            throw new AnoPublicacaoInvalidoException("Ano de publicação inválido.");
        }

        if (livroDTO.getId() == null) {
            if (livroRepository.existsByNome(livroDTO.getNome())) {
                throw new LivroDuplicadoException("Já existe um livro com o mesmo nome.");
            }
        } else {
            if (livroRepository.existsByNomeAndIdNot(livroDTO.getNome(), livroDTO.getId())) {
                throw new LivroDuplicadoException("Já existe um livro com o mesmo nome.");
            }
        }
    
        LivroEntity livro = livroMapper.toEntity(livroDTO);
        
        Autor autor;
        if (livroDTO.getNovoAutorNome() != null && !livroDTO.getNovoAutorNome().isBlank()) {
            Autor novoAutor = new Autor();
            novoAutor.setNomeAutor(livroDTO.getNovoAutorNome());
            novoAutor.setDescricaoDoAutor(livroDTO.getNovoAutorDescricao());
            novoAutor.setFotoAutor(livroDTO.getNovoAutorFotoUrl()); 
            autor = autorRepository.save(novoAutor);
        } else if (livroDTO.getAutorId() != null) {
            autor = autorRepository.findById(livroDTO.getAutorId())
                    .orElseThrow(() -> new RuntimeException("Autor não encontrado para o ID: " + livroDTO.getAutorId()));
        } else {
            throw new RuntimeException("Autor não fornecido para o livro.");
        }

        livro.setAutor(autor);
    
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario publicadoPor = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
        livro.setPublicadoPor(publicadoPor);
    
        LivroEntity livroSalvo = livroRepository.save(livro);
        return livroMapper.toDTO(livroSalvo);
    }

    public void deletar(Long id) {
        LivroEntity livroParaDeletar = livroRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Livro não encontrado para o ID: " + id));
        livroRepository.delete(livroParaDeletar);
    }

    public List<LivroDTO> listar() {
        Set<Long> curtidosIds = getLivrosCurtidosIds();
        List<LivroEntity> todosOsLivros = livroRepository.findAll();
        return todosOsLivros.stream()
                .map(livro -> mapToDtoWithLikeStatus(livro, curtidosIds))
                .collect(Collectors.toList());
    }

    public Page<LivroDTO> listarPaginado(Pageable pageable) {
        Set<Long> curtidosIds = getLivrosCurtidosIds();
        Page<LivroEntity> paginaDeLivros = livroRepository.findAll(pageable);
        return paginaDeLivros.map(livro -> mapToDtoWithLikeStatus(livro, curtidosIds));
    }

    public Page<LivroDTO> buscarPorPalavraChave(String keyword, Pageable pageable) {
        Set<Long> curtidosIds = getLivrosCurtidosIds();
        Page<LivroEntity> paginaDeLivros;

        Optional<GeneroEnum> genero = Stream.of(GeneroEnum.values())
                .filter(g -> g.toString().equalsIgnoreCase(keyword))
                .findFirst();

        if (genero.isPresent()) {
            paginaDeLivros = livroRepository.findByGenero(genero.get(), pageable);
        } else {
            paginaDeLivros = livroRepository.findByNomeContainingIgnoreCaseOrAutorNomeAutorContainingIgnoreCase(keyword, keyword, pageable);
        }
        return paginaDeLivros.map(livro -> mapToDtoWithLikeStatus(livro, curtidosIds));
    }

    public LivroDTO buscarPorId(Long id) {
        Set<Long> curtidosIds = getLivrosCurtidosIds();
        return livroRepository.findById(id)
                .map(livro -> mapToDtoWithLikeStatus(livro, curtidosIds))
                .orElseThrow(() -> new RuntimeException("Livro não encontrado para o ID: " + id));
    }
    
    public List<LivroDTO> listarPorGenero(GeneroEnum genero) {
        Set<Long> curtidosIds = getLivrosCurtidosIds();
        List<LivroEntity> livrosPorGenero = livroRepository.findByGenero(genero);
        return livrosPorGenero.stream()
                .map(livro -> mapToDtoWithLikeStatus(livro, curtidosIds))
                .collect(Collectors.toList());
    }

    public Page<LivroDTO> buscarPorGenero(GeneroEnum genero, Pageable pageable) {
        Set<Long> curtidosIds = getLivrosCurtidosIds();
        Page<LivroEntity> paginaDeLivros = livroRepository.findByGenero(genero, pageable);
        return paginaDeLivros.map(livro -> mapToDtoWithLikeStatus(livro, curtidosIds));
    }

    public List<LivroDTO> listarMaisRecentes() {
        Set<Long> curtidosIds = getLivrosCurtidosIds();
        List<LivroEntity> livrosRecentes = livroRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        if (livrosRecentes.size() > 5) {
            livrosRecentes = livrosRecentes.subList(0, 5);
        }
        return livrosRecentes.stream()
                .map(livro -> mapToDtoWithLikeStatus(livro, curtidosIds))
                .collect(Collectors.toList());
    }

    public Page<LivroDTO> buscarPorAutorId(Long autorId, Pageable pageable) {
        Set<Long> curtidosIds = getLivrosCurtidosIds();
        Page<LivroEntity> paginaDeLivros = livroRepository.findByAutorId(autorId, pageable);
        return paginaDeLivros.map(livro -> mapToDtoWithLikeStatus(livro, curtidosIds));
    }

    private Set<Long> getLivrosCurtidosIds() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return Collections.emptySet();
        }
        
        return usuarioRepository.findByEmail(authentication.getName())
                .map(Usuario::getLivrosCurtidos)
                .orElse(Collections.emptyList())
                .stream()
                .map(LivroEntity::getId)
                .collect(Collectors.toSet());
    }

    private LivroDTO mapToDtoWithLikeStatus(LivroEntity livro, Set<Long> curtidosIds) {
        LivroDTO dto = livroMapper.toDTO(livro);
        dto.setCurtidoPeloUsuario(curtidosIds.contains(livro.getId()));
        return dto;
    }
}