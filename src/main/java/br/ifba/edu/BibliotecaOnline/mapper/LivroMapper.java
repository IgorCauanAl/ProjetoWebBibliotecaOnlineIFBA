package br.ifba.edu.BibliotecaOnline.mapper;

import br.ifba.edu.BibliotecaOnline.DTO.LivroDTO;
import br.ifba.edu.BibliotecaOnline.entities.LivroEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface LivroMapper {

    @Mapping(target = "publicadoPor", ignore = true)
    LivroEntity toEntity(LivroDTO dto);

    @Mapping(source = "publicadoPor.nome", target = "publicadoPorNome")
    LivroDTO toDTO(LivroEntity entity);
}

