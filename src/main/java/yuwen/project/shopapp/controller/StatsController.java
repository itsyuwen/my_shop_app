package yuwen.project.shopapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuwen.project.shopapp.domain.OrderItem;
import yuwen.project.shopapp.domain.Product;
import yuwen.project.shopapp.service.StatsService;
import yuwen.project.shopapp.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;
    private final UserService userService;
    // Endpoint for most frequently purchased items by a user
    @GetMapping("/popular/{topN}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Product>> getMostPopularProducts(@PathVariable int topN) {
        List<Product> products = statsService.findPopularProducts(topN);
        return ResponseEntity.ok(products);
    }
    @GetMapping("/profit/{topN}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")

    public ResponseEntity<List<Product>> getMostProfitable(@PathVariable int topN) {
        List<Product> products = statsService.findProfitableProducts(topN);
        return ResponseEntity.ok(products);
    }
    @GetMapping("/frequent/{topN}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Product>> getMostFrequentlyPurchasedProductsByUser(@PathVariable int topN) {

        Long userId = userService.getCurrentUserId();
        List<Product> products = statsService.findTopPurchasedProductsByUser(userId, topN);
        return ResponseEntity.ok(products);
    }

    // Endpoint for most recently purchased items by a user
    @GetMapping("/recent/{topN}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Product>> getMostRecentlyPurchasedProductsByUser(@PathVariable int topN) {
        Long userId = userService.getCurrentUserId();
        List<Product> products = statsService.findTopRecentlyPurchasedProductsByUser(userId, topN);
        return ResponseEntity.ok(products);
    }
}
