package br.ifba.edu.BibliotecaOnline.service;

import br.ifba.edu.BibliotecaOnline.DTO.LivroDTO;
import br.ifba.edu.BibliotecaOnline.excecao.FileDownloadException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final LivroService livroService;


    public ResponseEntity<Resource> downloadPdf(Long id) {
        LivroDTO livro = livroService.buscarPorId(id);
        String pdfUrl = livro.getPdfUrl();

        if (pdfUrl == null || pdfUrl.isBlank()) {
            throw new FileDownloadException("O livro não possui um arquivo PDF anexado.");
        }

        try {
            String caminhoRelativo = pdfUrl.replaceFirst("^/", ""); 
            Path arquivo = Paths.get(caminhoRelativo).toAbsolutePath().normalize();
            Resource resource = new UrlResource(arquivo.toUri());

            if (resource.exists() || resource.isReadable()) {
                String nomeBase = (livro.getNome() != null ? livro.getNome() : "livro");
                String nomeAmigavel = nomeBase.replaceAll("[^a-zA-Z0-9.-]", "_") + ".pdf";

                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeAmigavel + "\"")
                        .body(resource);
            } else {
                throw new FileNotFoundException("Arquivo PDF não encontrado no caminho: " + arquivo.toString());
            }

        } catch (MalformedURLException | FileNotFoundException e) {
            throw new FileDownloadException("Erro ao localizar ou ler o arquivo do livro: " + e.getMessage());
        } catch (IOException e) {
             throw new FileDownloadException("Erro de I/O ao processar o arquivo: " + e.getMessage());
        }
    }
}