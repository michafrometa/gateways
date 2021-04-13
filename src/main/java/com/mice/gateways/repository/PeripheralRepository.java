package com.mice.gateways.repository;

import com.mice.gateways.domain.Peripheral;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PeripheralRepository extends JpaRepository<Peripheral, UUID> {
    Page<Peripheral> findByGatewayId(Long gatewayId, Pageable page);

    Optional<Peripheral> findByIdAndGatewayId(UUID id, Long gatewayId);

}
