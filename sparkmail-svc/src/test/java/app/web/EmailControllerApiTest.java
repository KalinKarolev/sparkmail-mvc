package app.web;

import app.model.Email;
import app.model.EmailStatus;
import app.service.EmailService;
import app.web.dto.EmailRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailController.class)
public class EmailControllerApiTest {

    @MockitoBean
    private EmailService emailService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenSendEmail_happyPath() throws Exception {
        Email email = new Email(UUID.randomUUID(), "user1@example.com", "Subject 1", LocalDateTime.now(), "Body 1", EmailStatus.FAILED);
        when(emailService.sendEmail(any())).thenReturn(email);

        EmailRequest emailRequest = new EmailRequest("test@example.com", "Subject", "Message");
        MockHttpServletRequestBuilder request = post("/api/v1/emails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailRequest));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("emailId").isNotEmpty())
                .andExpect(jsonPath("userEmail").isNotEmpty())
                .andExpect(jsonPath("subject").isNotEmpty())
                .andExpect(jsonPath("body").isNotEmpty())
                .andExpect(jsonPath("createdOn").isNotEmpty());
    }

    @Test
    void whenRetrieveEmailsInStatusFailed_happyPath() throws Exception {
        Email email1 = new Email(UUID.randomUUID(), "user1@example.com", "Subject 1", LocalDateTime.now(), "Body 1", EmailStatus.FAILED);
        Email email2 = new Email(UUID.randomUUID(), "user2@example.com", "Subject 2", LocalDateTime.now(), "Body 2", EmailStatus.FAILED);

        List<Email> emails = List.of(email1, email2);
        when(emailService.findAllEmailsInStatusFailed()).thenReturn(emails);

        mockMvc.perform(get("/api/v1/emails"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("[0].emailId").isNotEmpty())
                .andExpect(jsonPath("[0].userEmail").isNotEmpty())
                .andExpect(jsonPath("[0].subject").isNotEmpty())
                .andExpect(jsonPath("[0].body").isNotEmpty())
                .andExpect(jsonPath("[0].createdOn").isNotEmpty())
                .andExpect(jsonPath("[1].emailId").isNotEmpty())
                .andExpect(jsonPath("[1].userEmail").isNotEmpty())
                .andExpect(jsonPath("[1].subject").isNotEmpty())
                .andExpect(jsonPath("[1].body").isNotEmpty())
                .andExpect(jsonPath("[1].createdOn").isNotEmpty());
    }

    @Test
    void whenDeleteFailedEmails_happyPath() throws Exception {
        MockHttpServletRequestBuilder request = delete("/api/v1/emails/failed")
                .param("emailId", UUID.randomUUID().toString());

        mockMvc.perform(request)
                .andExpect(status().isOk());
        verify(emailService).deleteFailedEmail(any());
    }

    @Test
    void whenSendRequestToInvalidEndpoint_return404WithErrorMessage() throws Exception {
        mockMvc.perform(get("/api/v1/emails/invalid"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("status").value(404))
                .andExpect(jsonPath("message").value("Not supported application endpoint!"));
    }

    @Test
    void whenUnhandledExceptionThrown_thenReturnsInternalServerError() throws Exception {
        when(emailService.sendEmail(any())).thenThrow(new RuntimeException("Unexpected error"));
        mockMvc.perform(post("/api/v1/emails"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("status").value(500))
                .andExpect(jsonPath("message").value("An internal error occurred. Please retry your request."));
    }
}
