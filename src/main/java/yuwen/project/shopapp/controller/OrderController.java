package yuwen.project.shopapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import yuwen.project.shopapp.domain.Order;
import yuwen.project.shopapp.dto.OrderRequest;
import yuwen.project.shopapp.exceptions.InvalidOrderStateException;
import yuwen.project.shopapp.exceptions.OrderNotFoundException;
import yuwen.project.shopapp.service.OrderService;
import yuwen.project.shopapp.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;


    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest orderRequest) {
        // Assuming that UserPrincipal is correctly set up to get the user's details
        Long userId = userService.getCurrentUserId();
        Order order = orderService.createOrder(orderRequest, userId);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<List<Order>> getAllOrders(Authentication authentication) {
        List<Order> allOrders = new ArrayList<>();
        Long userId = userService.getCurrentUserId();
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            allOrders = orderService.getAllOrders();
        }else if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))){
            allOrders = orderService.getUserOrders(userId);
        }
        return ResponseEntity.ok(allOrders);
    }


    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<Order> getOrderDetail(@PathVariable Long orderId,Authentication authentication) {
        Order order = new Order();
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            order = orderService.getOrderDetail(orderId);
        }else if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))){
            Long userId = userService.getCurrentUserId();
            order = orderService.getOrderDetailByUser(orderId,userId);
        }
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{orderId}/{action}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @PathVariable String action,Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            try {
                String newStatus = null;
                if (action.toLowerCase().equals("cancel")) {
                    newStatus = "CANCELED";
                } else if(action.toLowerCase().equals("complete")) {
                    newStatus = "COMPLETED";
                }else{
                    return ResponseEntity.badRequest().body("Invalid action");
                }
                Order updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
                return ResponseEntity.ok(updatedOrder);
            } catch (OrderNotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            }
        }else if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))){
            Long userId = userService.getCurrentUserId();
            try {
                String newStatus;
                if (action.toLowerCase().equals("cancel")) {
                    newStatus = "CANCELED";
                } else {
                    return ResponseEntity.badRequest().body("Invalid action");
                }
                Order updatedOrder = orderService.updateOrderStatusByUser(userId,orderId, newStatus);
                return ResponseEntity.ok(updatedOrder);
            } catch (OrderNotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            } catch (InvalidOrderStateException ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
            }
        }

        return null;
    }


}
