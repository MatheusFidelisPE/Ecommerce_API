package com.ecommerce.api_ecommerce.controller;

import com.ecommerce.api_ecommerce.service.ProductService;
import com.ecommerce.api_ecommerce.dto.ErrorResponse;
import com.ecommerce.api_ecommerce.dto.ProductDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
    @RequestMapping(value = "/api/product",name = "products-controller")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/",
            name = "get-all-products",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDto> getProducts(){
        List<ProductDto> productDtoList = productService.getAllProducts();
        return new ResponseEntity(productDtoList, HttpStatus.OK);
    }

    @PostMapping(value = "/",
            name ="create-product",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDto dto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
           List<ObjectError> erros = bindingResult.getAllErrors();
           String message = erros.stream()
                   .map(ObjectError::getDefaultMessage)
                   .collect(Collectors.joining("; "));
           ErrorResponse errorResponse = ErrorResponse.builder().errorMessage(message).code(HttpStatus.BAD_REQUEST.value()).build();

            return new ResponseEntity(errorResponse,HttpStatus.BAD_REQUEST);
        }else{
            ProductDto createdProductDto = productService.createProduct(dto);
            return new ResponseEntity(createdProductDto,HttpStatus.CREATED);
        }
    }

    @GetMapping(value = "/{id}",
            name ="get-product",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProductById(@PathVariable("id") Integer id){

        try{
            ProductDto dto = productService.getProductByid(id);
            return new ResponseEntity(dto,HttpStatus.OK);
        }catch (EntityNotFoundException e){
            String errorMessage = "Product with id "+id.toString()+" not found";
            ErrorResponse errorResponse = ErrorResponse
                    .builder()
                    .errorMessage(errorMessage)
                    .code(HttpStatus.NOT_FOUND.value())
                    .build();
            return new ResponseEntity(errorResponse,HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{id}",
            name = "update-product",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProductById(@PathVariable("id") Integer id,
                                               @RequestBody @Valid ProductDto dto){
        try {
            ProductDto updateProduct = productService.updateProduct(id, dto);
            return new ResponseEntity(updateProduct,HttpStatus.OK);
        }catch (EntityNotFoundException e){
            String errorMessage = "Product with id "+id.toString()+" not found";
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errorMessage(errorMessage).code(HttpStatus.NOT_FOUND.value()).build();
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping(value = "/{id}",
            name = "delete-product",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteProductById(@PathVariable("id") Integer id){
        try {
            productService.deleteById(id);
            return new ResponseEntity("Produto deletado com sucesso", HttpStatus.OK);
        }catch (EntityNotFoundException e){
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errorMessage("Product with id"+id.toString()+"Not found")
                    .code(HttpStatus.NOT_FOUND.value())
                    .build();
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
        }

    }
}
