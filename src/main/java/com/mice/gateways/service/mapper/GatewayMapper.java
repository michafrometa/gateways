package com.mice.gateways.service.mapper;

import com.mice.gateways.domain.Gateway;
import com.mice.gateways.service.dto.GatewayDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity {@link Gateway} and its DTO {@link GatewayDTO}.
 */
@Mapper(componentModel = "spring")
public interface GatewayMapper extends EntityMapper<GatewayDTO, Gateway> {

    Gateway toEntity(GatewayDTO gatewayDTO);

    GatewayDTO toDto(Gateway gateway);

    List<Gateway> toEntity(List<GatewayDTO> dtoList);

    List<GatewayDTO> toDto(List<Gateway> entityList);

    default Gateway fromId(Long id) {
        if (id == null) {
            return null;
        }
        Gateway gateway = new Gateway();
        gateway.setId(id);
        return gateway;
    }
}
