package com.ecommerce.api_ecommerce.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ecommerce.api_ecommerce.dto.LoginDTO;
import com.ecommerce.api_ecommerce.dto.RegisterDTO;
import com.ecommerce.api_ecommerce.model.User;
import com.ecommerce.api_ecommerce.model.enums.UserRole;
import com.ecommerce.api_ecommerce.repository.UserRepository;
import com.ecommerce.api_ecommerce.security.MyTokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private MyTokenService myTokenService;

    private RegisterDTO registerDTO;
    private LoginDTO loginDTO;
    private User user;
    private UserDetails userDetails;
    @BeforeEach
    public void init(){
        this.registerDTO = new RegisterDTO("user", "123", UserRole.USER, "email@gmail");
        this.loginDTO = new LoginDTO("user", "123");
        this.user = User.builder()
                .email("user@gmail")
                .password("123")
                .role(UserRole.USER)
                .username("user")
                .build();
        this.userDetails = (UserDetails) user;
    }

    @Test
    @DisplayName("Register User successfully")
    public void AuthController_RegisterUser_ReturnStatusCreated() throws Exception {
//        Arrange
        when(userRepository.findByUsername(any()))
                .thenReturn(null);
        when(userRepository.save(any()))
                .thenReturn(this.user);
//        Act
       ResultActions result =  mockMvc
                .perform(post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerDTO))
                        );
//        Assert
        result
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @Test
    @DisplayName("User already registered")
    public void AuthController_RegisterUser_ReturnStatusConflict() throws Exception {
//        Arrange
        when(userRepository.findByUsername(any()))
                .thenReturn(this.user);
        when(userRepository.save(any()))
                .thenReturn(this.user);
//        Act
        ResultActions result =  mockMvc
                .perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO))
                );
//        Assert
        result
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }
//    @Test
//    @DisplayName("login successfully")
//    public void AuthController_LoginUser_ReturnToken() throws Exception {
//
//        //        Arrange
//        String token = createTokenIntern(user);
//        userDetails = (UserDetails) user;
//        when(userRepository.findByUsername(any()))
//                .thenReturn(userDetails);
//
//        when(myTokenService. createToken(any()))
//                .thenReturn(token);
//        when(authenticationManager.authenticate(any()))
//                .thenReturn(Authentication);
////        Act
//        ResultActions result =  mockMvc
//                .perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginDTO))
//                );
////        Assert
//        result
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//    }
    @Test
    void testLogin_UserFound() throws Exception {

        when(userRepository.findByUsername(Mockito.any()))
                .thenReturn(userDetails);

        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));

        String expectedToken = "testToken";
        when(myTokenService.createToken(Mockito.any()))
                .thenReturn(expectedToken);

        ResultActions result =  mockMvc
                .perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO))
                );
//        Assert
        result
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    private String createTokenIntern(User auth) {
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
