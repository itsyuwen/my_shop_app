// OrderRequest DTO
package yuwen.project.shopapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private List<OrderItemRequest> order;

    @Data
    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;
    }
}
