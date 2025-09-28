package br.ifba.edu.BibliotecaOnline.controller;

import br.ifba.edu.BibliotecaOnline.DTO.CurtidosDTO;
import br.ifba.edu.BibliotecaOnline.DTO.LivroDTO;
import br.ifba.edu.BibliotecaOnline.entities.Autor;
import br.ifba.edu.BibliotecaOnline.model.GeneroEnum;
import br.ifba.edu.BibliotecaOnline.service.AutorService;
import br.ifba.edu.BibliotecaOnline.service.CurtidosService;
import br.ifba.edu.BibliotecaOnline.service.LivroService;
import br.ifba.edu.BibliotecaOnline.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import java.util.NoSuchElementException;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class HomeController {

    private final LivroService livroService;
    private final AutorService autorService;
    private final FileStorageService fileStorageService;
    private final CurtidosService curtidosService;

    @GetMapping({"/", "/home"})
    public String exibirHome(
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        final int TAMANHO_PAGINA = 10;
        
        if (query != null && !query.isBlank()) {
            
            Optional<Autor> autorEncontrado = autorService.buscarPorNomeIgnoreCase(query.trim());
            
            if (autorEncontrado.isPresent()) {
                return "redirect:/autor/" + autorEncontrado.get().getId();
            }

            Pageable pageable = PageRequest.of(page, TAMANHO_PAGINA, Sort.by("nome"));
            Page<LivroDTO> resultados = livroService.buscarPorPalavraChave(query, pageable);
            
            model.addAttribute("resultadosPesquisa", resultados);
            model.addAttribute("termoBusca", query);
            model.addAttribute("isSearch", true);
            model.addAttribute("isGeneroSearch", false);
            model.addAttribute("isAutorSearch", false);

        } else {
            
            model.addAttribute("livrosEmAlta", livroService.listar());
            model.addAttribute("livrosRecentes", livroService.listarMaisRecentes());
            model.addAttribute("livrosTerror", livroService.listarPorGenero(GeneroEnum.TERROR));
            model.addAttribute("livrosRomance", livroService.listarPorGenero(GeneroEnum.ROMANCE));
            model.addAttribute("isSearch", false);
            model.addAttribute("isGeneroSearch", false);
            model.addAttribute("isAutorSearch", false);
        }

        return "home";
    }

    @GetMapping("/genero/{genero}")
    public String exibirLivrosPorGenero(
            @PathVariable("genero") GeneroEnum genero,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        final int TAMANHO_PAGINA = 10;
        Pageable pageable = PageRequest.of(page, TAMANHO_PAGINA, Sort.by("nome"));

        Page<LivroDTO> resultados = livroService.buscarPorGenero(genero, pageable);
        
        model.addAttribute("resultadosGenero", resultados);
        model.addAttribute("genero", genero.name());
        model.addAttribute("isGeneroSearch", true);
        model.addAttribute("isSearch", false);
        model.addAttribute("isAutorSearch", false);

        return "home";
    }
    
    @GetMapping("/autor/{id}")
    public String exibirLivrosPorAutor(
            @PathVariable("id") Long autorId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        final int TAMANHO_PAGINA = 8;
        Pageable pageable = PageRequest.of(page, TAMANHO_PAGINA, Sort.by("nome"));

        try {
            Page<LivroDTO> resultados = livroService.buscarPorAutorId(autorId, pageable);
            Autor autor = autorService.buscarPorId(autorId);

            model.addAttribute("resultadosAutor", resultados);
            model.addAttribute("autorNome", autor.getNomeAutor());
            model.addAttribute("autorFotoUrl", autor.getFotoAutor());
            model.addAttribute("autorDescricao", autor.getDescricaoDoAutor());
            model.addAttribute("autorId", autorId);
            
            model.addAttribute("isAutorSearch", true);
            model.addAttribute("isSearch", false);
            model.addAttribute("isGeneroSearch", false);

        } catch (NoSuchElementException e) {
            return "redirect:/home";
        }

        return "home";
    }

    @GetMapping("/livros/{id}")
    public String detalhesLivro(@PathVariable("id") Long id, Model model) {
        try {
            LivroDTO livro = livroService.buscarPorId(id);
            Autor autor = autorService.buscarPorId(livro.getAutorId()); 

            model.addAttribute("livro", livro);
            model.addAttribute("autor", autor); 

            return "detalhes-livro";
        } catch (RuntimeException e) {
            return "redirect:/home";
        }
    }

    @GetMapping("/livros/download/{id}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable Long id) {
        try {
            return fileStorageService.downloadPdf(id);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/curtidos")
    public String viewCurtidosPage(Model model, Authentication authentication,
                                     @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        if (authentication == null) {
            return "redirect:/login";
        }
        Page<CurtidosDTO> paginaDeLivrosCurtidos = curtidosService.listarCurtidos(authentication, pageable);
        model.addAttribute("paginaDeLivrosCurtidos", paginaDeLivrosCurtidos);
        return "curtidos"; 
    }

}