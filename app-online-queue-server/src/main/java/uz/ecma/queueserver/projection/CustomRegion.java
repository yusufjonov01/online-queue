package uz.ecma.queueserver.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.ecma.queueserver.entity.Region;

@Projection(types = Region.class)
public interface CustomRegion {
    Integer getId();
    String getNameUzl();
    String getNameUzk();
    String getNameRu();
    String getNameEn();
}
