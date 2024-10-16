package com.becoder.service;



import com.becoder.entity.Product;
import com.becoder.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

//    public Product addProduct(Product product) {
//        return productRepository.save(product);
//    }


//
//    public void deleteProduct(Long id) {
//        productRepository.deleteById(id);
//    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
}
