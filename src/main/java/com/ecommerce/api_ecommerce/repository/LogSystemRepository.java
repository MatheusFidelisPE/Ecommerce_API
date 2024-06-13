package com.ecommerce.api_ecommerce.repository;

import com.ecommerce.api_ecommerce.model.LogSystem;
import com.ecommerce.api_ecommerce.model.PrimaryKeyLogSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogSystemRepository extends JpaRepository<LogSystem, PrimaryKeyLogSystem> {
}
