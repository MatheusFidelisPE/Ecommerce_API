package com.ecommerce.api_ecommerce.integrationtests.controller;

import com.ecommerce.api_ecommerce.config.TestConfigs;
import com.ecommerce.api_ecommerce.dto.LoginDTO;
import com.ecommerce.api_ecommerce.dto.RegisterDTO;
import com.ecommerce.api_ecommerce.integrationtests.testcontainers.AbstractIntegrationTest;
import com.ecommerce.api_ecommerce.model.enums.UserRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserControllerIntegrationTests extends AbstractIntegrationTest {

    private static RegisterDTO registro;
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static LoginDTO login;


    @BeforeAll
    public static void setup(){
        registro = RegisterDTO.builder()
                .username("John Doe")
                .email("john@gmail.com")
                .password("123456")
                .role(UserRole.USER)
                .build();
        login = new LoginDTO(registro.username(), registro.password());

        objectMapper = new ObjectMapper();
        specification = new RequestSpecBuilder()
                .setPort(TestConfigs.SERVER_PORT)
                .setBasePath("/auth")
                .addFilter(new RequestLoggingFilter()) // Se não passar nada no construtor, ele fará o log de tudo
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("Criando um usuário com sucesso - Integration Test")
    public void integrationTest_when_CreateOneUser_ShouldReturnOneUserObject() throws JsonProcessingException {
        specification.basePath("/auth/register");
        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(registro)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();
        RegisterDTO createdUser = objectMapper.readValue(content, RegisterDTO.class);
        assertNotNull(createdUser);
        assertTrue(createdUser.username() != null && !createdUser.username().isEmpty());
        assertTrue(createdUser.username().equals(registro.username()));
    }
    @Test
    @Order(2)
    @DisplayName("Login com usuário criado no método anterior - Integration Test")
    public void integrationTest_when_LoginOneUser_ShouldReturnOneToken() throws JsonProcessingException {
        specification.basePath("/auth/login");
        var token = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(login)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        assertNotNull(token);
    }

}
