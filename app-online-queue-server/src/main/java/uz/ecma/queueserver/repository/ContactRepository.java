package uz.ecma.queueserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.ecma.queueserver.entity.Company;
import uz.ecma.queueserver.entity.Contact;

import java.util.UUID;

public interface ContactRepository extends JpaRepository<Contact, UUID> {

    boolean existsByEmailEqualsIgnoreCase(String email);

    boolean existsByEmailEqualsIgnoreCaseAndIdNot(String email, UUID id);
}
