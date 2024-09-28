package yuwen.project.shopapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuwen.project.shopapp.domain.Product;
import yuwen.project.shopapp.domain.Watchlist;
import yuwen.project.shopapp.domain.Watchlist.WatchlistKey;
import yuwen.project.shopapp.repository.ProductRepository;
import yuwen.project.shopapp.repository.WatchlistRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void addToWatchlist(Long userId, Long productId) {
        WatchlistKey id = new WatchlistKey(userId, productId);
        if (watchlistRepository.existsById(id)) {
            throw new IllegalStateException("Product is already in the watchlist");
        }

        Watchlist watchlist = new Watchlist();
        watchlist.setId(id);
        watchlistRepository.save(watchlist);
    }

    @Transactional
    public void removeFromWatchlist(Long userId, Long productId) {
        WatchlistKey id = new WatchlistKey(userId, productId);
        Watchlist watchlist = watchlistRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product not found in watchlist"));

        watchlistRepository.delete(watchlist);
    }

    public List<Product> viewWatchlist(Long userId) {
        List<Long> productIds = watchlistRepository.findAllByUserId(userId).stream()
                .map(Watchlist::getId)
                .map(WatchlistKey::getProduct)
                .collect(Collectors.toList());

        return productRepository.findAllById(productIds).stream()
                .filter(product -> product.getQuantity() > 0)
                .collect(Collectors.toList());
    }
}
