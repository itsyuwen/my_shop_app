package yuwen.project.shopapp.repository;
import org.springframework.stereotype.Repository;
import yuwen.project.shopapp.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import yuwen.project.shopapp.domain.User;

import java.util.List;
import java.util.Optional;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Find all orders by a specific user (Assuming there's a 'user' relationship in the Order entity)
    List<Order> findAllByUserId(Long userId);

    // Find orders with specific statuses
    List<Order> findByOrderStatus(String status);

    List<Order> findAllByUser(User user);

    Optional<Object> findByOrderIdAndUserId(Long orderId, Long userId);

}
