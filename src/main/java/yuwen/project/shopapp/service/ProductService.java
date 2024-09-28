package yuwen.project.shopapp.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuwen.project.shopapp.domain.Product;
import yuwen.project.shopapp.dto.ProductRequest;
import yuwen.project.shopapp.repository.ProductRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAvailableProducts() {

        return productRepository.findByQuantityGreaterThan(0);
    }
    public List<Product> getAvailableProductsSeller() {

        return productRepository.findByQuantityGreaterThan(-1);
    }
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
    }

    public Product createProduct(ProductRequest productCreateDTO) {
        if (productRepository.existsByName(productCreateDTO.getName())) {
            throw new IllegalArgumentException("Product with name " + productCreateDTO.getName() + " already exists.");
        }

        Product product = new Product();
        BeanUtils.copyProperties(productCreateDTO, product);

        return productRepository.save(product);
    }
    public Product updateProduct(Long productId, ProductRequest productUpdateDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
        
        if (productUpdateDTO.getName() != null && !product.getName().equals(productUpdateDTO.getName())) {
            if (productRepository.existsByName(productUpdateDTO.getName())) {
                throw new IllegalArgumentException("Product with name " + productUpdateDTO.getName() + " already exists.");
            }
            product.setName(productUpdateDTO.getName());
        }

        if (productUpdateDTO.getDescription() != null) {
            product.setDescription(productUpdateDTO.getDescription());
        }
        if (productUpdateDTO.getWholesalePrice() != null) {
            product.setWholesalePrice(productUpdateDTO.getWholesalePrice());
        }
        if (productUpdateDTO.getRetailPrice() != null) {
            product.setRetailPrice(productUpdateDTO.getRetailPrice());
        }
        if (productUpdateDTO.getQuantity() != null) {
            product.setQuantity(productUpdateDTO.getQuantity());
        }

        return productRepository.save(product);
    }
}
