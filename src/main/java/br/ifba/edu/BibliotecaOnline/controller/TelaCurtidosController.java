package br.ifba.edu.BibliotecaOnline.controller;

import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import br.ifba.edu.BibliotecaOnline.service.CurtidosService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class TelaCurtidosController {

    private final CurtidosService curtidosService;



    @GetMapping("/curtidos")
    public String curtidosPage(Authentication authentication, Model model) {
        model.addAttribute("usuario", curtidosService.getUsuarioLogado(authentication));
        return "curtidos";
    }
}
