package com.ecommerce.api_ecommerce.service;

import com.ecommerce.api_ecommerce.mapper.ProductMapper;
import com.ecommerce.api_ecommerce.dto.ProductDto;
import com.ecommerce.api_ecommerce.model.Product;
import com.ecommerce.api_ecommerce.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private ProductService productService;


    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void init(){
        this.product = Product.builder().name("Nome").description("descrição").price(10.99).build();
        this.productDto = ProductDto.builder().name("Nome").description("descrição").price(10.99).build();
    }

    @Test
    public void ProductService_GetAllProducts_ReturnListOfProduct(){
//        Arrange
        when(productRepository.findAll())
                .thenReturn(Arrays.asList(this.product));
        when(productMapper.toProductDtoList(Mockito.anyList()))
                .thenReturn(Arrays.asList(productDto));
//        Act
        List<ProductDto> productDtoList = productService.getAllProducts();
//        Assert
        Assertions.assertThat(productDtoList).isNotNull();
        Assertions.assertThat(productDtoList.size()).isEqualTo(1);
    }
    @Test
    public void ProductService_CreateProduct_ReturnProductDto(){
//        Arrange
        when(productMapper.toEntity(Mockito.any(ProductDto.class)))
                .thenReturn(product);
        when(productRepository.save(Mockito.any(Product.class)))
                .thenReturn(product);
        when(productMapper.toDto(Mockito.any()))
                .thenReturn(productDto);
//        Act
        ProductDto createdProduct = productService.createProduct(productDto);
//        Assert
        Assertions.assertThat(createdProduct).isNotNull();
        Assertions.assertThat(createdProduct.getProductId())
                .isEqualTo(product.getProductId());
    }
    @Test
    public void ProductService_GetProductById_ReturnProductDto(){
        when(productRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(product));
        when(productMapper.toDto(Mockito.any(Product.class)))
                .thenReturn(productDto);

        ProductDto collectedProduct = productService.getProductByid(1);

        Assertions.assertThat(collectedProduct).isNotNull();
        Assertions.assertThat(collectedProduct.getProductId()).isEqualTo(product.getProductId());
    }
    @Test
    public void ProductService_UpdateProduct_ReturnProductDto(){
        when(productRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(product));
        when(productRepository.save(Mockito.any()))
                .thenReturn(product);
        when(productMapper.toDto(Mockito.any()))
                .thenReturn(productDto);

        ProductDto updatedProduct = productService.updateProduct(1, productDto);

        Assertions.assertThat(updatedProduct)
                .isNotNull();
        Assertions.assertThat(updatedProduct.getName())
                .isEqualTo(productDto.getName());
        Assertions.assertThat(updatedProduct.getProductId())
                .isEqualTo(productDto.getProductId());
        Assertions.assertThat(updatedProduct.getPrice())
                .isEqualTo(productDto.getPrice());
        Assertions.assertThat(updatedProduct.getDescription())
                .isEqualTo(productDto.getDescription());

    }
    @Test
    public void ProductService_DeleteProductById_ReturnNothing(){
        when(productRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(product));
        doNothing().when(productRepository).deleteById(Mockito.any());

        productService.deleteById(1);

        verify(productRepository).deleteById(Mockito.any());
        verify(productRepository).findById(Mockito.any());
    }
}
