package br.ifba.edu.BibliotecaOnline.mapper;

import br.ifba.edu.BibliotecaOnline.DTO.LivroDTO;
import br.ifba.edu.BibliotecaOnline.entities.LivroEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LivroMapper {

    @Mapping(target = "publicadoPor", ignore = true)
    @Mapping(target = "autor", ignore = true) // Ignorar o autor, pois será tratado no serviço
    LivroEntity toEntity(LivroDTO dto);

    @Mapping(source = "publicadoPor.nome", target = "publicadoPorNome")
    @Mapping(source = "autor.nomeAutor", target = "autorNome")
    @Mapping(source = "autor.id", target = "autorId")
    @Mapping(target = "novoAutorNome", ignore = true)
    @Mapping(target = "novoAutorDescricao", ignore = true)
    @Mapping(target = "novoAutorFotoUrl", ignore = true)
    LivroDTO toDTO(LivroEntity entity);
}