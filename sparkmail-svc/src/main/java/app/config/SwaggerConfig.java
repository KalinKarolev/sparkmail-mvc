package app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info applicationInfo = new Info()
                .title("SparkMail REST API")
                .description("REST API designed to operate as the email service for SparkFund App. "
                        + "This service handles email notifications triggered by donation events, "
                        + "such as notifying Spark owners when a new donation is made. "
                        + "It also supports re-sending failed emails and deletes old emails "
                        + "that were not successfully sent.")
                .version("1.0.0")
                .contact(new Contact()
                        .name("Kalin Karolev")
                        .email("kalin.karolev@gmail.com")
                        .url("https://www.linkedin.com/in/kalin-karolev-6b6b8a113/"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));

        return new OpenAPI().info(applicationInfo);
    }
}
