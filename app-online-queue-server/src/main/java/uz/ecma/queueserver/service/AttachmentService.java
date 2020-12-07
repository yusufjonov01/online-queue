package uz.ecma.queueserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.ecma.queueserver.entity.Attachment;
import uz.ecma.queueserver.entity.AttachmentContent;
import uz.ecma.queueserver.repository.AttachmentContentRepository;
import uz.ecma.queueserver.repository.AttachmentRepository;
import uz.ecma.queueserver.repository.CompanyRepository;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class AttachmentService {
    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepository;
    @Autowired
    CompanyRepository companyRepository;

    public UUID uploadFile(MultipartHttpServletRequest request) {
        try {
            Iterator<String> fileNames = request.getFileNames();
            while (fileNames.hasNext()) {
                MultipartFile file = request.getFile(fileNames.next());
                assert file != null;
                Attachment attachment = new Attachment(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getSize());
                Attachment savedAttachment = attachmentRepository.save(attachment);

                AttachmentContent attachmentContent = new AttachmentContent(
                        savedAttachment,
                        file.getBytes());
                attachmentContentRepository.save(attachmentContent);
                return savedAttachment.getId();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpEntity<?> getFile(UUID id) {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getAttachment"));
        AttachmentContent attachmentContent = attachmentContentRepository.findByAttachmentId(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(attachment.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + attachment.getName() + "\"")
                .body(attachmentContent.getContent());

    }

}
