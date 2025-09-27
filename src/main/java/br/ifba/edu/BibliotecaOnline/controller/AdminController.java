package br.ifba.edu.BibliotecaOnline.controller;

import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UsuarioService usuarioService;


    @PostMapping("/usuarios/promover/{id}")
    public String promoverUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.promoverParaAdmin(id);
        redirectAttributes.addFlashAttribute("sucesso", "Usuário promovido a administrador com sucesso!");
        return "redirect:/admin/gerenciar-usuarios";
    }

    @PostMapping("/usuarios/deletar/{id}")
    public String deletarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deletarUsuario(id);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário deletado com sucesso!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin/gerenciar-usuarios";
    }


    // Dentro da classe AdminController
    @GetMapping("/gerenciar-usuarios")
    public String exibirPaginaGerenciarUsuarios(Model model,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(required = false) String keyword) { // Parâmetro de busca
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Usuario> paginaDeUsuarios;

        if (keyword != null && !keyword.isEmpty()) {
            // Se há uma palavra-chave, faz a busca
            paginaDeUsuarios = usuarioService.buscarPorPalavraChave(keyword, pageable);
            model.addAttribute("keyword", keyword); // Devolve a palavra-chave para a view
        } else {
            paginaDeUsuarios = usuarioService.listarTodosPaginado(pageable);
        }
        
        model.addAttribute("paginaDeUsuarios", paginaDeUsuarios);
        
        return "admin/gerenciar-usuarios";
    }
}
