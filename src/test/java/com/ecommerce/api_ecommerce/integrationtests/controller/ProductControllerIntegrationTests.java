package com.ecommerce.api_ecommerce.integrationtests.controller;

import com.ecommerce.api_ecommerce.config.TestConfigs;
import com.ecommerce.api_ecommerce.integrationtests.testcontainers.AbstractIntegrationTest;
import com.ecommerce.api_ecommerce.model.Product;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProductControllerIntegrationTests extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static Product product;

    @BeforeAll
    public static void setup(){
        product = Product.builder()
                .name("Product Name")
                .description("Product Description")
                .price(99.99)
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        specification = new RequestSpecBuilder()
                .setBasePath("")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .build();
    }
    @Test
    @DisplayName("Criando um usuário com sucesso")
    void integrationTest_when_CreateOneUser_ShouldReturnOneUserObject(){

    }
}
