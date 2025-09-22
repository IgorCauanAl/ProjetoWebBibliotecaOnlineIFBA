package br.ifba.edu.BibliotecaOnline.service;

import br.ifba.edu.BibliotecaOnline.DTO.CodigoDTO;
import br.ifba.edu.BibliotecaOnline.DTO.EmailRecuperacaoDTO;
import br.ifba.edu.BibliotecaOnline.DTO.SenhaNovaDTO;
import br.ifba.edu.BibliotecaOnline.entities.TokenSenha;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.repository.TokenSenhaRepository;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class RecuperarSenhaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenSenhaRepository tokenSenhaRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void enviarCodigo (EmailRecuperacaoDTO emailDTO){
        //Verificar se o email existe
        Optional<Usuario> usuario = usuarioRepository.findByEmail(emailDTO.getEmail());

        if(usuario.isEmpty()){
            throw new RuntimeException("Email não encontrado");
        }

        //Extraindo usuario de optional para passar no set usuario
        Usuario u = usuario.get();

        //Criar o código de 6 digitos para ser enviado
        String codigo = String.format("%06d", new Random().nextInt((1_000_000)));

        //Atribuir o codigo ao atributo codigo e para o usuario da tabela token
        TokenSenha tokenSenha = new TokenSenha();
        tokenSenha.setCodigo(codigo);
        tokenSenha.setUsuario(u);
        tokenSenha.setExpiracao(LocalDateTime.now().plusMinutes(15));

        tokenSenhaRepository.save(tokenSenha);

        try {
            emailService.enviarEmail(u.getEmail(), "Recuperação de senha",
                    "Seu código de senha é " + codigo + ". Ele expira em 15 minutos.");
        } catch (Exception e) {
            throw new RuntimeException("Falha ao enviar e-mail: " + e.getMessage(), e);
        }



    }



    public boolean receberCodigo(CodigoDTO codigoDto){

        Optional<TokenSenha> token = tokenSenhaRepository.findByCodigo(codigoDto.getCodigo());

        //Se não existe retorna falso
        if(token.isEmpty()){
            return false;
        }

        //Extração do token senha do optional
        TokenSenha t = token.get();


        //Verifica a expiração
        if(t.getExpiracao().isBefore(LocalDateTime.now())){
            return false;
        }

        if(!t.getCodigo().equals(codigoDto.getCodigo())){
            return false;
        }

        return true;



    }

    public void trocarSenha(SenhaNovaDTO senhaNovaDto){

        Optional<TokenSenha> token = tokenSenhaRepository.findByCodigo(senhaNovaDto.getCodigo());

        //Código não encontrado
        if(token.isEmpty()){
            throw new RuntimeException("Código não encontrado");
        }

        //Extraindo token no optional
        TokenSenha t = token.get();

        //Verificar a expiração de 15 minutos
        if(t.getExpiracao().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Código com tempo expirado");
        }

        //Pega o usuario
        Usuario usuario = token.get().getUsuario();

        //Atualizar a senha
        usuario.setSenha(passwordEncoder.encode(senhaNovaDto.getNovaSenha()));
        usuarioRepository.save(usuario);

        tokenSenhaRepository.delete(t);





    }
}
