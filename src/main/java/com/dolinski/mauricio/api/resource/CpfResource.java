package com.dolinski.mauricio.api.resource;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.dolinski.mauricio.api.entity.Cpf;
import com.dolinski.mauricio.api.entity.Documento;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/cpf")
public class CpfResource {
    private Documento documento = new Cpf();
    
    @Tag(name="Gerar")
    @GET
    public String gerar() {
        return documento.gerar();
    }
    
    @Tag(name="Validar")
    @POST
    public String validar(@FormParam("cpf") String numero) {
        return documento.validar(numero);
    }
}

