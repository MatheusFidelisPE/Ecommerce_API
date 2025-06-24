package com.ecommerce.api_ecommerce.mapper;

import com.ecommerce.api_ecommerce.dto.ProductDto;
import com.ecommerce.api_ecommerce.dto.RegisterDTO;
import com.ecommerce.api_ecommerce.model.Product;
import com.ecommerce.api_ecommerce.model.User;
import jakarta.persistence.ManyToMany;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMapper {

    private ModelMapper modelMapper = new ModelMapper();

    public List<ProductDto> toProductDtoList(List<Product> productList){
        List<ProductDto> productDtoList = productList
                .stream()
                .map(x -> modelMapper.map(x,ProductDto.class))
                .toList();
        return productDtoList;
    }
    public Product toEntity(ProductDto dto){
        return modelMapper.map(dto,Product.class);
    }
    public ProductDto toDto(Product ett){
        return modelMapper.map(ett,ProductDto.class);
    }

    public void mapDtoToEtt(Product product, ProductDto dto) {
        modelMapper.map(dto, product);
    }
    public RegisterDTO userToDto(User user){
        return new RegisterDTO(user.getUsername(),
                user.getEmail(),
                null,
                user.getRole().getRole());
    }
}
