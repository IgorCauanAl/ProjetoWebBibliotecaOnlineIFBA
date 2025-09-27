package br.ifba.edu.BibliotecaOnline.service;

import br.ifba.edu.BibliotecaOnline.DTO.EditarDetalhesPerfilDTO;
import br.ifba.edu.BibliotecaOnline.DTO.MudarSenhaDTO;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
@AllArgsConstructor
public class ServicosUsuariosPerfilService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

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

    public void alterarDetalhes(EditarDetalhesPerfilDTO dto, UserDetails userDetails, HttpServletRequest request){
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Atualiza nome e e-mail
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuarioRepository.save(usuario);

        // Recarrega UserDetails atualizado pelo novo e-mail
        UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(usuario.getEmail());

        // Atualiza o SecurityContext com o novo UserDetails
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                updatedUserDetails,
                updatedUserDetails.getPassword(),
                updatedUserDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

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

    @Transactional
    public String atualizarFotoPerfil(Long usuarioId, MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("O arquivo enviado está vazio.");
            }

            // O ideal é pegar este caminho de um application.properties, como sugerido anteriormente
            Path pastaDeUpload = Paths.get("uploads/fotos-perfil/");
            if (!Files.exists(pastaDeUpload)) {
                Files.createDirectories(pastaDeUpload);
            }

            String nomeArquivo = "usuario-" + usuarioId + "-" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
            
            Files.copy(file.getInputStream(), pastaDeUpload.resolve(nomeArquivo), StandardCopyOption.REPLACE_EXISTING);

            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + usuarioId));
            
            String caminhoParaSalvar = "/uploads/fotos-perfil/" + nomeArquivo;

            usuario.setFotoPerfil(caminhoParaSalvar);
            usuarioRepository.save(usuario); // Agora o save está garantido pela transação

            return caminhoParaSalvar;

        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar a foto de perfil. Por favor, tente novamente.", e);
        }
    }


}
