package com.mice.gateways.service.mapper;


import com.mice.gateways.domain.Peripheral;
import com.mice.gateways.service.dto.PeripheralDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

/**
 * Mapper for the entity {@link Peripheral} and its DTO {@link PeripheralDTO}.
 */
@Mapper(componentModel = "spring")
public interface PeripheralMapper extends EntityMapper<PeripheralDTO, Peripheral> {

    @Mapping(target = "gateway", ignore = true)
    Peripheral toEntity(PeripheralDTO peripheral);

    PeripheralDTO toDto(Peripheral peripheral);

    List<Peripheral> toEntity(List<PeripheralDTO> dtoList);

    List<PeripheralDTO> toDto(List<Peripheral> entityList);

    default Peripheral fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Peripheral peripheral = new Peripheral();
        peripheral.setId(id);
        return peripheral;
    }
}
