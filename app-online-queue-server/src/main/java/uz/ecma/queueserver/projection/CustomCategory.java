package uz.ecma.queueserver.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.ecma.queueserver.entity.Aware;
import uz.ecma.queueserver.entity.Category;

@Projection(types = Category.class)
public interface CustomCategory {
    Integer getId();

    String getNameUzl();

    String getNameUzk();

    String getNameRu();

    String getNameEn();

}
