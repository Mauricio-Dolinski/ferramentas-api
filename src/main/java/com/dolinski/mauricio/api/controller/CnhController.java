package com.dolinski.mauricio.api.controller;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;

import com.dolinski.mauricio.api.dto.CnhDTO;
import com.dolinski.mauricio.api.service.DocumentoService;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;



@Path("/cnh")
public class CnhController {

    @Inject
    @Named("cnhService")
    private DocumentoService documento;
    
    @Tag(name="Gerador")
    @GET
    public RestResponse<String> gerar() {
        return documento.gerar();
    }
    
    @Tag(name="Validador")
    @POST
    public RestResponse<String> validar(@Valid @BeanParam CnhDTO dto) {
        return documento.validar(dto);
    }
} 
