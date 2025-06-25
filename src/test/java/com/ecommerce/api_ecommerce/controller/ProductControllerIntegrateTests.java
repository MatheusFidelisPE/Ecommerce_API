package com.ecommerce.api_ecommerce.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ecommerce.api_ecommerce.dto.ProductDto;
import com.ecommerce.api_ecommerce.model.Product;
import com.ecommerce.api_ecommerce.model.User;
import com.ecommerce.api_ecommerce.model.enums.UserRole;
import com.ecommerce.api_ecommerce.repository.ProductRepository;
import com.ecommerce.api_ecommerce.security.MyTokenService;
import com.ecommerce.api_ecommerce.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ProductControllerIntegrateTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MyTokenService tokenService;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;
    private Product product;
    private ProductDto productDto;
    private String token;
    private User user;

    @BeforeEach
    void init(){
        this.product = Product.builder().name("Nome").description("descrição").price(10.99).build();
        this.productDto = ProductDto.builder().name("Nome").description("descrição").price(10.99).build();
        this.user = User.builder()
                .username("testUser")
                .role(UserRole.USER)
                .password("passTest")
                .build();
        this.token = tokenService.createToken((User) user);
    }

//    @Test
    public void ProductController_getAllProducts_RetunProductDtoList() throws Exception {


        when(productService.getAllProducts())
                .thenReturn(Arrays.asList(productDto));

        ResultActions result =  mockMvc.perform(get("/api/product/")
                        .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON));

        result
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.size()").value(1))
                .andDo(MockMvcResultHandlers.print());
    }
//    @Test
    public void ProductController_CreateProductWithRoleUser_RetunUnauthorized() throws Exception {


        when(productService.createProduct(Mockito.any(ProductDto.class)))
                .thenReturn(productDto);

        ResultActions result =  mockMvc.perform(post("/api/product/create")
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        result
                .andExpect(MockMvcResultMatchers.status().is(403));
    }
//    @Test
    public void ProductController_CreateProductWithRoleMng_RetunUnauthorized() throws Exception {
        this.user.setRole(UserRole.MNG);
        token = tokenService.createToken(user);

        when(productService.createProduct(Mockito.any(ProductDto.class)))
                .thenReturn(productDto);

        ResultActions result =  mockMvc.perform(post("/api/product/create")
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        result
                .andExpect(MockMvcResultMatchers.status().is(403));
    }
//    @Test
    public void ProductController_CreateProductWithRoleAdm_RetunUnauthorized() throws Exception {
        this.user.setRole(UserRole.ADM);
        token = tokenService.createToken(user);

        when(productService.createProduct(Mockito.any(ProductDto.class)))
                .thenReturn(productDto);

        ResultActions result =  mockMvc.perform(post("/api/product/create")
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        result
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("name").value("Nome"))
                .andExpect(MockMvcResultMatchers.jsonPath("productId").value(0));
    }
}
