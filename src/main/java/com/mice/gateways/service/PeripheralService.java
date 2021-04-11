package com.mice.gateways.service;

import com.mice.gateways.domain.Peripheral;
import com.mice.gateways.repository.PeripheralRepository;
import com.mice.gateways.service.dto.PeripheralDTO;
import com.mice.gateways.service.mapper.PeripheralMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PeripheralService {

    private final Logger log = LoggerFactory.getLogger(PeripheralService.class);

    private final PeripheralRepository peripheralRepository;

    private final PeripheralMapper peripheralMapper;

    public PeripheralService(PeripheralRepository peripheralRepository, PeripheralMapper peripheralMapper) {
        this.peripheralRepository = peripheralRepository;
        this.peripheralMapper = peripheralMapper;
    }

    public PeripheralDTO save(PeripheralDTO peripheralDTO) {
        log.debug("Request to save Peripheral : {}", peripheralDTO);
        Peripheral peripheral = peripheralMapper.toEntity(peripheralDTO);
        peripheral = peripheralRepository.save(peripheral);
        return peripheralMapper.toDto(peripheral);
    }

    public Page<PeripheralDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Peripherals");
        return peripheralRepository.findAll(pageable)
                .map(peripheralMapper::toDto);
    }

    public Optional<PeripheralDTO> findOne(UUID id) {
        return this.peripheralRepository.findById(id)
                .map(peripheralMapper::toDto);
    }

    public void delete(UUID id) {
        this.peripheralRepository.deleteById(id);
    }
}
