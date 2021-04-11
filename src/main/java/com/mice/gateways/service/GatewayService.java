package com.mice.gateways.service;

import com.mice.gateways.domain.Gateway;
import com.mice.gateways.repository.GatewayRepository;
import com.mice.gateways.service.dto.GatewayDTO;
import com.mice.gateways.service.mapper.GatewayMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class GatewayService {

    private final Logger log = LoggerFactory.getLogger(GatewayService.class);

    private final GatewayRepository gatewayRepository;

    private final GatewayMapper gatewayMapper;

    public GatewayService(GatewayRepository gatewayRepository, GatewayMapper gatewayMapper) {
        this.gatewayRepository = gatewayRepository;
        this.gatewayMapper = gatewayMapper;
    }

    public GatewayDTO save(GatewayDTO gatewayDTO) {
        log.debug("Request to save Gateway : {}", gatewayDTO);
        Gateway gateway = gatewayMapper.toEntity(gatewayDTO);
        gateway = gatewayRepository.save(gateway);
        return gatewayMapper.toDto(gateway);
    }

    public Page<GatewayDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Gateways");
        return gatewayRepository.findAll(pageable)
                .map(gatewayMapper::toDto);
    }

    public Optional<GatewayDTO> findOne(Long id) {
        return this.gatewayRepository.findById(id)
                .map(gatewayMapper::toDto);
    }

    public void delete(Long id) {
        this.gatewayRepository.deleteById(id);
    }
}
