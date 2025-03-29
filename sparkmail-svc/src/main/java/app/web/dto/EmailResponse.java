package app.web.dto;

import java.time.LocalDateTime;

import app.model.EmailStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailResponse {

    private String subject;

    private LocalDateTime createdOn;

    private EmailStatus status;
}
