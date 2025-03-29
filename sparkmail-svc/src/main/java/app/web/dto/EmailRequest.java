package app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmailRequest {

    @NotNull
    private String userEmail;

    @NotBlank
    private String subject;

    @NotBlank
    private String body;
}
