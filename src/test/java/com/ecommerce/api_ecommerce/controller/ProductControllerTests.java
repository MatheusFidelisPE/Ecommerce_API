package com.ecommerce.api_ecommerce.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ecommerce.api_ecommerce.dto.ErrorResponse;
import com.ecommerce.api_ecommerce.dto.ProductDto;
import com.ecommerce.api_ecommerce.model.Product;
import com.ecommerce.api_ecommerce.model.User;
import com.ecommerce.api_ecommerce.model.enums.UserRole;
import com.ecommerce.api_ecommerce.repository.UserRepository;
import com.ecommerce.api_ecommerce.security.MyTokenService;
import com.ecommerce.api_ecommerce.security.SecurityFilter;
import com.ecommerce.api_ecommerce.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.ResponseActions;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ProductControllerTests {


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;
    @MockBean
    private SecurityFilter securityFilter;
    @Autowired
    private ObjectMapper objectMapper;
    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void init(){
        this.product = Product.builder().name("Nome").description("descrição").price(10.99).build();
        this.productDto = ProductDto.builder().name("Nome").description("descrição").price(10.99).build();
    }

    @Test
    public void ProductController_getAllProducts_RetunProductDtoList() throws Exception {

        when(productService.getAllProducts())
                .thenReturn(Arrays.asList(productDto));

        ResultActions result =  mockMvc.perform(get("/api/product/")
//                        .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON));

        result
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void ProductController_CreateProduct_RetunProductDto() throws Exception {
        when(productService.createProduct(Mockito.any(ProductDto.class)))
                .thenReturn(productDto);

        ResultActions result =  mockMvc.perform(post("/api/product/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        result
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("name").value("Nome"));
//                .andExpect(MockMvcResultMatchers.jsonPath("productId").value(Optional.of(0)));
    }
    @Test
    public void ProductController_CreateProduct_RetunErrorResponse() throws Exception {
        when(productService.createProduct(Mockito.any(ProductDto.class)))
                .thenReturn(productDto);
        ProductError prodError = new ProductError(productDto.getName(), productDto.getPrice());
        ResultActions result =  mockMvc.perform(post("/api/product/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prodError)));


        result
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    public void ProductController_GetProductById_RetunProductDto() throws Exception {
        when(productService.getProductByid(Mockito.any(Integer.class)))
                .thenReturn(productDto);

        ResultActions result =  mockMvc.perform(get("/api/product/1"));

        result
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("name").value("Nome"))
                .andExpect(MockMvcResultMatchers.jsonPath("productId").value(0));
    }
    @Test
    public void ProductController_GetProductById_RetunResponseError() throws Exception {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(404)
                .errorMessage("Product with id "+1+" not found")
                .build();

        doThrow(new EntityNotFoundException()).when(productService)
                .getProductByid(Mockito.any());

        ResultActions result =  mockMvc.perform(get("/api/product/1")
                .contentType(MediaType.APPLICATION_JSON));

        result
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage").value(errorResponse.getErrorMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(errorResponse.getCode()));
    }

    @Test
    public void ProductController_UpdateProduct_RetunProductDto() throws Exception {
        when(productService.updateProduct(Mockito.any(Integer.class),Mockito.any(ProductDto.class)))
                .thenReturn(productDto);

        ResultActions result =  mockMvc.perform(put("/api/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        result
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("name").value("Nome"))
                .andExpect(MockMvcResultMatchers.jsonPath("productId").value(0));
    }
    @Test
    public void ProductController_UpdateProduct_RetunResponseError() throws Exception {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(404)
                .errorMessage("Product with id "+1+" not found")
                .build();
        doThrow(new EntityNotFoundException()).when(productService)
                .updateProduct(Mockito.any(), Mockito.any());

        ResultActions result =  mockMvc.perform(put("/api/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        result
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage").value(errorResponse.getErrorMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(errorResponse.getCode()));

    }
    @Test
    public void ProductController_DeleteProduct_RetunNothing() throws Exception {
        doNothing().when(productService).deleteById(Mockito.any(Integer.class));

        ResultActions result =  mockMvc.perform(delete("/api/product/1")
                .contentType(MediaType.APPLICATION_JSON));

        result
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(productService).deleteById(Mockito.any());

    }
    @Test
    public void ProductController_DeleteProduct_RetunErrorMessage() throws Exception {
        ErrorResponse errorResponse = ErrorResponse.builder().code(404).errorMessage("Product with id"+1+"Not found").build();
        doThrow(new EntityNotFoundException()).when(productService).deleteById(Mockito.any());

        ResultActions result =  mockMvc.perform(delete("/api/product/1")
                .contentType(MediaType.APPLICATION_JSON));

        result
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage").value(errorResponse.getErrorMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(errorResponse.getCode()));

        verify(productService).deleteById(Mockito.any());

    }
    private String createToken(User auth) {
        Algorithm algorithm = Algorithm.HMAC256("my-secret-key");
        return JWT.create()
                .withIssuer("ecommerce-matheus")
                .withExpiresAt(getExpiretionInstant())
                .withSubject(auth.getUsername())
                .sign(algorithm);
    }
    private Instant getExpiretionInstant() {
        return LocalDateTime.now().plusHours(10).toInstant(ZoneOffset.of("-03:00"));
    }
}
record ProductError(String name, Double price){}