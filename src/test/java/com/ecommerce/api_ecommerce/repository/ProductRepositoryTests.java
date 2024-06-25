package com.ecommerce.api_ecommerce.repository;

import com.ecommerce.api_ecommerce.dto.ProductDto;
import com.ecommerce.api_ecommerce.model.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void init(){
        this.product = Product.builder().name("Nome").description("descrição").price(10.99).build();
        this.productDto = ProductDto.builder().name("Nome").description("descrição").price(10.99).build();
    }

    @Test
    public void ProductRepositoryTest_SaveProduct_ReturnProduct(){
//        Arrange

//        Act
        Product savedProduct = productRepository.save(product);
//        Assert
        Assertions.assertThat(savedProduct).isNotNull();
        Assertions.assertThat(savedProduct.getName()).isEqualTo(this.product.getName());
    }

    @Test
    public void ProductRepository_GetProductById_ReturnProduct(){
//        Arrange
        productRepository.save(product);
//        Act
        Optional<Product> foundProduct = productRepository.findById(product.getProductId());
//        Assert
        Assertions.assertThat(foundProduct).isNotNull();
        Assertions.assertThat(foundProduct.get().getProductId())
                .isEqualTo(product.getProductId());
    }

    @Test
    public void ProductRepository_GetAllProducts_ReturnProductList(){
//      Arrange
        productRepository.save(this.product);
//        Act
        List<Product> productList = productRepository.findAll();
//        Assert
        Assertions.assertThat(productList).isNotNull();
        Assertions.assertThat(productList.size()).isEqualTo(1);
    }
    @Test
    public void ProductRepository_UpdateProduct_ReturnProduct(){
//      Arrange
        Product toUpdateProduct = productRepository.save(this.product);

        toUpdateProduct.setName(productDto.getName());
        toUpdateProduct.setPrice(productDto.getPrice());

//        Act
        Product newProduct = productRepository.save(toUpdateProduct);
//        Assert
        Assertions.assertThat(newProduct).isNotNull();
        Assertions.assertThat(newProduct.getProductId()).isEqualTo(this.product.getProductId());
        Assertions.assertThat(newProduct.getName()).isEqualTo(this.productDto.getName());
    }
    @Test
    public void ProductRepository_DeleteProductById_ReturnProduct(){
//      Arrange
        this.product = productRepository.save(this.product);
//        Act
        productRepository.deleteById(product.getProductId());
//        Assert
        Assertions.assertThat(productRepository.findById(product.getProductId()).isEmpty()).isTrue();

    }




}
