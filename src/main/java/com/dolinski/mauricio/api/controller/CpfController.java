package com.dolinski.mauricio.api.controller;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;

import com.dolinski.mauricio.api.dto.CpfDTO;
import com.dolinski.mauricio.api.service.CpfService;
import com.dolinski.mauricio.api.service.DocumentoService;

import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;



@Path("/cpf")
public class CpfController {
    private DocumentoService documento = new CpfService();
    
    @Tag(name="Gerador")
    @GET
    public RestResponse<String> gerar() {
        return documento.gerar();
    }
    
    @Tag(name="Validador")
    @POST
    public RestResponse<String> validar(@Valid @BeanParam CpfDTO dto) {
        return documento.validar(dto);
    }
}

