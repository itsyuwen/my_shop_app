package yuwen.project.shopapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuwen.project.shopapp.domain.Order;
import yuwen.project.shopapp.domain.OrderItem;
import yuwen.project.shopapp.domain.Product;
import yuwen.project.shopapp.dto.ProductRequest;
import yuwen.project.shopapp.repository.OrderRepository;
import yuwen.project.shopapp.repository.ProductRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Product addProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setWholesalePrice(productRequest.getWholesalePrice());
        product.setRetailPrice(productRequest.getRetailPrice());
        product.setQuantity(productRequest.getQuantity());
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long productId, ProductRequest updateRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        product.setDescription(updateRequest.getDescription());
        product.setWholesalePrice(updateRequest.getWholesalePrice());
        product.setRetailPrice(updateRequest.getRetailPrice());
        product.setQuantity(updateRequest.getQuantity());
        return productRepository.save(product);
    }

    public Page<Order> viewOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAll(pageable);
    }

    public Order viewOrderDetails(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    @Transactional
    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!isValidStatusTransition(order.getOrderStatus(), status)) {
            throw new IllegalStateException("Invalid order status transition");
        }

        order.setOrderStatus(status);
        orderRepository.save(order);

        if ("CANCELED".equals(status)) {
            restoreProductQuantities(order);
        }
    }

    // Helper method to restore product quantities when an order is canceled
    private void restoreProductQuantities(Order order) {
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);
        }
    }

    // Helper method to check if the status transition is valid
    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        // Define a valid order status transition map
        List<String> validTransitions = Arrays.asList("PROCESSING", "SHIPPED", "COMPLETED", "CANCELED");

        // Ensure current status can only transition to a valid next status
        if ("COMPLETED".equals(currentStatus) || "CANCELED".equals(currentStatus)) {
            return false; // Can't transition away from a final state
        }

        // Otherwise, allow the transition if new status is valid
        return validTransitions.contains(newStatus);
    }
}
