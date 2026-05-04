package com.vivah.vivahconnect.controller;

import com.vivah.vivahconnect.entity.Guest;
import com.vivah.vivahconnect.repository.GuestRepository;
import com.vivah.vivahconnect.service.EmailService;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(originPatterns = {"http://127.0.0.1:*", "http://localhost:*"})
@RestController
@RequestMapping("/guest")
public class GuestController {

    // Repository handles database operations for the Guest table.
    @Autowired
    private GuestRepository guestRepository;

    // Service handles plain email and invitation email sending.
    @Autowired
    private EmailService emailService;

    // Create a new guest/member record.
    @PostMapping("/add")
    public Map<String, Object> addGuest(@RequestBody Guest guest) {
        Guest saved = guestRepository.save(guest);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", saved);

        return response;
    }

    // Simple endpoint to test whether mail configuration is working.
    @GetMapping("/test-mail")
    public String testMail() {
        emailService.sendEmail(
            "s99565042@gmail.com",
            "Test Mail",
            "This is test"
        );

        return "Test mail sent";
    }

    // Read all guests so the frontend can render the member list.
    @GetMapping("/all")
    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    // Read a single guest by id for detail or edit-related use cases.
    @GetMapping("/{id}")
    public ResponseEntity<?> getGuestById(@PathVariable int id) {
        return guestRepository.findById(id)
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Guest not found."));
    }

    // Update only the editable fields of an existing guest.
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateGuest(@PathVariable int id, @RequestBody Guest updatedGuest) {
        return guestRepository.findById(id)
            .<ResponseEntity<?>>map(existingGuest -> {
                existingGuest.setName(updatedGuest.getName());
                existingGuest.setEmail(updatedGuest.getEmail());

                Guest savedGuest = guestRepository.save(existingGuest);
                return ResponseEntity.ok(savedGuest);
            })
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Guest not found."));
    }

    // Delete a guest by id.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteGuest(@PathVariable int id) {
        if (!guestRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Member not found.");
        }

        guestRepository.deleteById(id);
        return ResponseEntity.ok("Member removed successfully.");
    }

    // Extra delete route for frontend code that sends POST instead of DELETE.
    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteGuestWithPost(@PathVariable int id) {
        return deleteGuest(id);
    }

    // Send the uploaded invitation file to every saved guest.
    @GetMapping("/send")
    public ResponseEntity<String> sendEmails() {
        List<Guest> guests = guestRepository.findAll();
        if (guests.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("No guests found. Add guests before sending.");
        }

        // This path is filled by InvitationController after file upload.
        String filePath = InvitationController.lastUploadedFilePath;
        if (filePath == null || filePath.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Upload an invitation file before sending emails.");
        }

        File invitationFile = new File(filePath);
        if (!invitationFile.exists()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Uploaded invitation file was not found. Please upload it again.");
        }

        try {
            // Send the same uploaded invitation to each guest individually.
            for (Guest guest : guests) {
                emailService.sendEmailWithAttachment(
                    guest.getName(),
                    guest.getEmail(),
                    filePath
                );
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok("Emails with attachment sent successfully.");
    }
}
