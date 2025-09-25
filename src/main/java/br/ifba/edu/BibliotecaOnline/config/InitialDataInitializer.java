package br.ifba.edu.BibliotecaOnline.config;

import br.ifba.edu.BibliotecaOnline.entities.Autor;
import br.ifba.edu.BibliotecaOnline.entities.LivroEntity;
import br.ifba.edu.BibliotecaOnline.entities.Role;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.model.GeneroEnum;
import br.ifba.edu.BibliotecaOnline.repository.AutorRepository;
import br.ifba.edu.BibliotecaOnline.repository.LivroRepository;
import br.ifba.edu.BibliotecaOnline.repository.RoleRepository;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
public class InitialDataInitializer {

    @Bean
    public CommandLineRunner initializeAllData(
            RoleRepository roleRepository,
            UsuarioRepository usuarioRepository,
            LivroRepository livroRepository,
            AutorRepository autorRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // Passo 1: Inicializar as roles
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ADMIN")));
            Role userRole = roleRepository.findByName("USER")
                    .orElseGet(() -> roleRepository.save(new Role("USER")));

            // Passo 2: Inicializar o usuário administrador
            Usuario adminUser = usuarioRepository.findByEmail("admin@biblioteca.com")
                    .orElseGet(() -> {
                        Set<Role> adminRoles = new HashSet<>();
                        adminRoles.add(adminRole);
                        Usuario admin = new Usuario();
                        admin.setNome("Administrador Padrão");
                        admin.setEmail("admin@biblioteca.com");
                        admin.setSenha(passwordEncoder.encode("senha123"));
                        admin.setRoles(adminRoles);
                        return usuarioRepository.saveAndFlush(admin);
                    });

            // Passo 3: Inicializar os autores e os livros
            if (livroRepository.count() == 0) {
                // Autores
                Autor anaSantos = getOrCreateAutor("Ana Santos", "Uma escritora talentosa com foco em narrativas emocionais e poéticas.", autorRepository);
                Autor matheusZara = getOrCreateAutor("Matheus Zara", "Conhecido por suas histórias de fantasia e aventura.", autorRepository);
                Autor johnGreen = getOrCreateAutor("John Green", "Famoso autor de romances para jovens adultos.", autorRepository);
                Autor carlaAbrantes = getOrCreateAutor("Carla Abrantes", "Escritora de ficção científica com foco em mundos distantes.", autorRepository);
                Autor raizaVarella = getOrCreateAutor("Raiza Varella", "Autora que explora temas de fantasia e mitologia em suas obras.", autorRepository);
                Autor pauloBorges = getOrCreateAutor("Paulo Borges", "Autor de ficção científica e mistério, com histórias intrigantes.", autorRepository);
                Autor brunoEduardo = getOrCreateAutor("Bruno Eduardo", "Escritor com uma abordagem única para contos e fábulas.", autorRepository);
                Autor collenHoover = getOrCreateAutor("Collen Hoover", "Autora aclamada por seus romances contemporâneos.", autorRepository);
                Autor rVCampbell = getOrCreateAutor("R.V Campbell", "Especialista em narrativas de viagem no tempo e mistério.", autorRepository);
                Autor rodrigoCiriaco = getOrCreateAutor("Rodrigo Ciríaco", "Escritor brasileiro com histórias sensíveis e significativas.", autorRepository);
                Autor itamarVieira = getOrCreateAutor("Itamar Vieira", "Autor premiado, conhecido por sua obra 'Torto Arado'.", autorRepository);
                Autor raphaelMontes = getOrCreateAutor("Raphael Montes", "Autor de thrillers e romances policiais com um toque sombrio.", autorRepository);
                Autor stephenKing = getOrCreateAutor("Stephen King", "Mestre do terror com obras que marcaram gerações.", autorRepository);
                Autor filipeRibeiro = getOrCreateAutor("Filipe Ribeiro", "Escritor de contos e ficção fantástica.", autorRepository);
                Autor beverlyMariel = getOrCreateAutor("Beverly Mariel", "Autora de suspense e mistério.", autorRepository);
                Autor peterStraub = getOrCreateAutor("Peter Straub", "Coautor e escritor renomado do gênero de terror.", autorRepository);
                Autor anaMorais = getOrCreateAutor("Ana Morais", "Conhecida por seus romances leves e envolventes.", autorRepository);
                Autor helenaFigueiredo = getOrCreateAutor("Helena Figueiredo", "Autora de romances com histórias de reencontro e superação.", autorRepository);
                Autor hannahHowell = getOrCreateAutor("Hannah Howell", "Especialista em romances históricos de época.", autorRepository);

                // Livros em Alta
                addLivro("Está chovendo estrelas", 2022, GeneroEnum.ROMANCE, "Uma história sobre amor e destino em uma noite estrelada.", "/img/capa (6).png", anaSantos, adminUser, livroRepository);
                addLivro("Viagem pelas estrelas", 2021, GeneroEnum.AVENTURA, "Uma jornada intergaláctica em busca de um novo lar para a humanidade.", "/img/capa (5).png", matheusZara, adminUser, livroRepository);
                addLivro("A culpa é das estrelas", 2012, GeneroEnum.ROMANCE, "O romance icônico de John Green sobre dois adolescentes que se apaixonam em um grupo de apoio para pacientes com câncer.", "/img/capa (3).png", johnGreen, adminUser, livroRepository);
                addLivro("Nascer da estrela", 2020, GeneroEnum.FICCAO, "Uma saga épica sobre a origem de uma civilização estelar.", "/img/capa (1).png", carlaAbrantes, adminUser, livroRepository);
                addLivro("Caçadora de estrelas", 2019, GeneroEnum.FANTASIA, "Uma jovem caçadora embarca em uma aventura para recuperar estrelas caídas.", "/img/capa (2).png", raizaVarella, adminUser, livroRepository);
                addLivro("O Planeta Sombrio", 2018, GeneroEnum.SUSPENSE, "Uma expedição de pesquisa se depara com um mistério aterrorizante em um planeta inexplorado.", "/img/capa (4).png", pauloBorges, adminUser, livroRepository);

                // Livros Recentes
                addLivro("O livro da capa verde", 2023, GeneroEnum.FICCAO, "Uma história intrigante sobre um livro misterioso que esconde segredos.", "/img/recentes (1).png", brunoEduardo, adminUser, livroRepository);
                addLivro("É assim que acaba", 2016, GeneroEnum.ROMANCE, "Um romance que aborda temas delicados e a força do amor.", "/img/recentes (2).png", collenHoover, adminUser, livroRepository);
                addLivro("Mestres do tempo", 2021, GeneroEnum.FICCAO, "Um grupo de cientistas tenta dominar a arte de viajar no tempo para corrigir erros do passado.", "/img/recentes (4).png", rVCampbell, adminUser, livroRepository);
                addLivro("Vovó virou semente", 2022, GeneroEnum.BIOGRAFIA, "Uma obra sensível sobre luto e memória, contada através de uma metáfora poética.", "/img/recentes (5).png", rodrigoCiriaco, adminUser, livroRepository);
                addLivro("Torto arado", 2019, GeneroEnum.HISTORIA, "Um romance que narra a história de duas irmãs e a luta por dignidade e liberdade no sertão baiano.", "/img/recentes (6).png", itamarVieira, adminUser, livroRepository);

                // Livros de Terror
                addLivro("O vilarejo", 2020, GeneroEnum.TERROR, "Um grupo de amigos se perde e acaba em um vilarejo isolado e aterrorizante.", "/img/terror (1).png", raphaelMontes, adminUser, livroRepository);
                addLivro("IT a coisa", 1986, GeneroEnum.TERROR, "A clássica história de sete amigos que enfrentam uma criatura maligna que se alimenta de medo.", "/img/terror (2).png", stephenKing, adminUser, livroRepository);
                addLivro("Terra de sonhos e acasos", 2019, GeneroEnum.FANTASIA, "Uma fantasia sombria que explora as fronteiras entre o sonho e a realidade.", "/img/terror (3).png", filipeRibeiro, adminUser, livroRepository);
                addLivro("Knock Knock", 2021, GeneroEnum.TERROR, "Um suspense psicológico onde um simples som na porta desencadeia um pesadelo.", "/img/terror (4).png", beverlyMariel, adminUser, livroRepository);
                addLivro("Os mortos-vivos", 1978, GeneroEnum.TERROR, "Uma epidemia transforma a população em zumbis sedentos por sangue.", "/img/terror (5).png", peterStraub, adminUser, livroRepository);
                addLivro("Saco de ossos", 1998, GeneroEnum.TERROR, "Um escritor de luto descobre segredos sobrenaturais em sua casa de veraneio.", "/img/terror (6).png", stephenKing, adminUser, livroRepository);

                // Livros de Romance
                addLivro("Encontrei você", 2022, GeneroEnum.ROMANCE, "Uma história de amor que transcende o tempo e a distância.", "/img/romance (1).png", anaMorais, adminUser, livroRepository);
                addLivro("Até breve", 2020, GeneroEnum.ROMANCE, "Um romance que explora a saudade e a esperança de um reencontro.", "/img/romance (3).png", helenaFigueiredo, adminUser, livroRepository);
                addLivro("O destino das terras altas", 2015, GeneroEnum.ROMANCE, "Uma história de amor ambientada nas paisagens místicas das terras altas escocesas.", "/img/terror (4).png", hannahHowell, adminUser, livroRepository);
            }
        };
    }

    private Autor getOrCreateAutor(String nome, String descricao, AutorRepository autorRepository) {
        Optional<Autor> autorOpt = autorRepository.findByNomeAutorIgnoreCase(nome);
        return autorOpt.orElseGet(() -> {
            Autor novoAutor = new Autor(nome, descricao);
            return autorRepository.save(novoAutor);
        });
    }

    private void addLivro(String nome, Integer ano, GeneroEnum genero, String sinopse, String capaUrl, Autor autor, Usuario publicadoPor, LivroRepository livroRepository) {
        LivroEntity livro = new LivroEntity();
        livro.setNome(nome);
        livro.setAnoPublicacao(ano);
        livro.setGenero(genero);
        livro.setSinopse(sinopse);
        livro.setCapaUrl(capaUrl);
        livro.setAutor(autor);
        livro.setPublicadoPor(publicadoPor);
        livroRepository.save(livro);
    }
}