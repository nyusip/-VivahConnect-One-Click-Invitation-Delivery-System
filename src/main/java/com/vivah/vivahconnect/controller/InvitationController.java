package com.vivah.vivahconnect.controller;

import java.io.File;
import java.io.IOException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(originPatterns = {"http://127.0.0.1:*", "http://localhost:*"})
@RestController
@RequestMapping("/invitation")
public class InvitationController {

    // Stores the last uploaded invitation path so GuestController can attach it to emails.
    public static String lastUploadedFilePath;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Save uploaded files inside the project-level uploads folder.
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File dir = new File(uploadDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filePath = uploadDir + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            // Keep the file path for the "send to all members" step.
            lastUploadedFilePath = filePath;

            return "File uploaded successfully";

        } catch (IOException e) {
            e.printStackTrace();
            return "Upload failed";
        }
    }
}
