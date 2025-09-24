package br.ifba.edu.BibliotecaOnline.config;

import br.ifba.edu.BibliotecaOnline.entities.LivroEntity;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.model.GeneroEnum;
import br.ifba.edu.BibliotecaOnline.repository.LivroRepository;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@Order(2)
public class BookDataInitializer {

    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;

    public BookDataInitializer(LivroRepository livroRepository, UsuarioRepository usuarioRepository) {
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public CommandLineRunner initializeBooks() {
        return args -> {
            // LOG 1: Verificar se o inicializador de livros está rodando
            System.out.println("--- [DEBUG] BookDataInitializer está rodando ---");

            long bookCount = livroRepository.count();
            // LOG 2: Verificar a contagem de livros
            System.out.println("--- [DEBUG] Quantidade de livros no banco: " + bookCount + " ---");

            if (bookCount == 0) {
                System.out.println("--- [DEBUG] Nenhum livro encontrado, tentando criar os livros de teste... ---");
                
                // LOG 3: Verificar se estamos encontrando o admin
                Usuario autorPadrao = usuarioRepository.findByEmail("admin@biblioteca.com").orElse(null);
                if (autorPadrao == null) {
                    System.out.println("--- [DEBUG] ERRO: autorPadrao é nulo! Não foi possível encontrar o usuário com e-mail 'admin@biblioteca.com'. ---");
                } else {
                    System.out.println("--- [DEBUG] Admin encontrado com sucesso: " + autorPadrao.getNome() + " ---");
                }


                if (autorPadrao != null) {
                    // (O resto do seu código para criar os 15 livros permanece EXATAMENTE O MESMO aqui)
                    List<String> titulos = Arrays.asList(
                        "O Sol é Para Todos", "1984", "Dom Quixote", "Cem Anos de Solidão", "Orgulho e Preconceito",
                        "A Revolução dos Bichos", "O Grande Gatsby", "O Apanhador no Campo de Centeio", "Moby Dick", "Guerra e Paz",
                        "O Morro dos Ventos Uivantes", "Ulisses", "A Divina Comédia", "Hamlet", "O Senhor dos Anéis"
                    );

                    List<String> autores = Arrays.asList(
                        "Harper Lee", "George Orwell", "Miguel de Cervantes", "Gabriel García Márquez", "Jane Austen",
                        "George Orwell", "F. Scott Fitzgerald", "J.D. Salinger", "Herman Melville", "Liev Tolstói",
                        "Emily Brontë", "James Joyce", "Dante Alighieri", "William Shakespeare", "J.R.R. Tolkien"
                    );

                    List<GeneroEnum> generos = Arrays.asList(
                        GeneroEnum.FICCAO, GeneroEnum.FICCAO, GeneroEnum.AVENTURA, GeneroEnum.FANTASIA, GeneroEnum.ROMANCE,
                        GeneroEnum.FICCAO, GeneroEnum.ROMANCE, GeneroEnum.FICCAO, GeneroEnum.AVENTURA, GeneroEnum.HISTORIA,
                        GeneroEnum.ROMANCE, GeneroEnum.FICCAO, GeneroEnum.FANTASIA, GeneroEnum.HISTORIA, GeneroEnum.FANTASIA
                    );

                    List<LivroEntity> livrosParaAdicionar = new ArrayList<>();

                    for (int i = 1; i <= 15; i++) {
                        LivroEntity livro = new LivroEntity();
                        String titulo = titulos.get(i - 1);
                        String autor = autores.get(i - 1);
                        
                        livro.setNome(titulo);
                        livro.setAutor(autor);
                        livro.setAnoPublicacao(1800 + (i * 15));
                        livro.setSinopse("Esta é a sinopse do livro '" + titulo + "'. Uma obra clássica da literatura mundial.");
                        livro.setDescricaoAutor("Biografia de " + autor + ", um renomado autor de sua época.");
                        livro.setGenero(generos.get(i - 1));
                        livro.setCapaUrl("/uploads/imgs/" + i + ".png");
                        livro.setPdfUrl("/uploads/pdfs/" + i + ".pdf");
                        livro.setPublicadoPor(autorPadrao);

                        livrosParaAdicionar.add(livro);
                    }
                    
                    livroRepository.saveAll(livrosParaAdicionar);
                    // LOG 4: Mensagem de sucesso
                    System.out.println("--- [DEBUG] SUCESSO: " + livrosParaAdicionar.size() + " livros foram salvos no banco de dados. ---");
                }
            } else {
                 System.out.println("--- [DEBUG] O sistema já tem livros, a criação de livros de teste foi ignorada. ---");
            }
        };
    }
}