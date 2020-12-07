package uz.ecma.queueserver.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import uz.ecma.queueserver.entity.District;
import uz.ecma.queueserver.entity.Region;

@Projection(types = District.class)
public interface CustomDistrict {
    Integer getId();

    String getNameUzl();

    String getNameUzk();

    String getNameRu();

    String getNameEn();

    Region getRegion();

    @Value("#{target.region.id}")
    Integer getRegionId();
}
