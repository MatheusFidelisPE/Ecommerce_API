package com.ecommerce.api_ecommerce.repository;

import com.ecommerce.api_ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    public UserDetails findByUsername(String username);

}
