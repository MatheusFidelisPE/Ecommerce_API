package com.ecommerce.api_ecommerce.repository;

import com.ecommerce.api_ecommerce.integrationtests.testcontainers.AbstractIntegrationTest;
import com.ecommerce.api_ecommerce.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepository2Tests extends AbstractIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    private Product product1;

    @BeforeEach
    public void setup(){
        this.product1 = new Product().builder()
                .name("Teste 1")
                .description("Descrição teste 1")
                .price(23.45)
                .build();
    }
    @DisplayName("Given Product Object when save then return saved product")
    @Test
    void testGivenProductObject_whenSave_thenReturnSavedProduct() {
//        Given

//        When
        Product savedProduct = productRepository.save(product1);
//        Then
        assertNotNull(savedProduct);
        assertTrue(savedProduct.getProductId() > 0);
    }

    @DisplayName("Given a Product Object List when find all products then return the product list")
    @Test
    void testGivenProductObjectList_whenSave_thenReturnSavedProducts() {

        Product product2 = new Product().builder()
                .name("Teste 2")
                .description("Descrição teste 2")
                .price(3.34)
                .build();
        productRepository.save(product1);
        productRepository.save(product2);

        List<Product> products = productRepository.findAll();

        assertNotNull(products);
        assertEquals(2, products.size());
    }
    @DisplayName("Given Product Object  when find by id  then return product object")
    @Test
    void testGivenProductID_whenFindById_thenReturnProductObject() {
        productRepository.save(product1);

        Product foundProduct = productRepository.findById(product1.getProductId()).get();


        assertNotNull(foundProduct);
        assertEquals(product1.getName(), foundProduct.getName());
    }
    @DisplayName("Given a product name when FindByName then return product object")
    @Test
    void testGivenProductName_whenFindByName_thenReturnProductObject() {
        productRepository.save(product1);

        Product foundProduct = productRepository.findByName("Teste 1").get();

        assertNotNull(foundProduct);
        assertEquals(product1.getName(), foundProduct.getName());
        assertEquals(product1.getPrice(), foundProduct.getPrice());
    }

    @DisplayName("Given product object when updateProduct then return updated product")
    @Test
    void testGivenProductObject_whenUpdateProduct_thenReturnUpdatedProduct() {
        productRepository.save(product1);
        product1.setName("Teste 2 - UPDATE");

        Product updatedProduct = productRepository.save(product1);

        assertNotNull(updatedProduct);
        assertEquals("Teste 2 - UPDATE", updatedProduct.getName());
        assertEquals(product1.getDescription(), updatedProduct.getDescription());
        assertEquals(product1.getPrice(), updatedProduct.getPrice());
    }

    @DisplayName("Given product object when DeleteProduct then remove product")
    @Test
    void testGivenProductObject_whenRemoveProduct_thenReturnRemoveProduct() {
        Product savedProduct = productRepository.save(product1);

        productRepository.deleteById(savedProduct.getProductId());

        Optional<Product> productOptional = productRepository.findById(savedProduct.getProductId());
        assertFalse(productOptional.isPresent());
    }
    @DisplayName("Given product object when findByJPQL then return product object")
    @Test
    void testGivenProductObject_whenFindByJPQL_thenReturnProductObject() {
        productRepository.save(product1);

        Product foundProduct = productRepository.findByJPQL("Teste 1", "Descrição teste 1");

        assertNotNull(foundProduct);
        assertEquals(product1.getName(), foundProduct.getName());
        assertEquals(product1.getDescription(), foundProduct.getDescription());
        assertEquals(product1.getPrice(), foundProduct.getPrice());
    }
}
