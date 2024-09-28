package yuwen.project.shopapp.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yuwen.project.shopapp.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, CustomProductRepository {
    // Find all products that are in stock (quantity > 0)
    List<Product> findByQuantityGreaterThan(int quantity);
    Optional<Product> findByProductIdAndQuantityGreaterThan(Long id, int stockAmount);
    // Custom query to find all in-stock products (alternative approach using @Query)
    @Query("SELECT p FROM Product p WHERE p.quantity > 0")
    List<Product> findAllInStock();


    // Fetches products ordered by total profit (selling price - purchase price) (Most Profitable)
    @Query("SELECT oi.product FROM OrderItem oi " +
            "JOIN oi.order o " +
            "WHERE o.orderStatus <> 'CANCELED' " +
            "GROUP BY oi.product " +
            "ORDER BY SUM((oi.purchasedPrice - oi.wholesalePrice) * oi.quantity) DESC, oi.product.productId ASC")
    List<Product> findTopNMostProfitableProducts(Pageable pageable);

    // Fetches products ordered by the total sold quantity (Most Popular)
    @Query("SELECT oi.product FROM OrderItem oi " +
            "JOIN oi.order o " +
            "WHERE o.orderStatus <> 'CANCELED' " +
            "GROUP BY oi.product " +
            "ORDER BY SUM(oi.quantity) DESC, oi.product.productId ASC")
    List<Product> findTopNMostPopularProducts(Pageable pageable);

    Boolean existsByName(String name);
}
