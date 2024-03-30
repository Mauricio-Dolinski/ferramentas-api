package com.dolinski.mauricio.api.controller;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

import com.dolinski.mauricio.api.service.CnhService;
import com.dolinski.mauricio.api.service.DocumentoService;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;



@Path("/cnh")
public class CnhController {
    private DocumentoService documento = new CnhService();
    
    @Tag(name="Gerador")
    @Path("/gerar")
    @GET
    public RestResponse<String> gerar() {
        return ResponseBuilder.ok(documento.gerar()).build();
    }
    
    @Tag(name="Validador")
    @Path("/validar")
    @POST
    public RestResponse<String> validar(@Valid @BeanParam CnpjDTO dto) {
        try {
            return ResponseBuilder.ok(documento.validar(dto)).build();
        } catch (ValidationException e) {
            return ResponseBuilder.create(RestResponse.Status.BAD_REQUEST, e.getMessage()).build();
        }
    }
} 
