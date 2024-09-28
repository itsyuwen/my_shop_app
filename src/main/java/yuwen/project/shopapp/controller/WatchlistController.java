package yuwen.project.shopapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import yuwen.project.shopapp.domain.Product;
import yuwen.project.shopapp.service.UserService;
import yuwen.project.shopapp.service.WatchlistService;

import java.util.List;

@RestController
@RequestMapping("/watchlist")
@PreAuthorize("hasRole('ROLE_USER')")
public class WatchlistController {

    private final WatchlistService watchlistService;
    private final UserService userService;

    @Autowired
    public WatchlistController(WatchlistService watchlistService, UserService userService) {
        this.watchlistService = watchlistService;
        this.userService = userService;
    }

    // Endpoint to get all watchlist items for a user
    @GetMapping("/products/all")
    public ResponseEntity<List<Product>> getAllWatchlistItems() {
        Long userId = userService.getCurrentUserId();
        List<Product> watchlist = watchlistService.viewWatchlist(userId);
        return ResponseEntity.ok(watchlist);
    }

    // Endpoint to add a product to a user's watchlist
    @PostMapping("/product/{productId}")
    public ResponseEntity<?> addToWatchlist(@PathVariable Long productId) {
        Long userId = userService.getCurrentUserId();
        watchlistService.addToWatchlist(userId, productId);
        return ResponseEntity.ok("Product added to watchlist");
    }

    // Endpoint to remove a product from a user's watchlist
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<?> removeFromWatchlist(@PathVariable Long productId) {
        // This needs to be updated to get the userID from the SecurityContext
        Long userId = userService.getCurrentUserId();

        watchlistService.removeFromWatchlist(userId, productId);
        return ResponseEntity.ok("Product removed from watchlist");
    }


}
