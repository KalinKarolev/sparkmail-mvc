package app.integration;

import app.model.Email;
import app.model.EmailStatus;
import app.repository.EmailRepository;
import app.service.EmailService;
import app.web.dto.EmailRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class EmailITest {

    @Autowired
    private MailSender mailSender;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmailService emailService;

    @Test
    void sendEmail_happyPath() {
        EmailRequest emailRequest = EmailRequest.builder()
                .userEmail("email@email.com")
                .subject("Test Subject")
                .body("Test Email Body")
                .build();
        Email email = emailService.sendEmail(emailRequest);

        Optional<Email> optionalEmail = emailRepository.findById(email.getId());
        assertTrue(optionalEmail.isPresent());
        Email savedEmail = optionalEmail.get();
        assertEquals(emailRequest.getUserEmail(), savedEmail.getUserEmail());
        assertEquals(emailRequest.getSubject(), savedEmail.getSubject());
        assertEquals(emailRequest.getBody(), savedEmail.getBody());
        assertEquals(EmailStatus.SUCCEEDED, savedEmail.getStatus());
        assertNotNull(savedEmail.getCreatedOn());
    }
}
