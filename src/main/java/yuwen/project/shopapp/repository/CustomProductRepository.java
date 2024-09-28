package yuwen.project.shopapp.repository;
import yuwen.project.shopapp.domain.Product;
import java.util.List;
public interface CustomProductRepository {
    List<Product> findProductsBySearchTerm(String searchTerm);
}
