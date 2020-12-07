package uz.ecma.queueserver.repository.rest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import uz.ecma.queueserver.entity.District;
import uz.ecma.queueserver.projection.CustomDistrict;

import java.util.List;

@CrossOrigin
@RepositoryRestResource(path = "district", collectionResourceRel = "list", excerptProjection = CustomDistrict.class)
public interface DistrictRepository extends JpaRepository<District, Integer> {
    @RestResource(path = "/byRegion")
    List<District> findAllByRegionId(@Param("id") Integer id);
}
