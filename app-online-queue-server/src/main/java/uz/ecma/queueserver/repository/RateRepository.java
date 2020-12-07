package uz.ecma.queueserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.ecma.queueserver.entity.Queue;
import uz.ecma.queueserver.entity.Rate;

import java.util.UUID;

public interface RateRepository extends JpaRepository<Rate,UUID> {

}
