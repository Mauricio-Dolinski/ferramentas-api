package com.dolinski.mauricio.api;

import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;

import jakarta.ws.rs.core.Application;

@OpenAPIDefinition(
    tags = {
        @Tag(name="Gerador", description="Geração de números de documentos"),
        @Tag(name="Validador", description="Validação de números de documentos")
    },
    info = @Info(
        title="Ferramentas API",
        description="API com ferramentas para validação e geração de documentos comuns no Brasil.",
        version = "1.0.0",
        contact = @Contact(
            name = "Mauricio Dolinski",
            email = "mauriciompd@gmail.com"))
)
public class OpenApi extends Application {
}
