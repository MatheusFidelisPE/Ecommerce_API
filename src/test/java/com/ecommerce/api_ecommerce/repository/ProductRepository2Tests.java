package com.ecommerce.api_ecommerce.repository;

import com.ecommerce.api_ecommerce.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ProductRepository2Tests {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("Given Product Object when save then return saved product")
    @Test
    void testGivenProductObject_whenSave_thenReturnSavedProduct() {
//        Given
        Product product1 = new Product().builder()
                .name("Teste 1")
                .description("Descrição teste 1")
                .price(23.45)
                .build();
//        When
        Product savedProduct = productRepository.save(product1);
//        Then
        assertNotNull(savedProduct);
        assertTrue(savedProduct.getProductId() > 0);


    }
}
