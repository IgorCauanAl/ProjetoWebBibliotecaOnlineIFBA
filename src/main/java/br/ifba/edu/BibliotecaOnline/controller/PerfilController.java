package br.ifba.edu.BibliotecaOnline.controller;

import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import br.ifba.edu.BibliotecaOnline.service.CustomUserDetailsService;
import br.ifba.edu.BibliotecaOnline.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;


@Controller
@AllArgsConstructor
public class PerfilController {


    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;


    @GetMapping("/perfil")
    public String redirecionarPerfil() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (isAdmin) {
            return "redirect:/perfil-admin";
        } else {
            return "redirect:/perfil-usuario";
        }
    }


    @GetMapping("/perfil-admin")
    public String paginaPerfilAdmin(Model model) {
        // Pega o email do usuário logado
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        // Busca o usuário no banco
       Usuario usuario = usuarioService.buscarUsuarioLogado();
        // Adiciona o objeto usuário ao modelo
        model.addAttribute("usuario", usuario);
        return "perfil-admin";
    }

    @GetMapping("/perfil-usuario")
    public String paginaPerfilUsuario(Model model) {
        // Pega o email do usuário logado
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        // Busca o usuário no banco
        Usuario usuario = usuarioService.buscarUsuarioLogado();
        // Adiciona o objeto usuário ao modelo
        model.addAttribute("usuario", usuario);
        return "perfil-usuario";
    }

}
