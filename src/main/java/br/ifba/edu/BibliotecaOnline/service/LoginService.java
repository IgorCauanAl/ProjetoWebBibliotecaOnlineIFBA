package br.ifba.edu.BibliotecaOnline.service;

import br.ifba.edu.BibliotecaOnline.DTO.LoginRequest;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.excecao.LoginIncorretoException;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    
    public Usuario authenticate (LoginRequest request){

        // Busca o usuário pelo e-mail
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(LoginIncorretoException::new);

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new LoginIncorretoException();
        }
        return usuario;
    }
}