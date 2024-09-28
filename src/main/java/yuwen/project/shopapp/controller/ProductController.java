package yuwen.project.shopapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import yuwen.project.shopapp.domain.Product;
import yuwen.project.shopapp.dto.ProductRequest;
import yuwen.project.shopapp.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> viewAvailableProducts(Authentication authentication) {
        List<Product> availableProducts;
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            availableProducts = productService.getAvailableProductsSeller();
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            availableProducts = productService.getAvailableProducts();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(availableProducts);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PatchMapping("/{productId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody ProductRequest updateDTO) {
        Product updatedProduct = productService.updateProduct(productId, updateDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest createDTO) {
        Product product = productService.createProduct(createDTO);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }
}
