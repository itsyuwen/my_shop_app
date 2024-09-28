package yuwen.project.shopapp.repository;

import org.springframework.stereotype.Repository;
import yuwen.project.shopapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Check existence by username
    boolean existsByUsername(String username);

    // Check existence by email
    boolean existsByEmail(String email);

    // Find by username
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);

}
