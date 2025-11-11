package com.example.backend.mapper;


import com.example.backend.domain.Beneficio;
import com.example.backend.dto.BeneficioDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BeneficioMapper {
    BeneficioDTO toDto(Beneficio b);
    Beneficio toEntity(BeneficioDTO dto);
}
