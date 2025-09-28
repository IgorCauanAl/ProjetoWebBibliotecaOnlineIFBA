package br.ifba.edu.BibliotecaOnline.service;

import br.ifba.edu.BibliotecaOnline.entities.Autor;
import br.ifba.edu.BibliotecaOnline.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;

    public List<Autor> listarTodos() {
        return autorRepository.findAll();
    }

    public Autor buscarPorId(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Autor não encontrado para o ID: " + id));
    }

    public Optional<Autor> buscarPorNomeIgnoreCase(String nome) {
        return autorRepository.findByNomeAutorIgnoreCase(nome);
    }
    
}