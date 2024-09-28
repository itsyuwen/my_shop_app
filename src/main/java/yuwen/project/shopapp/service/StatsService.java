package yuwen.project.shopapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import yuwen.project.shopapp.domain.Product;
import yuwen.project.shopapp.repository.OrderItemRepository;


import java.util.List;


@Service
public class StatsService {

    private final OrderItemRepository orderItemRepository;

    @Autowired
    public StatsService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }
    public List<Product> findPopularProducts(int limit) {
        List<Product> popularProducts = orderItemRepository.findTopNMostPopularProducts(PageRequest.of(0, limit));
        return popularProducts;
    }
    public List<Product> findProfitableProducts(int limit) {
        List<Product> profitableProducts = orderItemRepository.findProductWithMostProfit(PageRequest.of(0, limit));
        return profitableProducts;
    }
    public List<Product> findTopRecentlyPurchasedProductsByUser(Long userId, int limit) {
        List<Product> recentPurchases = orderItemRepository.findTopNRecentlyPurchasedItemsByUserId(userId, PageRequest.of(0, limit));
        return recentPurchases;
    }

    public List<Product> findTopPurchasedProductsByUser(Long userId, int limit) {
        List<Product> topPurchases = orderItemRepository.findTopNFrequentlyPurchasedItemsByUserId(userId, PageRequest.of(0, limit));

        return topPurchases;
    }

}
