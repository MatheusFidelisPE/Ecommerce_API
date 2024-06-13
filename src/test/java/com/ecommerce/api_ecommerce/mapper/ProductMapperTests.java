package com.ecommerce.api_ecommerce.mapper;


import com.ecommerce.api_ecommerce.dto.ProductDto;
import com.ecommerce.api_ecommerce.model.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductMapperTests {
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private ProductMapper productMapper;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void init(){
        this.product = Product.builder().name("Nome").description("descrição").price(10.99).build();
        this.productDto = ProductDto.builder().name("Nome").description("descrição").price(10.99).build();
    }

    @Test
    public void ProductMapper_ToProductDtoList_ReturnProductDtoList(){
//        Arrange
        List<Product> productList = Arrays.asList(product);
        when(modelMapper.map(product,ProductDto.class))
                .thenReturn(productDto);
//        Act
        List<ProductDto> dtoList = productMapper.toProductDtoList(productList);
//        Arrange
        Assertions.assertThat(dtoList).isNotNull();
    }
    @Test
    public void ProductMapper_ToEntity_ReturnProduct(){
//        Arrange
        when(modelMapper.map(productDto,Product.class))
                .thenReturn(product);
//        Act
        Product mappedProduct = productMapper.toEntity(productDto);
//        Arrange
        Assertions.assertThat(mappedProduct).isNotNull();
        Assertions.assertThat(mappedProduct.getName()).isEqualTo(productDto.getName());
    }
    @Test
    public void ProductMapper_ToDto_ReturnProductDto(){
//        Arrange
        when(modelMapper.map(product,ProductDto.class))
                .thenReturn(productDto);
//        Act
        ProductDto mappedProductDto = productMapper.toDto(product);
//        Arrange
        Assertions.assertThat(mappedProductDto).isNotNull();
        Assertions.assertThat(mappedProductDto.getName())
                .isEqualTo(product.getName());
    }
    @Test
    public void ProductMapper_DtoToEtt_ReturnNothing(){
//        Arrange
        doNothing().when(modelMapper).map(productDto,product);
//        Act
        productMapper.mapDtoToEtt(product, productDto);
//        Arrange
        verify(modelMapper).map(productDto,product);
    }

}
