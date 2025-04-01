package app.web.mapper;

import app.model.Email;
import app.web.dto.EmailResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DtoMapperUTest {

    @Test
    public void testMapEmailToEmailResponse() {
        LocalDateTime cratedDate = LocalDateTime.of(2010, 3, 29, 12, 0, 0);
        Email email = Email.builder()
                .id(UUID.randomUUID())
                .userEmail("email@email.com")
                .subject("subject")
                .body("body")
                .createdOn(cratedDate)
                .build();
        EmailResponse result = DtoMapper.mapEmailToEmailResponse(email);

        assertEquals(email.getId(), result.getEmailId());
        assertEquals(email.getUserEmail(), result.getUserEmail());
        assertEquals(email.getSubject(), result.getSubject());
        assertEquals(email.getBody(), result.getBody());
        assertEquals(email.getCreatedOn(), result.getCreatedOn());
    }
}
