package com.ecommerce.api_ecommerce.repository;

import com.ecommerce.api_ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
