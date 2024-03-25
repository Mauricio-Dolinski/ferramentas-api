package com.dolinski.mauricio.api.controller;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

import com.dolinski.mauricio.api.service.Cpf;
import com.dolinski.mauricio.api.service.Documento;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.xml.bind.ValidationException;


@Path("/cpf")
public class CpfController {
    private Documento documento = new Cpf();
    
    @Tag(name="Gerador")
    @Path("/gerar")
    @GET
    public RestResponse<String> gerar() {
        return ResponseBuilder.ok(documento.gerar()).build();
    }
    
    @Tag(name="Validador")
    @Path("/validar")
    @POST
    public RestResponse<String> validar(@FormParam("cpf") String numero) {
        try {
            return ResponseBuilder.ok(documento.validar(numero)).build();
        } catch (ValidationException e) {
            return ResponseBuilder.create(RestResponse.Status.BAD_REQUEST, e.getMessage()).build();
        }
    }
}

