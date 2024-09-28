package yuwen.project.shopapp.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import lombok.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Watchlist {

    @EmbeddedId
    private WatchlistKey id;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WatchlistKey implements Serializable {
        @Column(name = "user_id")
        private Long user;

        @Column(name = "product_id")
        private Long product;


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof WatchlistKey)) return false;
            WatchlistKey that = (WatchlistKey) o;
            return Objects.equals(user, that.user) &&
                    Objects.equals(product, that.product);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, product);
        }
    }
}
