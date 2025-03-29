package app.web.mapper;

import app.model.Email;
import app.web.dto.EmailResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static EmailResponse mapEmailToEmailResponse(Email email) {
        return EmailResponse.builder()
            .subject(email.getSubject())
            .status(email.getStatus())
            .createdOn(email.getCreatedOn())
            .build();
    }
}
