package uz.ecma.queueserver.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.ecma.queueserver.entity.Aware;
import uz.ecma.queueserver.entity.Region;

@Projection(types = Aware.class)
public interface CustomAware {
    Integer getId();

    String getNameUzl();
    String getNameUzk();

    String getNameRu();

    String getNameEn();
}
