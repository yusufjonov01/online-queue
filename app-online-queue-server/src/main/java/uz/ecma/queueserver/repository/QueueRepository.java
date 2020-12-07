package uz.ecma.queueserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.ecma.queueserver.entity.Queue;
import uz.ecma.queueserver.entity.enums.QueueStatus;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface QueueRepository extends JpaRepository<Queue, UUID> {

    List<Queue> findByDirection_CompanyId(UUID direction_company_id);

    boolean existsByDirectionId(Integer direction_id);

    @Query(value = "SELECT count(id) from query where status=:status and direction_id in(SELECT id from direction where company_id=:id)", nativeQuery = true)
    Long getQueue(UUID id, String status);

    Optional<Queue> findByDirectionIdAndStatusAndClient_PhoneNumber(Integer direction_id, QueueStatus status, String client_phoneNumber);

    @Query(nativeQuery = true, value = "SELECT * FROM queue WHERE direction_id=:directionId AND status=:status ORDER BY created_at")
    Page<Queue> findAllByDirection(Integer directionId, String status, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM queue q WHERE DATE_TRUNC(:dataType, q.created_at)=DATE_TRUNC(:dataType, CURRENT_TIMESTAMP) and direction_id=:direction_id and q.status=:status ORDER BY q.created_at")
    Page<Queue> findAllByDirectionAndStatusAndDateType(Integer direction_id, String status, String dataType, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM queue q WHERE DATE_TRUNC(:dataType, q.created_at)=DATE_TRUNC(:dataType, CURRENT_TIMESTAMP) and client_id=:client_id and direction_id=:direction_id")
    Optional<Queue> existsByDirectionIdAndClientIdAndDateType(Integer direction_id, UUID client_id, String dataType);

    boolean existsByClient_idAndDirection_id(UUID client_id, Integer direction_id);

    @Query(nativeQuery = true, value = "SELECT * FROM queue WHERE direction_id=:directionId AND status=:status AND operator_id=:operator_id ORDER BY created_at")
    Page<Queue> findAllByDirectionAndOperatorIdAndStatus(Integer directionId, String status, Pageable pageable, UUID operator_id);

    @Query(nativeQuery = true, value = "SELECT * FROM queue WHERE direction_id=:directionId  AND operator_id=:operator_id ORDER BY created_at")
    Page<Queue> findAllByDirectionAndOperatorId(Integer directionId, Pageable pageable, UUID operator_id);

    @Query(nativeQuery = true, value = "select * from queue q inner join direction d on q.direction_id=d.id where d.company_id=:company_id and status=:status order by  d.id, q.queue_number")
    Page<Queue> findAllByStatusAndCompanyId(String status, UUID company_id, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from queue q inner join direction d on q.direction_id=d.id where d.company_id=:company_id order by  d.id, q.queue_number")
    Page<Queue> findAllCompanyId(UUID company_id, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM queue WHERE direction_id=:directionId AND status=:status ORDER BY created_at limit 50")
    List<Queue> findAllByDirectionLimit(Integer directionId, String status);

    @Query(nativeQuery = true, value = "SELECT * FROM queue WHERE status=:status")
    List<Queue> findAllByStatus(String status);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM queue WHERE direction_id=:id AND status=:status ")
    Integer getCountQueue(Integer id, String status);

    @Query(nativeQuery = true, value = "SELECT min(queue_number) FROM queue WHERE direction_id=:id AND  status='WAITING'")
    Integer getQueueMinWaitingNumber(Integer id);

    @Query(nativeQuery = true, value = "SELECT max(queue_number) FROM queue WHERE direction_id=:id AND  status='ACCEPTED'")
    Integer getQueueMaxAcceptedNumber(Integer id);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM queue WHERE direction_id=:id")
    Integer getCountAllQueue(Integer id);

    @Query(nativeQuery = true, value = "select count(*) from queue q where DATE_TRUNC(:dataType, q.created_at)=DATE_TRUNC(:dataType, CURRENT_TIMESTAMP) and direction_id=:direction_id")
    Integer getCountAllQueueByDirectionAndDateNameAndTimestamp(Integer direction_id, String dataType);

    @Query(nativeQuery = true, value = "select count(*) from queue q where DATE_TRUNC(:dataType, q.created_at)=DATE_TRUNC(:dataType, CURRENT_TIMESTAMP) and direction_id=:direction_id and q.status=:status")
    Integer getCountAllQueueByDirectionAndDateNameAndTimestampAndStatus(Integer direction_id, String dataType, String status);

    @Query(nativeQuery = true, value = "select * from queue q where DATE_TRUNC(:dataType, q.created_at)=DATE_TRUNC(:dataType, CURRENT_TIMESTAMP) and direction_id=:direction_id and q.status=:status")
    List<Queue> getAllQueueByDirectionAndDateNameAndTimestampAndStatus(Integer direction_id, String dataType, String status);

    @Query(nativeQuery = true, value = "select min(q.queue_number) from queue q where DATE_TRUNC(:dataType, q.created_at)=DATE_TRUNC(:dataType, CURRENT_TIMESTAMP) and direction_id=:direction_id and q.status=:status")
    Integer getMinQueueNumberByDirectionAndDateNameAndTimestampAndStatus(Integer direction_id, String dataType, String status);

    @Query(nativeQuery = true, value = "select max(q.queue_number) from queue q where DATE_TRUNC(:dataType, q.created_at)=DATE_TRUNC(:dataType, CURRENT_TIMESTAMP) and direction_id=:direction_id and q.status=:status")
    Integer getMaxQueueNumberByDirectionAndDateNameAndTimestampAndStatus(Integer direction_id, String dataType, String status);


    @Query(nativeQuery = true, value = "SELECT COUNT (*) FROM operator_direction WHERE direction_id=:id AND active=true ")
    Integer getCountOperator(Integer id);

    @Query(nativeQuery = true, value = "SELECT * FROM queue WHERE direction_id=:id AND status=:status ORDER BY created_at DESC limit 1")
    Queue getLastQueue(Integer id, String status);

    @Query(nativeQuery = true, value = "SELECT * FROM queue q WHERE DATE_TRUNC(:dataType, q.created_at)=DATE_TRUNC(:dataType, CURRENT_TIMESTAMP) and client_id=:client_id and direction_id=:direction_id")
    Optional<Queue> findByDirectionIdAndClientIdAndDateType(Integer direction_id, UUID client_id, String dataType);

    Optional<Queue> findByDirection_IdAndStatusAndOperator_Id(Integer direction_id, QueueStatus status, UUID operator_id);

    Optional<Queue> findByDirection_IdAndClient_IdAndStatus(Integer direction_id, UUID client_id, QueueStatus status);
    Optional<Queue> findByDirection_IdAndClient_Id(Integer direction_id, UUID client_id);
}