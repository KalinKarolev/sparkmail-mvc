package app.web;

import app.model.Email;
import app.service.EmailService;
import app.web.dto.EmailRequest;
import app.web.dto.EmailResponse;
import app.web.mapper.DtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/emails")
@Tag(name = "Email Management", description = "Operations related to emails")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService _emailService) {emailService = _emailService;}

    @PostMapping
    public ResponseEntity<EmailResponse> sendEmail(@RequestBody EmailRequest emailRequest) {
        Email email = emailService.sendEmail(emailRequest);

        EmailResponse response = DtoMapper.mapEmailToEmailResponse(email);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @GetMapping
    public ResponseEntity<List<EmailResponse>> retrieveEmailsInStatusFailed() {
        List<Email> failedEmails = emailService.findAllEmailsInStatusFailed();

        List<EmailResponse> responseList = failedEmails.stream()
                .map(DtoMapper::mapEmailToEmailResponse)
                .toList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseList);
    }

    @DeleteMapping("/failed")
    @Operation(summary = "Deletes previously failed emails.", description = "Deletes old emails that were not successfully sent.")
    public void deletedFailedEmail(@RequestParam(name ="emailId") UUID emailId) {
        emailService.deleteFailedEmail(emailId);
    }

}
