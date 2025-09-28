package br.ifba.edu.BibliotecaOnline.service;

import br.ifba.edu.BibliotecaOnline.DTO.AvaliacaoDTO;
import br.ifba.edu.BibliotecaOnline.DTO.CriarAvaliacaoDTO;
import br.ifba.edu.BibliotecaOnline.entities.AvaliacaoEntity;
import br.ifba.edu.BibliotecaOnline.entities.LivroEntity;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.mapper.AvaliacaoMapper;
import br.ifba.edu.BibliotecaOnline.repository.AvaliacaoRepository;
import br.ifba.edu.BibliotecaOnline.repository.LivroRepository;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;
    private final AvaliacaoMapper avaliacaoMapper;

    @Transactional
    public AvaliacaoDTO salvar(CriarAvaliacaoDTO dto, Long livroId, String usuarioEmail) {
        LivroEntity livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        Usuario usuario = usuarioRepository.findByEmail(usuarioEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Procurar a avaliação anterior do usuario
        Optional<AvaliacaoEntity> avaliacaoExistenteOpt = avaliacaoRepository.findByUsuarioAndLivro(usuario, livro);

        AvaliacaoEntity avaliacao;
        if (avaliacaoExistenteOpt.isPresent()) {
            // Se existir uma avaliação, ele atualiza
            avaliacao = avaliacaoExistenteOpt.get();
        } else {
            // Se nao existe, e criado uma nova instancia.
            avaliacao = new AvaliacaoEntity();
            avaliacao.setLivro(livro);
            avaliacao.setUsuario(usuario);
        }

        // Atualiza os valores da avaliacao
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setNota(dto.getNota());

        // Salva a entidade
        AvaliacaoEntity avaliacaoSalva = avaliacaoRepository.save(avaliacao);

        return avaliacaoMapper.toDTO(avaliacaoSalva);
    }

    public List<AvaliacaoDTO> buscarPorLivroId(Long livroId) {
        return avaliacaoRepository.findByLivroIdOrderByDataAvaliacaoDesc(livroId)
                .stream()
                .map(avaliacaoMapper::toDTO)
                .collect(Collectors.toList());
    }
}