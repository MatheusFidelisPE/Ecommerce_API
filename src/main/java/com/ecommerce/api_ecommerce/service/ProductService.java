package com.ecommerce.api_ecommerce.service;

import com.ecommerce.api_ecommerce.mapper.ProductMapper;
import com.ecommerce.api_ecommerce.dto.ProductDto;
import com.ecommerce.api_ecommerce.model.LogSystem;
import com.ecommerce.api_ecommerce.model.Product;
import com.ecommerce.api_ecommerce.model.enums.LogEnum;
import com.ecommerce.api_ecommerce.model.enums.TypeEntity;
import com.ecommerce.api_ecommerce.repository.LogSystemRepository;
import com.ecommerce.api_ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private LogSystemRepository logSystemRepository;
    public List<ProductDto> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        return productMapper.toProductDtoList(productList);
    }


    public ProductDto createProduct(ProductDto dto) {
        Product product = productMapper.toEntity(dto);
        Product created  = productRepository.save(product);
//        Log
//        LogSystem log = LogSystem.builder()
//                .logEnum(LogEnum.CREATE)
//                .action("salvar")
//                .editionMoment(LocalDateTime.now())
//                .userId(user.getUsername())
//                .typeEntity(TypeEntity.PRODUCT)
//                .idEntity((long) created.getProductId())
//                .build();
//        logSystemRepository.save(log);
        return productMapper.toDto(created);
    }

    public ProductDto getProductByid(Integer id) throws EntityNotFoundException{
        Product product = productRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new);
        ProductDto dto = productMapper.toDto(product);
        return dto;
    }

    public ProductDto updateProduct(Integer id, ProductDto dto) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new);

        productMapper.mapDtoToEtt(product, dto);
        product.setProductId(id);
        productRepository.save(product);

        return productMapper.toDto(product);
    }
    
    public void deleteById(Integer id) {
        productRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        productRepository.deleteById(id);

    }
}
