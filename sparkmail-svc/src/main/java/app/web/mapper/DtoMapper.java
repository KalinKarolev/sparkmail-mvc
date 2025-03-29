package app.web.mapper;

import app.model.Email;
import app.web.dto.EmailResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static EmailResponse mapEmailToEmailResponse(Email email) {
        return EmailResponse.builder()
                .emailId(email.getId())
                .userEmail(email.getUserEmail())
                .subject(email.getSubject())
                .body(email.getBody())
                .createdOn(email.getCreatedOn())
                .build();
    }
}
