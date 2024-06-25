package com.ecommerce.api_ecommerce.security;

import com.ecommerce.api_ecommerce.model.User;
import com.ecommerce.api_ecommerce.model.enums.UserRole;
import com.ecommerce.api_ecommerce.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired(required = true)
    private MyTokenService tokenService;
    @Autowired
    private UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenIfExists(request);
        if(token != null){
            UserDataFromToken userDataFromToken = tokenService.verifyToken(token);
//            String username = tokenService.verifyToken(token);
//            UserDetails user = userRepository.findByUsername(username);
            User user = User.builder().username(userDataFromToken.username())
                    .role(UserRole.fromRole(userDataFromToken.role())).build();

            var usernamePassword = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePassword);
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenIfExists(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token == null) return null;
        token = token.replace("Bearer ", "");
        return token;
    }
}
