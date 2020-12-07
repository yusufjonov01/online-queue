package uz.ecma.queueserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.ecma.queueserver.entity.Company;
import uz.ecma.queueserver.entity.Direction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DirectionRepository extends JpaRepository<Direction, Integer> {
    List<Direction> findAllByCompanyId(UUID uuid);

    Optional<Direction> findByCompanyAndNameUzlEqualsIgnoreCase(Company company, String nameUzl);

    Integer countAllByCompany_Id(UUID company_id);

    Optional<Direction> findByIdAndActive(Integer id, boolean active);

    boolean existsByNameUzlEqualsIgnoreCaseOrNameUzkEqualsIgnoreCaseOrNameRuEqualsIgnoreCaseOrNameEnEqualsIgnoreCaseAndCompany(String nameUzl, String nameUzk, String nameRu, String nameEn, Company company);

    boolean existsByNameUzlEqualsIgnoreCaseOrNameUzkEqualsIgnoreCaseOrNameRuEqualsIgnoreCaseOrNameEnEqualsIgnoreCaseAndCompanyAndIdNot(String nameUzl, String nameUzk, String nameRu, String nameEn, Company company, Integer id);

    boolean existsByCompanyAndIdNotAndNameUzlEqualsIgnoreCaseOrNameUzkEqualsIgnoreCaseOrNameRuEqualsIgnoreCaseOrNameEnEqualsIgnoreCase(Company company, Integer id, String nameUzl, String nameUzk, String nameRu, String nameEn);


    boolean existsByCompanyIdAndNameUzlAndIdNot(UUID company_id, String nameUzl, Integer id);

    boolean existsByCompanyIdAndNameUzlOrNameUzkOrNameRuOrNameEnAndIdNot(UUID company_id, String nameUzl, String nameUzk, String nameRu, String nameEn, Integer id);

    @Query(value = "select * from direction d where (select lower(names) from complains) like concat('%', lower(d.name_en), '%') or (select lower(names) from complains) like concat('%', lower(d.name_ru), '%') or (select lower(names) from complains) like concat('%', lower(d.name_uzl), '%') or (select lower(names) from complains) like concat('%', lower(d.name_uzk), '%')", nativeQuery = true)
    List<Direction> getDirectionByComplainsHas();

    boolean existsByNameUzlEqualsIgnoreCaseAndCompanyAndIdNot(String nameUzl, Company company, Integer id);

    boolean existsByNameUzkEqualsIgnoreCaseAndCompanyAndIdNot(String nameUzk, Company company, Integer id);

    boolean existsByNameRuEqualsIgnoreCaseAndCompanyAndIdNot(String nameRu, Company company, Integer id);

    boolean existsByNameEnEqualsIgnoreCaseAndCompanyAndIdNot(String nameEn, Company company, Integer id);


}