package uz.ecma.queueserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.ecma.queueserver.entity.Complains;

import java.util.List;
import java.util.UUID;

public interface ComplainRepository extends JpaRepository<Complains, UUID> {
    @Query(value = "select * from complains c where c.names like concat('%', lower(:first), '%') or c.names like concat('%', lower(:last), '%') or c.names like concat('%', lower(:middle), '%');", nativeQuery = true)
    List<Complains> searchByUserName(String first, String last, String middle);

    @Query(value = "select * from complains limit 1", nativeQuery = true)
    Complains selectFirst();
}
