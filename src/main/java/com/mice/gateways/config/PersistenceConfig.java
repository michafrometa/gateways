package com.mice.gateways.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enable auditing via annotation configuration
 * https://www.baeldung.com/database-auditing-jpa#1-enabling-jpa-auditing
 */
@Configuration
@EnableJpaAuditing
public class PersistenceConfig {
}
