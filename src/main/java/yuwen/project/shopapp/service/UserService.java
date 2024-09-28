package yuwen.project.shopapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuwen.project.shopapp.domain.*;
import yuwen.project.shopapp.dto.SignUpRequest;
import yuwen.project.shopapp.repository.*;

import yuwen.project.shopapp.service.UserPrincipal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    // Assume WatchlistRepository for watchlist functionalities
    private final WatchlistRepository watchlistRepository;

    public void registerUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername()) || userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("Username or email already taken");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(1);
        userRepository.save(user);
    }
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("No authenticated user.");
        }

        yuwen.project.shopapp.service.UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getId();
    }

    /*
    public List<Product> viewAvailableProducts() {
        return productRepository.findByQuantityGreaterThan(0);
    }

    public Product viewProductDetails(Long productId) {
        return productRepository.findByProductIdAndQuantityGreaterThan(productId, 0)
                .orElseThrow(() -> new IllegalArgumentException("Product not found or out of stock"));
    }

    @Transactional
    public Order cancelOrder(Long orderId, Long userId) {
        Order order = (Order) orderRepository.findByOrderIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("Order not found or does not belong to user"));

        if (!"PROCESSING".equals(order.getOrderStatus())) {
            throw new RuntimeException("Only processing orders can be canceled");
        }

        order.setOrderStatus("CANCELED");
        order.getOrderItems().forEach(item -> {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);
        });

        return orderRepository.save(order);
    }

    @Transactional
    public void addToWatchlist(Long userId, Long productId) {
        Watchlist.WatchlistKey id = new Watchlist.WatchlistKey();
        id.setUser(userId);
        id.setProduct(productId);

        boolean exists = watchlistRepository.existsById(id);
        if (exists) {
            throw new IllegalArgumentException("Product is already in watchlist");
        }

        Watchlist watchlist = new Watchlist();
        watchlist.setId(id);
        watchlistRepository.save(watchlist);
    }


    @Transactional
    public void removeFromWatchlist(Long userId, Long productId) {
        Watchlist.WatchlistKey id = new Watchlist.WatchlistKey(userId, productId);
        Watchlist watchlist = watchlistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not in watchlist"));

        watchlistRepository.delete(watchlist);
    }

    public List<Product> viewWatchlist(Long userId) {
        List<Watchlist> watchlistEntries = watchlistRepository.findAllByUserId(userId);
        return watchlistEntries.stream()
                .map(entry -> productRepository.findById(entry.getId().getProduct())
                        .filter(product -> product.getQuantity() > 0) // Only include in-stock products
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    // Implementations for viewing orders and analytics...
    public List<Order> viewUserOrders(Long userId) {
        return orderRepository.findAllByUserId(userId);
    }

    public Order viewOrderDetails(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }
*/
}
