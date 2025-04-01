package app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EmailRequest {

    @NotNull
    private String userEmail;

    @NotBlank
    private String subject;

    @NotBlank
    private String body;
}
