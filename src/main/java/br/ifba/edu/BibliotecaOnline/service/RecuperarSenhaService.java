package br.ifba.edu.BibliotecaOnline.service;

import br.ifba.edu.BibliotecaOnline.DTO.CodigoDTO;
import br.ifba.edu.BibliotecaOnline.DTO.EmailRecuperacaoDTO;
import br.ifba.edu.BibliotecaOnline.DTO.SenhaNovaDTO;
import br.ifba.edu.BibliotecaOnline.entities.TokenSenha;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.excecao.EmailJaExisteException;
import br.ifba.edu.BibliotecaOnline.repository.TokenSenhaRepository;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor 
public class RecuperarSenhaService {

    
    private final UsuarioRepository usuarioRepository;
    private final TokenSenhaRepository tokenSenhaRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public void enviarCodigo(EmailRecuperacaoDTO emailDTO) {

        Usuario u = usuarioRepository.findByEmail(emailDTO.getEmail())
                .orElseThrow(() -> new EmailJaExisteException("Email informado não está cadastrado!"));

        //Gerar um código aleatorio
        String codigo = String.format("%06d", new Random().nextInt(1_000_000));


        Optional<TokenSenha> tokenExistenteOpt = tokenSenhaRepository.findByUsuario(u);
        
        TokenSenha tokenParaSalvar;

        if (tokenExistenteOpt.isPresent()) {
            tokenParaSalvar = tokenExistenteOpt.get();
            tokenParaSalvar.setCodigo(codigo);
            tokenParaSalvar.setExpiracao(LocalDateTime.now().plusMinutes(15));
        } else {
            tokenParaSalvar = new TokenSenha();
            tokenParaSalvar.setUsuario(u);
            tokenParaSalvar.setCodigo(codigo);
            tokenParaSalvar.setExpiracao(LocalDateTime.now().plusMinutes(15));
        }

        tokenSenhaRepository.save(tokenParaSalvar);

        try {
            emailService.enviarEmail(u.getEmail(), "Recuperação de senha",
                    "Seu código de senha é " + codigo + ". Ele expira em 15 minutos.");
        } catch (Exception e) {
            throw new RuntimeException("Falha ao enviar e-mail: " + e.getMessage(), e);
        }
    }

    public boolean receberCodigo(CodigoDTO codigoDto) {
        Optional<TokenSenha> token = tokenSenhaRepository.findByCodigo(codigoDto.getCodigo());
        if (token.isEmpty()) {
            return false;
        }
        TokenSenha t = token.get();
        if (t.getExpiracao().isBefore(LocalDateTime.now())) {
            return false;
        }
        return t.getCodigo().equals(codigoDto.getCodigo());
    }

    public void trocarSenha(SenhaNovaDTO senhaNovaDto) {
        TokenSenha t = tokenSenhaRepository.findByCodigo(senhaNovaDto.getCodigo())
                .orElseThrow(() -> new RuntimeException("Código não encontrado ou inválido"));

        if (t.getExpiracao().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Código com tempo expirado");
        }
        Usuario usuario = t.getUsuario();
        usuario.setSenha(passwordEncoder.encode(senhaNovaDto.getNovaSenha()));
        usuarioRepository.save(usuario);
        tokenSenhaRepository.delete(t);
    }
}