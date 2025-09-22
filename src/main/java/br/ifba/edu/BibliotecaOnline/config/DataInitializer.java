/*package br.ifba.edu.BibliotecaOnline.config;

import br.ifba.edu.BibliotecaOnline.entities.LivroEntity;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.repository.LivroRepository;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeBooks(LivroRepository livroRepository, UsuarioRepository usuarioRepository) {
        return args -> {
            // Só executa se não houver nenhum livro no banco
            if (livroRepository.count() == 0) {

                // Pega o primeiro usuário (admin) para ser o "autor" padrão das publicações iniciais
                Usuario autorPadrao = usuarioRepository.findById(1L).orElse(null);

                if (autorPadrao != null) {
                    LivroEntity livro1 = new LivroEntity();
                    livro1.setNome("A Culpa é das Estrelas");
                    livro1.setAutor("John Green");
                    livro1.setAnoPublicacao(2012);
                    livro1.setSinopse("Hazel é uma paciente terminal de câncer. Em um grupo de apoio, ela conhece Augustus, e juntos eles embarcam em uma jornada inesquecível.");
                    livro1.setCapaUrl("/uploads/imgs/culpa-estrelas.png"); // Exemplo de imagem
                    livro1.setUsuario(autorPadrao);

                    LivroEntity livro2 = new LivroEntity();
                    livro2.setNome("É Assim que Acaba");
                    livro2.setAutor("Colleen Hoover");
                    livro2.setAnoPublicacao(2016);
                    livro2.setSinopse("Lily acredita ter encontrado o amor de sua vida, mas um encontro casual com um antigo amor a faz questionar tudo.");
                    livro2.setCapaUrl("/uploads/imgs/assim-que-acaba.png"); // Exemplo de imagem
                    livro2.setUsuario(autorPadrao);
                    
                    LivroEntity livro3 = new LivroEntity();
                    livro3.setNome("O Vilarejo");
                    livro3.setAutor("Raphael Montes");
                    livro3.setAnoPublicacao(2015);
                    livro3.setSinopse("Contos de terror interligados que se passam em um vilarejo isolado e amaldiçoado no interior do Brasil.");
                    livro3.setCapaUrl("/uploads/imgs/o-vilarejo.png"); // Exemplo de imagem
                    livro3.setUsuario(autorPadrao);

                    
                    livroRepository.saveAll(Arrays.asList(livro1, livro2, livro3));
                           
                } 
            }
        };
    }
} */
