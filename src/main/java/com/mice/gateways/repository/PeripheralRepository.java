package com.mice.gateways.repository;

import com.mice.gateways.domain.Peripheral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PeripheralRepository extends JpaRepository<Peripheral, UUID> {
}
