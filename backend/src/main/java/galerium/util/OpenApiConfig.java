package galerium.util;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Galerium")
                        .version("1.0.0")
                        .description("This API manages an photography gallery platform that connects " +
                                "photographers and clients. " +
                                "It includes features for managing photographers, clients, photos and galleries.\n\n"
                        )
                        .contact(new Contact()
                                .name("Repository - Galerium")
                                .url("https://github.com/AidaG91/Galerium"))
                        .license(new License()
                                .name("Licencia MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
