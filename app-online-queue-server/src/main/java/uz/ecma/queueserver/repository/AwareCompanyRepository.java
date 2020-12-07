package uz.ecma.queueserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.ecma.queueserver.entity.Aware;
import uz.ecma.queueserver.entity.AwareCompany;
import uz.ecma.queueserver.entity.Contact;

import java.util.Optional;
import java.util.UUID;

public interface AwareCompanyRepository extends JpaRepository<AwareCompany, UUID> {
    Optional<AwareCompany> findByContactAndAware(Contact contact, Aware aware);
    Optional<AwareCompany> findByContactIdAndAwareId(UUID contact_id, Integer aware_id);
}
