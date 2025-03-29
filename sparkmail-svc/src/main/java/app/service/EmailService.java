package app.service;

import app.exception.DomainException;
import app.model.Email;
import app.model.EmailStatus;
import app.repository.EmailRepository;
import app.web.dto.EmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class EmailService {

    private final MailSender mailSender;
    private final EmailRepository emailRepository;

    public EmailService(MailSender _mailSender, EmailRepository _emailRepository) {
        mailSender = _mailSender;
        emailRepository = _emailRepository;
    }

    public Email sendEmail(EmailRequest emailRequest) {
        String userEmail = emailRequest.getUserEmail();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject(emailRequest.getSubject());
        message.setText(emailRequest.getBody());

        Email email = Email.builder()
            .subject(emailRequest.getSubject())
            .body(emailRequest.getBody())
            .createdOn(LocalDateTime.now())
            .userEmail(userEmail)
            .build();

        try {
            mailSender.send(message);
            email.setStatus(EmailStatus.SUCCEEDED);
        } catch (Exception e) {
            email.setStatus(EmailStatus.FAILED);
            log.warn("Email to %s was not sent due to %s."
                         .formatted(userEmail, e.getMessage()));
        }

        return emailRepository.save(email);
    }

    public void deleteFailedEmail(UUID emailId) {
        Email email = findEmailById(emailId);
        emailRepository.delete(email);
        log.info("Email with id {} was resent and deleted.", emailId);
    }

    public Email findEmailById(UUID emailId) {
        return emailRepository.findById(emailId)
                .orElseThrow(() -> new DomainException("No spark found with ID: " + emailId));
    }

    public List<Email> findAllEmailsInStatusFailed() {
        return emailRepository.findAllByStatus(EmailStatus.FAILED);
    }
}
