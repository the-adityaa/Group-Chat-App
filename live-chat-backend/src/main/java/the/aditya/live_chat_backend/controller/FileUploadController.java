package the.aditya.live_chat_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import the.aditya.live_chat_backend.payload.UploadResponse;
import the.aditya.live_chat_backend.service.FileStorageService;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private final FileStorageService fileStorageService;

    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        String contentType = file.getContentType();

        if (contentType == null) {
            return ResponseEntity.badRequest().build();
        }

        Set<String> imageTypes = Set.of(
                "image/jpeg",
                "image/png",
                "image/webp"
        );

        if (imageTypes.contains(contentType)) {

            String url = fileStorageService.saveFile(file, "images");

            return ResponseEntity.ok(
                    new UploadResponse(
                            url,
                            file.getOriginalFilename(),
                            "IMAGE"
                    )
            );
        }

        if (contentType.equals("application/pdf")) {

            String url = fileStorageService.saveFile(file, "pdfs");

            return ResponseEntity.ok(
                    new UploadResponse(
                            url,
                            file.getOriginalFilename(),
                            "PDF"
                    )
            );
        }

        return ResponseEntity.badRequest().build();
    }
}