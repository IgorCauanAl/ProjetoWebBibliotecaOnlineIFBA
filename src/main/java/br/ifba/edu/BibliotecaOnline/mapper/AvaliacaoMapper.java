package br.ifba.edu.BibliotecaOnline.mapper;

import br.ifba.edu.BibliotecaOnline.DTO.AvaliacaoDTO;
import br.ifba.edu.BibliotecaOnline.entities.AvaliacaoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AvaliacaoMapper {

    @Mapping(source = "usuario.nome", target = "nomeUsuario")
    @Mapping(source = "usuario.fotoPerfil", target = "fotoUsuario")
    AvaliacaoDTO toDTO(AvaliacaoEntity entity);
}