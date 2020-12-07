package uz.ecma.queueserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.ecma.queueserver.entity.Attachment;
import uz.ecma.queueserver.entity.AttachmentContent;

import java.util.UUID;

public interface AttachmentContentRepository extends JpaRepository<AttachmentContent,UUID> {
    AttachmentContent findByAttachmentId(UUID id);

}
