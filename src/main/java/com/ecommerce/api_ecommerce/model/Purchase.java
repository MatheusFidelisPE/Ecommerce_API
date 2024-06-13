package com.ecommerce.api_ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private Long purchaseId;

    @ManyToMany
    @JoinTable(name = "product_purchase",
    joinColumns = @JoinColumn(name = "purchaseId"),
    inverseJoinColumns = @JoinColumn(name = "productId"))
    private Set<Product> productList;


}
