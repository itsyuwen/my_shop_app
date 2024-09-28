package yuwen.project.shopapp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yuwen.project.shopapp.domain.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WatchlistRepository extends JpaRepository<Watchlist, Watchlist.WatchlistKey> {
    // Find all watchlist items for a given user ID
    List<Watchlist> findByIdUser(Long userId);

    // Check if a specific product is in the watchlist of a given user
    boolean existsByIdUserAndIdProduct(Long userId, Long productId);

    @Query("SELECT w FROM Watchlist w WHERE w.id.user = :userId")
    List<Watchlist> findAllByUserId(@Param("userId") Long userId);

}
