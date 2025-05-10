package com.ecommerce.api_ecommerce.repository;

import com.ecommerce.api_ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByName(String name);

    @Query("SELECT p FROM Product p WHERE p.name = ?1 AND p.description = ?2")
    Product findByJPQL(String name, String description);
}
