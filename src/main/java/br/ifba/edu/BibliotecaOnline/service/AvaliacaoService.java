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

    /**
     * Salva ou atualiza a avaliação de um usuário para um livro.
     * Implementa a regra de que o usuário pode ter apenas uma avaliação por livro.
     */
    @Transactional
    public AvaliacaoDTO salvar(CriarAvaliacaoDTO dto, Long livroId, String usuarioEmail) {
        LivroEntity livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        Usuario usuario = usuarioRepository.findByEmail(usuarioEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Busca se já existe uma avaliação do usuário para este livro.
        Optional<AvaliacaoEntity> avaliacaoExistenteOpt = avaliacaoRepository.findByUsuarioAndLivro(usuario, livro);

        AvaliacaoEntity avaliacao;
        if (avaliacaoExistenteOpt.isPresent()) {
            // Atualiza a existente (reforçando a regra de 1 comentário)
            avaliacao = avaliacaoExistenteOpt.get(); 
        } else {
            // Cria uma nova
            avaliacao = new AvaliacaoEntity();
            avaliacao.setLivro(livro);
            avaliacao.setUsuario(usuario);
        }

        // Atualiza os campos
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setNota(dto.getNota());
        
        // Salva a entidade
        AvaliacaoEntity avaliacaoSalva = avaliacaoRepository.save(avaliacao);

        return avaliacaoMapper.toDTO(avaliacaoSalva);
    }
    
    /**
     * NOVO: Atualiza uma avaliação existente.
     * Validação de segurança: o usuário logado deve ser o autor da avaliação.
     */
    @Transactional
    public AvaliacaoDTO atualizar(Long avaliacaoId, CriarAvaliacaoDTO dto, String usuarioEmail) {
        AvaliacaoEntity avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        // *** SEGURANÇA / VALIDAÇÃO DE PROPRIEDADE ***
        if (!avaliacao.getUsuario().getEmail().equals(usuarioEmail)) {
            throw new SecurityException("Acesso negado. O usuário não é o autor desta avaliação.");
        }

        // Atualiza os campos
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setNota(dto.getNota());
        // Opcional: atualiza a data para refletir a edição
        avaliacao.setDataAvaliacao(LocalDateTime.now());
        
        AvaliacaoEntity avaliacaoAtualizada = avaliacaoRepository.save(avaliacao);
        return avaliacaoMapper.toDTO(avaliacaoAtualizada);
    }


    /**
     * NOVO: Deleta uma avaliação.
     * Validação de segurança: o usuário logado deve ser o autor da avaliação.
     */
    @Transactional
    public void deletar(Long avaliacaoId, String usuarioEmail) {
        AvaliacaoEntity avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        // *** SEGURANÇA / VALIDAÇÃO DE PROPRIEDADE ***
        if (!avaliacao.getUsuario().getEmail().equals(usuarioEmail)) {
            throw new SecurityException("Acesso negado. O usuário não é o autor desta avaliação.");
        }

        avaliacaoRepository.delete(avaliacao);
    }

    /**
     * Busca todas as avaliações de um livro.
     */
    public List<AvaliacaoDTO> buscarPorLivroId(Long livroId) {
        return avaliacaoRepository.findByLivroIdOrderByDataAvaliacaoDesc(livroId)
                .stream()
                .map(avaliacaoMapper::toDTO)
                .collect(Collectors.toList());
    }
}