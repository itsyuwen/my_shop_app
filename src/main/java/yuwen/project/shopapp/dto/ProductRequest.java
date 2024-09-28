package yuwen.project.shopapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private String description;
    private Double wholesalePrice;
    private Double retailPrice;
    private Integer quantity;
}
