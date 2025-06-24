package com.ecommerce.api_ecommerce.controller;


import com.ecommerce.api_ecommerce.dto.LoginDTO;
import com.ecommerce.api_ecommerce.dto.RegisterDTO;
import com.ecommerce.api_ecommerce.mapper.ProductMapper;
import com.ecommerce.api_ecommerce.model.User;
import com.ecommerce.api_ecommerce.repository.UserRepository;
import com.ecommerce.api_ecommerce.security.MyTokenService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MyTokenService myTokenService;
    private ModelMapper modelMapper = new ModelMapper();


    @PostMapping(value = "/register",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data, BindingResult bindingResult){
        if(userRepository.findByUsername(data.username()) != null) return new ResponseEntity<>("usuário já cadastrado", HttpStatus.CONFLICT);
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User user = User.builder()
                .email(data.email())
                .role(data.role())
                .username(data.username())
                .password(encryptedPassword)
                .build();
        User createdUser = userRepository.save(user);
        return new ResponseEntity<>(new RegisterDTO(createdUser.getUsername(), null, createdUser.getRole(), createdUser.getEmail()), HttpStatus.CREATED);
    }

    @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO data){
        if(userRepository.findByUsername(data.username()) == null) return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        String token = myTokenService.createToken((User) auth.getPrincipal());

        return ResponseEntity.ok(token);
    }
}
