package uz.ecma.queueserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.ecma.queueserver.entity.Role;
import uz.ecma.queueserver.entity.WorkTime;
import uz.ecma.queueserver.entity.enums.WeekName;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkTimeRepository extends JpaRepository<WorkTime, UUID> {
    List<WorkTime> findAllByDirection_CompanyId(UUID direction_company_id);

    boolean existsByCompany_IdAndWeekName(UUID company_id, WeekName weekName);

    boolean existsByDirection_IdAndWeekName(Integer direction_id, WeekName weekName);

    Optional<WorkTime> findByCompany_IdAndWeekName(UUID company_id, WeekName weekName);

    Optional<WorkTime> findByDirection_IdAndWeekName(Integer direction_id, WeekName weekName);

    List<WorkTime> findByCompany_IdOrderByWeekName(UUID company_id);
}
