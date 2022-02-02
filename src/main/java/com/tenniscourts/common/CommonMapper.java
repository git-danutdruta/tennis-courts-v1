package com.tenniscourts.common;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

public interface CommonMapper<D, E> {
    @Mapping(target = "id", ignore = true)
    E dtoToNewEntity(D dto);

    E dtoToEntity(D dto);

    @InheritInverseConfiguration(name = "dtoToEntity")
    D entityToDto(E entity);

    default List<E> dtoToEntity(List<D> dtoList) {
        return dtoList.stream().map(this::dtoToEntity).collect(Collectors.toList());
    }

    default List<D> entityToDto(List<E> entityList) {
        return entityList.stream().map(this::entityToDto).collect(Collectors.toList());
    }
}
