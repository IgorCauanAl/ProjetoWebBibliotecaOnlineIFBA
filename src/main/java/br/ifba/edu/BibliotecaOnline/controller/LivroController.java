package br.ifba.edu.BibliotecaOnline.controller;

import br.ifba.edu.BibliotecaOnline.DTO.LivroDTO;
import br.ifba.edu.BibliotecaOnline.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/admin/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;

    @GetMapping
    public String exibirPaginaGerenciarLivros(Model model,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(required = false) String keyword) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<LivroDTO> paginaDeLivros;

        if (keyword != null && !keyword.isEmpty()) {
            paginaDeLivros = livroService.buscarPorPalavraChave(keyword, pageable);
            model.addAttribute("keyword", keyword);
        } else {
            paginaDeLivros = livroService.listarPaginado(pageable);
        }
        
        model.addAttribute("paginaDeLivros", paginaDeLivros);
        
        return "admin/gerenciar-livros";
    }

    @GetMapping("/novo")
    public String exibirFormularioNovo(Model model) {
        model.addAttribute("livroDTO", new LivroDTO());
        return "admin/publicar-livro";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEditar(@PathVariable Long id, Model model) {
        LivroDTO livroDTO = livroService.buscarPorId(id);
        model.addAttribute("livroDTO", livroDTO);
        return "admin/publicar-livro";
    }

    @PostMapping("/salvar")
    public String salvarLivro(
            @Valid @ModelAttribute("livroDTO") LivroDTO livroDTO,
            BindingResult result,
            @RequestParam("capaFile") MultipartFile capaFile,
            @RequestParam("pdfFile") MultipartFile pdfFile,
            Model model) {

        if (result.hasErrors()) {
            return "admin/publicar-livro";
        }

        if (livroDTO.getId() == null) {
            if (capaFile.isEmpty()) {
                result.rejectValue("capaUrl", "error.capaUrl", "A imagem da capa é obrigatória.");
            }
            if (pdfFile.isEmpty()) {
                result.rejectValue("pdfUrl", "error.pdfUrl", "O arquivo PDF do livro é obrigatório.");
            }
            if(result.hasErrors()){
                 return "admin/publicar-livro";
            }
        }

        try {
            if (capaFile != null && !capaFile.isEmpty()) {
                String capaUrl = salvarArquivo(capaFile, "uploads/imgs");
                livroDTO.setCapaUrl(capaUrl);
            }

            if (pdfFile != null && !pdfFile.isEmpty()) {
                String pdfUrl = salvarArquivo(pdfFile, "uploads/pdfs");
                livroDTO.setPdfUrl(pdfUrl);
            }

            livroService.salvar(livroDTO);
            return "redirect:/perfil-admin";

        } catch (IOException e) {
            // IOException ainda precisa ser tratada aqui pois é específica do upload de arquivos
            model.addAttribute("erro", "Erro ao salvar arquivo: " + e.getMessage());
            return "admin/publicar-livro";
        }
        // LivroDuplicadoException e AnoPublicacaoInvalidoException agora são tratadas pelo @ControllerAdvice
    }

    @GetMapping("/deletar/{id}")
    public String deletarLivro(@PathVariable Long id) {
        livroService.deletar(id);
        return "redirect:/admin/livros";
    }
    
    
     
    private String salvarArquivo(MultipartFile arquivo, String diretorio) throws IOException {
        String nomeOriginal = arquivo.getOriginalFilename();
        if (nomeOriginal == null) {
            // Garante que não teremos um NullPointerException se o nome do arquivo for nulo
            nomeOriginal = "arquivo_sem_nome_" + UUID.randomUUID();
        }
        
        String nomeArquivo = UUID.randomUUID() + "_" + nomeOriginal;
        Path caminhoCompleto = Paths.get(diretorio, nomeArquivo);
        Files.createDirectories(caminhoCompleto.getParent());
        Files.copy(arquivo.getInputStream(), caminhoCompleto, StandardCopyOption.REPLACE_EXISTING);
        
        return "/" + diretorio + "/" + nomeArquivo;
    }
}