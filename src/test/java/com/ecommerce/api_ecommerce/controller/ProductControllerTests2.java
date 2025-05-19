package com.ecommerce.api_ecommerce.controller;

import com.ecommerce.api_ecommerce.dto.ProductDto;
import com.ecommerce.api_ecommerce.model.Product;
import com.ecommerce.api_ecommerce.security.SecurityFilter;
import com.ecommerce.api_ecommerce.service.ProductService;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.hamcrest.Matchers.is;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ProductControllerTests2 {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;
    @MockBean
    private SecurityFilter securityFilter;

    private Product product;
    private ProductDto  productDto;

    @BeforeEach
    public void setup(){
        product = new Product().builder()
                .name("Nome")
                .description("Descrição")
                .price(Double.valueOf(10.5))
                .build();
        productDto = new ProductDto().builder()
                .name("Nome")
                .description("Descrição")
                .price(Double.valueOf(10.5))
                .build();
    }

    @DisplayName("Junit test for given Product object when product then return saved product")
    @Test
    public void testGivenProductObject_WhenCreateProduct_thenReturnSavedProduct() throws Exception {

        given(productService.createProduct(any(ProductDto.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions result =  mockMvc.perform(post("/api/product/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        result
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(productDto.getName())));
    }

    @DisplayName("test Given List Of Products When findAllProducts Then Return Product List")
    @Test
    public void testGivenListOfProducts_WhenFindAllProducts_ThenReturnProductList() throws Exception {

        ProductDto p2 = new ProductDto().builder()
                .name("Prod 2")
                .description("Prod 2 Desc")
                .price(10.99)
                .build();
        List<ProductDto> productDtoList = new ArrayList<>();

        productDtoList.add(productDto);
        productDtoList.add(p2);
        given(productService.getAllProducts())
                .willReturn(productDtoList);

        ResultActions result = mockMvc.perform(get("/api/product/"));

        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(productDtoList.size())));
    }
    @DisplayName("test Given Product When FindById Then Return Product Object")
    @Test
    public void testGivenProduct_WhenFindById_ThenReturnProductObject() throws Exception {


;
        given(productService.getProductByid(any(Integer.class))).willReturn(productDto);
        int productId = 1;

        ResultActions result = mockMvc.perform(get("/api/product/{id}", productId));

        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(productDto.getName())));
    }

    @DisplayName("test Given Product When FindById Then Return Product Object")
    @Test
    public void testGivenInvalidProductId_WhenFindById_ThenReturnNotFoundMessage() throws Exception {

        given(productService.getProductByid(any(Integer.class)))
                .willThrow(new EntityNotFoundException());
        int productId = 1;

        ResultActions result = mockMvc.perform(get("/api/product/{id}", productId));

        result
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @DisplayName("test Given Product When UpdateProductById Then Return Product Updated")
    @Test
    public void testGivenUpdateProduct_WhenUpdateProduct_ThenReturnUpdateProduct() throws Exception {

        given(productService.updateProduct(any(Integer.class), any(ProductDto.class)))
                .willReturn(productDto);

        productDto.setPrice(25.99);

        int productId = 1;

        ResultActions result = mockMvc.perform(put("/api/product/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(productDto.getName())))
                .andExpect(jsonPath("$.price", is(productDto.getPrice())));
    }

    @DisplayName("test Given Unsaved ID When UpdateProductById Then Return Error Message")
    @Test
    public void testGivenError_WhenUpdateProduct_ThenReturnErrorMessage() throws Exception {

        given(productService.updateProduct(any(Integer.class), any(ProductDto.class)))
                .willThrow(new EntityNotFoundException());

        int productId = 1;

        ResultActions result = mockMvc.perform(put("/api/product/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        result
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage", is("Product with id "+productId+" not found")));
    }

    @DisplayName("test Given ID When DeleteByID Then return Ok message")
    @Test
    public void testGivenID_WhenDeleteProductById_ThenReturnOkMessage() throws Exception {

        doNothing()
                .when(productService)
                .deleteById(any(Integer.class));

        int productId = 1;

        ResultActions result = mockMvc.perform(delete("/api/product/{id}", productId));

        result
                .andDo(print())
                .andExpect(status().isOk());
    }
    @DisplayName("test unsaved ID When DeleteByID Then return Error Message")
    @Test
    public void testGivenUnsavedID_WhenDeleteProductById_ThenReturnErrorMessage() throws Exception {

        doThrow(new EntityNotFoundException())
                .when(productService)
                .deleteById(any(Integer.class));

        int productId = 1;

        ResultActions result = mockMvc.perform(delete("/api/product/{id}", productId));

        result
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage", is("Product with id"+productId+"Not found")));
    }



}
