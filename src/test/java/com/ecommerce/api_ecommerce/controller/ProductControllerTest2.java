package com.ecommerce.api_ecommerce.controller;

import com.ecommerce.api_ecommerce.dto.ProductDto;
import com.ecommerce.api_ecommerce.model.Product;
import com.ecommerce.api_ecommerce.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class ProductControllerTest2 {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService service;

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

    @DisplayName("Junit test for given Product Object when createProduct is called then return ProductDto")
    @Test
    public void givenProductObject_whenCreateProduct_thenReturnProductDto() throws Exception {
        given(service.createProduct(any(ProductDto.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(
                    post("/api/product/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productDto)));


        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(productDto.getName())))



    }



}
