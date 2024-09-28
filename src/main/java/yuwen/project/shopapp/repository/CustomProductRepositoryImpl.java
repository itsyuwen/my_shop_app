package yuwen.project.shopapp.repository;

import yuwen.project.shopapp.domain.Product;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class CustomProductRepositoryImpl implements CustomProductRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> findProductsBySearchTerm(String searchTerm) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> product = cq.from(Product.class);

        // Constructing dynamic query
        Predicate namePredicate = cb.like(cb.lower(product.get("name")), "%" + searchTerm.toLowerCase() + "%");
        Predicate descriptionPredicate = cb.like(cb.lower(product.get("description")), "%" + searchTerm.toLowerCase() + "%");
        cq.where(cb.or(namePredicate, descriptionPredicate));

        return entityManager.createQuery(cq).getResultList();
    }
}
