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

import java.util.List;
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

        AvaliacaoEntity avaliacao = new AvaliacaoEntity();
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setNota(dto.getNota());
        avaliacao.setLivro(livro);
        avaliacao.setUsuario(usuario);

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