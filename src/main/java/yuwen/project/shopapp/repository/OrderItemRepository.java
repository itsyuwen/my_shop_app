package yuwen.project.shopapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yuwen.project.shopapp.domain.OrderItem;
import yuwen.project.shopapp.domain.Product;


import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT oi.product as product, SUM((oi.purchasedPrice - oi.wholesalePrice) * oi.quantity) as totalProfit FROM OrderItem oi JOIN oi.order o WHERE o.orderStatus NOT IN ('Processing', 'Canceled') GROUP BY oi.product ORDER BY totalProfit DESC")
    List<Product> findProductWithMostProfit(Pageable pageable);
    @Query("SELECT oi.product as product, SUM(oi.quantity) as quantitySold FROM OrderItem oi JOIN oi.order o WHERE o.orderStatus NOT IN ('Processing', 'Canceled') GROUP BY oi.product ORDER BY quantitySold DESC")
    List<Product> findTopNMostPopularProducts(Pageable pageable);

    @Query(value = "SELECT oi.product FROM OrderItem oi JOIN oi.order o WHERE o.user.id = :userId AND o.orderStatus != 'canceled' GROUP BY oi.product.productId ORDER BY COUNT(oi.product.productId) DESC")
    List<Product> findTopNFrequentlyPurchasedItemsByUserId(Long userId, Pageable pageable);
    @Query(value = "SELECT oi.product FROM OrderItem oi JOIN oi.order o WHERE o.user.id = :userId AND o.orderStatus != 'canceled' ORDER BY o.datePlaced DESC, oi.itemId ASC")
    List<Product>findTopNRecentlyPurchasedItemsByUserId(Long userId, Pageable pageable);
}



