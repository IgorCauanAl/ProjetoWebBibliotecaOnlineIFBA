package br.ifba.edu.BibliotecaOnline.service;

import br.ifba.edu.BibliotecaOnline.DTO.MudarSenhaDTO;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ServiçosUsuariosPerfilService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public void alterarSenha (MudarSenhaDTO dto){
        //Authenticação do usuário
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // username
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));


        //Senha antiga é valida?
        if(!passwordEncoder.matches(dto.getSenhaAntiga(), usuario.getSenha())){
            throw new BadCredentialsException("Senha antiga incorreta");
        }

        //Senha nova é igual a antiga?
        if(passwordEncoder.matches(dto.getSenhaNova(), usuario.getSenha())){
            throw new IllegalArgumentException("Senha nova não pode ser igual a antiga!");
        }


        //Mudança da senha
        usuario.setSenha(passwordEncoder.encode(dto.getSenhaNova()));
        usuarioRepository.save(usuario);


    }

    @Transactional
    public void apagarConta(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuarioRepository.delete(usuario);
        SecurityContextHolder.clearContext();

    }

}
