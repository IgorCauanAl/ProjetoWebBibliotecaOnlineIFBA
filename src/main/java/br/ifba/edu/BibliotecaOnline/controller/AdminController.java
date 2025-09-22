package br.ifba.edu.BibliotecaOnline.controller;

import br.ifba.edu.BibliotecaOnline.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UsuarioService usuarioService;

    @GetMapping("/gerenciar-usuarios")
    public String exibirPaginaGerenciarUsuarios(Model model) {
        String emailAdminLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        
        // Adiciona a lista de usuários ao modelo, excluindo o próprio admin logado
        model.addAttribute("usuarios", usuarioService.listarTodos().stream()
                .filter(u -> !u.getEmail().equals(emailAdminLogado))
                .collect(Collectors.toList()));
        
        return "admin/gerenciar-usuarios";
    }

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
}