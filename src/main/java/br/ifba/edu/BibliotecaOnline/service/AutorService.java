package br.ifba.edu.BibliotecaOnline.service;

import br.ifba.edu.BibliotecaOnline.entities.Autor;
import br.ifba.edu.BibliotecaOnline.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;

    public List<Autor> listarTodos() {
        return autorRepository.findAll();
    }
}