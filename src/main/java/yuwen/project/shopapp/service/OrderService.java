package yuwen.project.shopapp.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuwen.project.shopapp.domain.Order;
import yuwen.project.shopapp.domain.OrderItem;
import yuwen.project.shopapp.domain.Product;
import yuwen.project.shopapp.domain.User;
import yuwen.project.shopapp.dto.OrderRequest;
import yuwen.project.shopapp.exceptions.EntityNotFoundException;
import yuwen.project.shopapp.repository.OrderItemRepository;
import yuwen.project.shopapp.repository.OrderRepository;
import yuwen.project.shopapp.repository.ProductRepository;
import yuwen.project.shopapp.repository.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
enum OrderStatus {
    PROCESSING, COMPLETED, CANCELED;
}
@Service
public class OrderService {
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public Order createOrder(OrderRequest orderRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Order order = new Order();
        order.setDatePlaced(new Date());
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PROCESSING.toString());
        order.setOrderItems(new ArrayList<>());

        for (OrderRequest.OrderItemRequest itemRequest : orderRequest.getOrder()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + itemRequest.getProductId()));

            if (product.getQuantity() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product id: " + itemRequest.getProductId());
            }

            product.setQuantity(product.getQuantity() - itemRequest.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPurchasedPrice(product.getRetailPrice());
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);

            productRepository.save(product);
        }

        return orderRepository.save(order);
    }


    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getUserOrders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        return orderRepository.findAllByUser(user);
    }
    public Order getOrderDetail(Long orderId) {
        // Fetch an order by its ID or throw if not found
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order with ID " + orderId + " not found"));
    }
    public Order getOrderDetailByUser(Long orderId,Long userId) {
        // Fetch an order by its ID or throw if not found
        Order od = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order with ID " + orderId + " not found"));
        if(!od.getUser().getId().equals(userId)){
            throw new EntityNotFoundException("Order with ID " + orderId + " not found");
        }
        return od;
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, String newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));


        if ("CANCELED".equals(newStatus) && "CANCELED".equals(order.getOrderStatus())) {
            throw new RuntimeException("Order has already been canceled!");
        }

        // For cancellation, restore the stock
        if ("CANCELED".equals(newStatus)) {
            order.getOrderItems().forEach(item -> {
                Product product = item.getProduct();
                product.setQuantity(product.getQuantity() + item.getQuantity());
                productRepository.save(product);
            });
        }
        order.setOrderStatus(newStatus);
        return orderRepository.save(order);
    }
    @Transactional
    public Order updateOrderStatusByUser(Long userId,Long orderId, String newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if(!order.getUser().getId().equals(userId)){
            throw new EntityNotFoundException("Order with ID " + orderId + " not found");
        }

        if ("CANCELED".equals(newStatus) && "CANCELED".equals(order.getOrderStatus())) {
            throw new RuntimeException("Order has already been canceled!");
        }

        // For cancellation, restore the stock
        if ("CANCELED".equals(newStatus)) {
            order.getOrderItems().forEach(item -> {
                Product product = item.getProduct();
                product.setQuantity(product.getQuantity() + item.getQuantity());
                productRepository.save(product);
            });
        }
        order.setOrderStatus(newStatus);
        return orderRepository.save(order);
    }



}
