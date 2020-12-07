package uz.ecma.queueserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;
import uz.ecma.queueserver.entity.Company;
import uz.ecma.queueserver.entity.Direction;
import uz.ecma.queueserver.entity.OperatorDirection;
import uz.ecma.queueserver.entity.Queue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OperatorDirectionRepository extends JpaRepository<OperatorDirection, UUID> {
    List<OperatorDirection> findAllByDirection_Company(Company direction_company);

    @Query(nativeQuery = true, value = "SELECT * FROM operator_direction WHERE user_id=:userId AND active=true ")
    OperatorDirection getDirectionId(UUID userId);

    @Query(nativeQuery = true, value = "SELECT * FROM operator_direction WHERE direction_id in(select id from direction where company_id=:company_id)")
    List<OperatorDirection> getOperatorList(UUID company_id);

    boolean existsByUserId(UUID user_id);

    Optional<OperatorDirection> findByUserId(UUID user_id);

    Integer countByDirection_IdAndActive(Integer direction_id, boolean active);




}