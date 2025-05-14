package com.ecommerce.api_ecommerce.controller;

import com.ecommerce.api_ecommerce.dto.ProductDto;
import com.ecommerce.api_ecommerce.model.Product;
import com.ecommerce.api_ecommerce.security.SecurityFilter;
import com.ecommerce.api_ecommerce.service.ProductService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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

        result.andDo(print())
                .andExpect(status().isCreated());
//                .andExpect(jsonPath("$.name".value);
    }




}
