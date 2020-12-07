package uz.ecma.queueserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.ecma.queueserver.entity.Attachment;
import uz.ecma.queueserver.entity.User;

import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment,UUID> {

}
