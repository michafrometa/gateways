package com.mice.gateways.service.mapper.peripheral;


import com.mice.gateways.domain.Peripheral;
import com.mice.gateways.service.dto.peripheral.PeripheralDTO;
import com.mice.gateways.service.dto.peripheral.PeripheralOnlyGatewayDTO;
import com.mice.gateways.service.mapper.EntityMapper;
import com.mice.gateways.service.mapper.GatewayMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

/**
 * Mapper for the entity {@link Peripheral} and its DTO {@link PeripheralDTO}.
 */
@Mapper(componentModel = "spring", uses = {GatewayMapper.class})
public interface PeripheralMapper extends EntityMapper<PeripheralDTO, Peripheral> {

    @Mapping(source = "gatewayId", target = "gateway")
    Peripheral toEntity(PeripheralDTO peripheral);

    @Mapping(source = "gatewayId", target = "gateway")
    Peripheral toEntity(PeripheralOnlyGatewayDTO peripheral);

    @Mapping(source = "gateway.id", target = "gatewayId")
    PeripheralDTO toDto(Peripheral peripheral);

    @Mapping(source = "gateway.id", target = "gatewayId")
    PeripheralOnlyGatewayDTO toPeripheralOnlyGatewayDTO (Peripheral peripheral);

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
