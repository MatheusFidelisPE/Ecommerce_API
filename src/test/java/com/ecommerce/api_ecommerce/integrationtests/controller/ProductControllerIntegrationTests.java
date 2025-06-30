package com.ecommerce.api_ecommerce.integrationtests.controller;

import com.ecommerce.api_ecommerce.config.TestConfigs;
import com.ecommerce.api_ecommerce.dto.LoginDTO;
import com.ecommerce.api_ecommerce.dto.ProductDto;
import com.ecommerce.api_ecommerce.dto.RegisterDTO;
import com.ecommerce.api_ecommerce.integrationtests.testcontainers.AbstractIntegrationTest;
import com.ecommerce.api_ecommerce.model.Product;
import com.ecommerce.api_ecommerce.model.enums.UserRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.google.common.net.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProductControllerIntegrationTests extends AbstractIntegrationTest {

    private static RequestSpecification specificationUser;
    private static RequestSpecification specificationProduct;
    private static ObjectMapper objectMapper;
    private static ProductDto product;
    private static RegisterDTO registro;
    private static LoginDTO login;
    private static String token;
    private static Integer productId;
    @Autowired
    private ProductDto productDto;

    @BeforeAll
    public static void setup(){
        registro = RegisterDTO.builder()
                .username("John Doe")
                .email("john@gmail.com")
                .password("123456")
                .role(UserRole.ADM)
                .build();
        login = new LoginDTO("John Doe", "123456");
        product = ProductDto.builder()
                .name("Product Name")
                .description("Product Description")
                .price(99.99)
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        specificationUser = new RequestSpecBuilder()
                .setPort(TestConfigs.SERVER_PORT_SWAGGER)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .build();
        specificationProduct = new RequestSpecBuilder()
                .setBasePath("/api/product/")
                .setPort(TestConfigs.SERVER_PORT_SWAGGER)
                .addFilter(new RequestLoggingFilter())
                .build();

    }
    @Test
    @Order(1)
    @DisplayName("Criando um usuário com sucesso")
    void integrationTest_when_CreateOneUser_ShouldReturnOneUserObject() throws JsonProcessingException {
        specificationUser.basePath("/auth/register");

        var content = given()
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .spec(specificationUser)
                .body(registro)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract().body().asString();
        RegisterDTO createdUser = objectMapper.readValue(content, RegisterDTO.class);
        assertNotNull(createdUser);
        assertTrue(createdUser.username() != null && !createdUser.username().isEmpty());
        assertTrue(createdUser.username().equals(registro.username()));
    }
    @Test
    @Order(2)
    @DisplayName("Fazendo login com usuário criado no método anterior")
    void integrationTest_when_Login_ShouldReturnOneTokenAccess() throws JsonProcessingException {
        specificationUser.basePath("/auth/login");

        var content = given()
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .spec(specificationUser)
                .body(login)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().body().asString();
        token = content;
        assertNotNull(token);
    }
    @Test
    @Order(3)
    @DisplayName("Criando um produto com o usuário anteriormente criado")
    void integrationTest_when_CreateAProduct_ShouldReturnProduct() throws JsonProcessingException {

        Map<String, String> headers = Map.of("Authorization", "Bearer " + token);
        var content = given()
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .spec(specificationProduct)
                .headers(headers)
                .body(product)
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(201)
                .extract().body().asString();
        ProductDto createdProduct = objectMapper.readValue(content, ProductDto.class);
        productId = createdProduct.getProductId();
        assertEquals("Product Description", createdProduct.getDescription());
        assertEquals("Product Name", createdProduct.getName());
        assertEquals(99.99, createdProduct.getPrice());

    }
    @Test
    @Order(4)
    @DisplayName("Buscando todos os produtos cadastrados")
    void integrationTest_when_CollectAllProducts_ShouldReturnTheListOfProducts() throws JsonProcessingException {

        Map<String, String> headers = Map.of("Authorization", "Bearer " + token);
        var content = given()
                .spec(specificationProduct)
                .headers(headers)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().body().asString();
        List<ProductDto> products = Arrays.asList(objectMapper.readValue(content, ProductDto[].class));
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Product Description", products.get(0).getDescription());
        assertEquals("Product Name", products.get(0).getName());
    }
    @Test
    @Order(5)
    @DisplayName("Buscando um produto pelo ID")
    void integrationTest_when_GetAnProductPassingID_ShouldReturnProductObject() throws JsonProcessingException {

        Map<String, String> headers = Map.of("Authorization", "Bearer " + token);
        var content = given()
                .spec(specificationProduct)
                .pathParam("id", productId)
                .headers(headers)
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .extract().body().asString();
        ProductDto foundProduct = objectMapper.readValue(content, ProductDto.class);
        assertNotNull(foundProduct);
        assertEquals("Product Description", foundProduct.getDescription());
        assertEquals("Product Name",foundProduct.getName());
    }
    @Test
    @Order(6)
    @DisplayName("Updating a product by ID")
    void integrationTest_when_UpdateAProductPassingID_ShouldReturnUpdatedProductObject() throws JsonProcessingException {

        Map<String, String> headers = Map.of("Authorization", "Bearer " + token);
        product.setDescription("Updated Product Description");
        var content = given()
                .spec(specificationProduct)
                .pathParam("id", productId)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .headers(headers)
                .body(product)
                .when()
                .put("/{id}")
                .then()
                .statusCode(200)
                .extract().body().asString();
        ProductDto foundProduct = objectMapper.readValue(content, ProductDto.class);
        assertNotNull(foundProduct);
        assertEquals("Updated Product Description", foundProduct.getDescription());
        assertEquals("Product Name",foundProduct.getName());
    }
    @Test
    @Order(7)
    @DisplayName("Delete product by ID")
    void integrationTest_when_DeleteProductById_ShouldReturnNothing() throws JsonProcessingException {

        Map<String, String> headers = Map.of("Authorization", "Bearer " + token);
        product.setDescription("Updated Product Description");
        var content = given()
                .spec(specificationProduct)
                .pathParam("id", productId)
                .headers(headers)
                .when()
                .delete("/{id}")
                .then()
                .statusCode(200)
                .extract().body().asString();git

        assertNotNull(content);
        assertEquals("Produto deletado com sucesso", content);
    }


    @DisplayName("Test Swagger UI page")
    @Test
    void testShouldDisplaySwaggerUIPage(){
        //	Given
        var content = given()
                .basePath("/swagger-ui/index.html")
                .port(TestConfigs.SERVER_PORT_SWAGGER)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        assertTrue(content.contains("Swagger UI"));
    }
}
