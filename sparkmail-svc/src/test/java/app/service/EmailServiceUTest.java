package app.service;

import app.exception.EmailNotFoundException;
import app.model.Email;
import app.model.EmailStatus;
import app.repository.EmailRepository;
import app.web.dto.EmailRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceUTest {

    @Mock
    private MailSender mailSender;
    @Mock
    private EmailRepository emailRepository;

    @InjectMocks
    private EmailService emailService;

    private EmailRequest emailRequest;
    private Email email;
    private UUID emailId;

    @BeforeEach
    void setUp() {
        emailId = UUID.randomUUID();
        emailRequest = new EmailRequest("test@example.com", "Test Subject", "Test Body");
        email = Email.builder()
                .id(emailId)
                .userEmail("test@example.com")
                .subject("Test Subject")
                .body("Test Body")
                .createdOn(LocalDateTime.now())
                .status(EmailStatus.SUCCEEDED)
                .build();
    }

    @Test
    void givenValidEmailRequest_whenSendEmail_thenSavesSucceededEmail() {
        when(emailRepository.save(any(Email.class))).thenReturn(email);

        Email result = emailService.sendEmail(emailRequest);

        assertNotNull(result);
        assertEquals(EmailStatus.SUCCEEDED, result.getStatus());
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(emailRepository, times(1)).save(any(Email.class));
    }

    @Test
    void givenMailSenderFails_whenSendEmail_thenSavesFailedEmail() {
        doThrow(new RuntimeException("Mail error"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);

        emailService.sendEmail(emailRequest);

        verify(emailRepository).save(emailCaptor.capture());
        assertEquals(EmailStatus.FAILED, emailCaptor.getValue().getStatus());
    }

    @Test
    void givenValidEmailId_whenDeleteFailedEmail_thenDeletesEmail() {
        when(emailRepository.findById(emailId)).thenReturn(Optional.of(email));
        doNothing().when(emailRepository).delete(any(Email.class));

        emailService.deleteFailedEmail(emailId);

        verify(emailRepository, times(1)).findById(emailId);
        verify(emailRepository, times(1)).delete(email);
    }

    @Test
    void givenInvalidEmailId_whenDeleteFailedEmail_thenThrowsException() {
        when(emailRepository.findById(emailId)).thenReturn(Optional.empty());

        assertThrows(EmailNotFoundException.class, () -> emailService.deleteFailedEmail(emailId));
        verify(emailRepository, times(1)).findById(emailId);
        verify(emailRepository, never()).delete(any(Email.class));
    }

    @Test
    void givenValidEmailId_whenFindEmailById_thenReturnsEmail() {
        when(emailRepository.findById(emailId)).thenReturn(Optional.of(email));

        Email result = emailService.findEmailById(emailId);

        assertNotNull(result);
        assertEquals(emailId, result.getId());
        verify(emailRepository, times(1)).findById(emailId);
    }

    @Test
    void givenInvalidEmailId_whenFindEmailById_thenThrowsException() {
        when(emailRepository.findById(emailId)).thenReturn(Optional.empty());

        assertThrows(EmailNotFoundException.class, () -> emailService.findEmailById(emailId));
        verify(emailRepository, times(1)).findById(emailId);
    }

    @Test
    void whenFindAllEmailsInStatusFailed_thenReturnsListOfEmails() {
        List<Email> failedEmails = List.of(email);
        when(emailRepository.findAllByStatus(EmailStatus.FAILED)).thenReturn(failedEmails);

        List<Email> result = emailService.findAllEmailsInStatusFailed();

        assertEquals(1, result.size());
        verify(emailRepository, times(1)).findAllByStatus(EmailStatus.FAILED);
    }
}
