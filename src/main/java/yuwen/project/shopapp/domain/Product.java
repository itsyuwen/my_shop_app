package yuwen.project.shopapp.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String name;

    private String description;

    private Integer quantity;

    @Column(nullable = false)
    private Double retailPrice;

    private Double wholesalePrice;

}
