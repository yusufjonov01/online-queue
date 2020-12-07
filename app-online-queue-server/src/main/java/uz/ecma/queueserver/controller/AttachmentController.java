package uz.ecma.queueserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.ecma.queueserver.payload.ApiResponse;
import uz.ecma.queueserver.service.AttachmentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {

    @Autowired
    AttachmentService attachmentService;

    @PostMapping("/upload")
    public HttpEntity<?> uploadFile(MultipartHttpServletRequest request) {
        UUID uuid = attachmentService.uploadFile(request);
        return
                ResponseEntity
                        .ok(
                                new String[]
                                        {ServletUriComponentsBuilder
                                                .fromCurrentContextPath()
                                                .path("/api/attachment/")
                                                .path(uuid.toString())
                                                .toUriString()
        });
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getFile(@PathVariable UUID id) {
        return attachmentService.getFile(id);
    }

}
