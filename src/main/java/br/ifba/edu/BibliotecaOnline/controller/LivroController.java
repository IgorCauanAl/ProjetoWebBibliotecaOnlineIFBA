package br.ifba.edu.BibliotecaOnline.controller;

import br.ifba.edu.BibliotecaOnline.DTO.LivroDTO;
import br.ifba.edu.BibliotecaOnline.excecao.AnoPublicacaoInvalidoException;
import br.ifba.edu.BibliotecaOnline.excecao.AutorExistenteException;
import br.ifba.edu.BibliotecaOnline.service.AutorService;
import br.ifba.edu.BibliotecaOnline.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final AutorService autorService;

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

    private void carregarAutoresNoModelo(Model model) {
        model.addAttribute("todosAutores", autorService.listarTodos());
    }

    @GetMapping("/novo")
    public String exibirFormularioNovo(Model model) {
        model.addAttribute("livroDTO", new LivroDTO());
        carregarAutoresNoModelo(model);
        return "admin/publicar-livro";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEditar(@PathVariable Long id, Model model) {
        LivroDTO livroDTO = livroService.buscarPorId(id);
        model.addAttribute("livroDTO", livroDTO);
        carregarAutoresNoModelo(model);
        return "admin/publicar-livro";
    }

   @PostMapping("/salvar")
    public String salvarLivro(
            @Valid @ModelAttribute("livroDTO") LivroDTO livroDTO,
            BindingResult result,
            @RequestParam("capaFile") MultipartFile capaFile,
            @RequestParam("pdfFile") MultipartFile pdfFile,
            @RequestParam(name = "novoAutorFoto", required = false) MultipartFile novoAutorFoto,
            Model model) {

       
        boolean isNovoAutor = "novo".equals(livroDTO.getTipoAutor());

        if (isNovoAutor) {
            if (livroDTO.getNovoAutorNome() == null || livroDTO.getNovoAutorNome().isBlank()) {
                result.rejectValue("novoAutorNome", "error.novoAutorNome", "O nome do novo autor é obrigatório.");
            }
            if (livroDTO.getNovoAutorDescricao() == null || livroDTO.getNovoAutorDescricao().isBlank()) {
                result.rejectValue("novoAutorDescricao", "error.novoAutorDescricao", "A descrição do novo autor é obrigatória.");
            }
        } else { 
            if (livroDTO.getAutorId() == null) {
                result.rejectValue("autorId", "error.autorId", "Selecione um autor existente.");
            }
        }
        
        boolean isEdicao = livroDTO.getId() != null;
        if (!isEdicao) { 
            if (capaFile.isEmpty()) {
                result.rejectValue("capaUrl", "error.capaUrl", "A imagem da capa é obrigatória.");
            }
            if (pdfFile.isEmpty()) {
                result.rejectValue("pdfUrl", "error.pdfUrl", "O arquivo PDF do livro é obrigatório.");
            }
        }
        
        if (result.hasErrors()) {
            carregarAutoresNoModelo(model);
            if (isEdicao) { 
                LivroDTO original = livroService.buscarPorId(livroDTO.getId());
                livroDTO.setCapaUrl(original.getCapaUrl());
                livroDTO.setPdfUrl(original.getPdfUrl());
            }
            return "admin/publicar-livro";
        }

        try {
            if (capaFile != null && !capaFile.isEmpty()) {
                livroDTO.setCapaUrl(salvarArquivo(capaFile, "uploads/imgs"));
            }
            if (pdfFile != null && !pdfFile.isEmpty()) {
                livroDTO.setPdfUrl(salvarArquivo(pdfFile, "uploads/pdfs"));
            }
            if (novoAutorFoto != null && !novoAutorFoto.isEmpty()) {
                livroDTO.setNovoAutorFotoUrl(salvarArquivo(novoAutorFoto, "uploads/autores"));
            }

            livroService.salvar(livroDTO);
            return "redirect:/admin/livros";

        } catch (AnoPublicacaoInvalidoException e) {
            result.rejectValue("anoPublicacao", "error.anoPublicacao", e.getMessage());
            carregarAutoresNoModelo(model);
            return "admin/publicar-livro";

        } catch (AutorExistenteException e) {
            
            result.rejectValue("novoAutorNome", "error.novoAutorNome", e.getMessage());
            carregarAutoresNoModelo(model);
            return "admin/publicar-livro";
        } catch (Exception e) {
            
            model.addAttribute("erroGeral", "Ocorreu um erro inesperado: " + e.getMessage());
            carregarAutoresNoModelo(model);
            return "admin/publicar-livro";
        }
    }
    
    @GetMapping("/deletar/{id}")
    public String deletarLivro(@PathVariable Long id) {
        livroService.deletar(id);
        return "redirect:/admin/livros";
    }
    
    private String salvarArquivo(MultipartFile arquivo, String diretorio) throws IOException {
        String nomeOriginal = arquivo.getOriginalFilename();
        if (nomeOriginal == null || nomeOriginal.isBlank()) {
            nomeOriginal = "arquivo_sem_nome";
        }
        
        String nomeBase = Paths.get(nomeOriginal).getFileName().toString().replaceAll("[^a-zA-Z0-9.-]", "_");
        String nomeArquivo = UUID.randomUUID() + "_" + nomeBase;
        
        Path caminhoDiretorio = Paths.get(diretorio);
        Files.createDirectories(caminhoDiretorio);

        Path caminhoCompleto = caminhoDiretorio.resolve(nomeArquivo);
        
        Files.copy(arquivo.getInputStream(), caminhoCompleto, StandardCopyOption.REPLACE_EXISTING);
        
        return "/" + diretorio + "/" + nomeArquivo;
    }

}