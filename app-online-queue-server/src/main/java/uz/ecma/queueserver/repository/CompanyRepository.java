package uz.ecma.queueserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.ecma.queueserver.entity.Company;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    @Query(value = "select * from company c where (select lower(names) from complains) like concat('%', lower(c.name), '%')", nativeQuery = true)
    List<Company> getCompanyByComplainsHas();

    boolean existsByNameEqualsIgnoreCase(String name);

    boolean existsByTin(String tin);

    @Query(value = "select c.id, c.created_at, c.created_by, c.updated_at, c.update_by, c.active, c.count_rate, c.name, c.rate_amount, c.tin, c.category_id, c.contact_id, c.logo_id from company c inner join direction d on c.id=d.company_id where d.id=:directionId", nativeQuery = true)
    Optional<Company> getCompanyByDirectionId(Integer directionId);

    Page<Company> findAllByCategoryIdAndActive(Integer category_id, boolean active, Pageable pageable);

    Page<Company> findAllByCategoryId(Integer id, Pageable pageable);

    boolean existsByNameEqualsIgnoreCaseAndTinAndIdNot(String name, String tin, UUID id);

    Page<Company> findAllByNameContains(String name, Pageable pageable);

    Page<Company> findAllByActive(boolean active, Pageable pageable);

    Page<Company> findAllByActiveOrderByCreatedAt(boolean active, Pageable pageable);

    @Query(value = "select * from company where active=false ", nativeQuery = true)
    List<Company> getActiveFalseCompany();


}