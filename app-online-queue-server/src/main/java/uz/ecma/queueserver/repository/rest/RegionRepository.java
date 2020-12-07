package uz.ecma.queueserver.repository.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import uz.ecma.queueserver.entity.Region;
import uz.ecma.queueserver.projection.CustomRegion;

import java.util.List;

@CrossOrigin
@RepositoryRestResource(path = "region",collectionResourceRel = "list",excerptProjection = CustomRegion.class)
public interface RegionRepository extends JpaRepository<Region,Integer> {
//    @RestResource(path = "nameUzl", rel = "nameUzl")
//    public Page findByNameUzl(@Param("name") String name, Pageable p);
}
