package yuwen.project.shopapp.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    @Column(nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
