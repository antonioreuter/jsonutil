package com.waes.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by aandra1 on 30/09/16.
 */
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.waes.models"})
@EnableJpaRepositories(basePackages = {"com.waes.repositories"})
@EnableTransactionManagement
public class RepositoryConfig {
}