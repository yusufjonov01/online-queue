package uz.ecma.queueserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import uz.ecma.queueserver.entity.Category;
import uz.ecma.queueserver.projection.CustomCategory;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RepositoryRestResource(path = "category", collectionResourceRel = "list", excerptProjection = CustomCategory.class)
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(value = "SELECT cat.id FROM (SELECT c.id, COUNT(*) AS count_id FROM category c JOIN company s ON s.category_id = c.id GROUP BY c.id) AS cat  GROUP BY cat.id ORDER  BY MAX(cat.count_id) DESC limit 3", nativeQuery = true)
    List<Integer> getTopCategoryId();

    @Query(value = "select f.id, f.name_uzl, f.name_en, f.name_uzk, f.name_ru  from (SELECT c.id,c.name_uzl, c.name_en, c.name_ru, c.name_uzk, COUNT(*) AS count_id FROM category c JOIN company s ON s.category_id = c.id where s.active=true GROUP BY c.id) as f  group by f.id, f.name_uzl, f.name_en, f.name_uzk, f.name_ru order by max(f.count_id) desc limit 5", nativeQuery = true)
    List<Category> getCategoryForIndex();

    @Query(value = "select f.id, f.name_uzl, f.name_en, f.name_uzk, f.name_ru, f.count from (SELECT c.id,c.name_uzl, c.name_en, c.name_ru, c.name_uzk, COUNT(*) AS count FROM category c JOIN company s ON s.category_id = c.id where s.active=true GROUP BY c.id) as f  group by f.id, f.name_uzl, f.name_en, f.name_uzk, f.name_ru, f.count order by max(f.count) desc", nativeQuery = true)
    List<Category> getCategoryWithCountForIndex();
}
