package com.ecommerce.api_ecommerce.service;

import com.ecommerce.api_ecommerce.dto.ProductDto;
import com.ecommerce.api_ecommerce.mapper.ProductMapper;
import com.ecommerce.api_ecommerce.model.Product;
import com.ecommerce.api_ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest2 {
    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository repository;
    @Mock
    private ProductMapper mapper;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    public void setup(){
        product = new Product().builder()
                .name("Nome")
                .description("Descrição")
                .price(10.5)
                .build();
        productDto = new ProductDto().builder()
                .name("Nome")
                .description("Descrição")
                .price(10.5)
                .build();

    }


    @DisplayName("Junit test for given Product Object when save Product is called then return Product")
    @Test
    void testGivenProductObject_whenSaveProductIsCalled_thenReturnProduct() {
        // Arrange
       given(repository.findByName(anyString())).willReturn(Optional.empty());
       given(repository.save(product)).willReturn(product);
       given(mapper.toEntity(productDto)).willReturn(product);
       given(mapper.toDto(product)).willReturn(productDto);

       ProductDto savedProduct = productService.createProduct(productDto);

       assertNotNull(savedProduct);
//       assertTrue(savedProduct.getProductId()>0);
        assertEquals("Nome", savedProduct.getName());
    }
    @DisplayName("Junit test for given Existing name when save Product is called then throw exception")
    @Test
    void testGivenExistingName_whenSaveProductIsCalled_thenThrowException() {

        given(repository.findByName(anyString())).willReturn(Optional.of(product));
        given(mapper.toEntity(productDto)).willReturn(product);


        // Act & Assert
        assertThrows(RuntimeException.class, () -> productService.createProduct(productDto));

        verify(repository, never()).save(any(Product.class));
    }

    @DisplayName("Junit test for given Product List when getAllProducts is called then return Product List")
    @Test
    void testGivenProductList_whenGetAllProductsIsCalled_thenReturnProductList() {
        // Arrange
        given(repository.findAll()).willReturn(List.of(product));
        given(mapper.toProductDtoList(any())).willReturn(List.of(productDto));

        List<ProductDto> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
    }
    @DisplayName("Junit test for given Product List when getAllProducts is called then return Product List")
    @Test
    void testGivenProductList_whenGetAllProductsIsCalled_thenReturnEmptyProductList() {
        // Arrange
        given(repository.findAll()).willReturn(Collections.emptyList());
        given(mapper.toProductDtoList(any())).willReturn(Collections.emptyList());

        List<ProductDto> products = productService.getAllProducts();

        assertTrue(products.isEmpty());
        assertEquals(0, products.size());
    }

    @DisplayName("Junit test for given Product Id when getProductById is called then return Product")
    @Test
    void testGivenProductId_whenGetProductByIdIsCalled_thenReturnProduct() {

        given(repository.findById(any())).willReturn(Optional.of(product));
        given(mapper.toDto(product)).willReturn(productDto);

        ProductDto dto = productService.getProductByid(1);

        assertNotNull(dto);
        assertEquals("Nome", dto.getName());
    }
    @DisplayName("Junit test for given Product when updateProduct is called then return Product Object")
    @Test
    void testGivenProductObject_WhenUpdateProduct_thenReturnUpdatedProductObjectDto() {

        productDto.setName("Nome Novo");
        product.setName("Nome Novo");

        given(repository.findById(any())).willReturn(Optional.of(product));
        doNothing().when(mapper).mapDtoToEtt(product, productDto);
        given(repository.save(product)).willReturn(product);
        given(mapper.toDto(any())).willReturn(productDto);

        ProductDto dto = productService.updateProduct(1, productDto);

        assertNotNull(dto);
        assertEquals("Nome Novo", dto.getName());
    }
    @DisplayName("Junit  testGivenProductID_WhenDeleteProduct_thenReturnNothing")
    @Test
    void testGivenProductID_WhenDeleteProduct_thenReturnNothing() {

        given(repository.findById(any())).willReturn(Optional.of(product));
        willDoNothing().given(repository).deleteById(any());

        productService.deleteById(1);

        verify(repository, times(1)).deleteById(1);
    }



}
