package org.example.springkart.project.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(){

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("JWT token based authentication");

        SecurityRequirement bearerRequirement = new SecurityRequirement().addList("Bearer Authentication");

        //now we will define actual open api object for swagger UI generation
        return new OpenAPI()
                .info(new Info()
                        .title("SpringKart APIs ")
                        .version("v1.0")
                        .description("")
                        .contact(new Contact().name("Harsh Rastogi").email("https://github.com/Kota-Kun22/")
                        .url("https://github.com/Kota-Kun22/SpringKart"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("SpringKart Wiki")
                        .url("https://github.com/Kota-Kun22/SpringKart"))
                .addSecurityItem(bearerRequirement)
                .components(new Components().addSecuritySchemes("Bearer Authentication", securityScheme));
    }
}
