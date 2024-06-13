package com.ecommerce.api_ecommerce.controller;


import com.ecommerce.api_ecommerce.dto.LoginDTO;
import com.ecommerce.api_ecommerce.dto.RegisterDTO;
import com.ecommerce.api_ecommerce.model.User;
import com.ecommerce.api_ecommerce.repository.UserRepository;
import com.ecommerce.api_ecommerce.security.MyTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MyTokenService myTokenService;

    @PostMapping(value = "/register",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data){
        if(userRepository.findByUsername(data.username()) != null) return new ResponseEntity<>("usuário já cadastrado", HttpStatus.CONFLICT);
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User user = User.builder()
                .email(data.email())
                .role(data.role())
                .username(data.username())
                .password(encryptedPassword)
                .build();
        userRepository.save(user);
        return new ResponseEntity<>("Usuário criado!", HttpStatus.CREATED);
    }

    @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody @Valid LoginDTO data){
        if(userRepository.findByUsername(data.username()) == null) return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        String token = myTokenService.createToken((User) auth.getPrincipal());

        return ResponseEntity.ok(token);
    }
}
