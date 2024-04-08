package com.dolinski.mauricio.api.service;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

import com.dolinski.mauricio.api.dto.DocumentoDTO;

public class CnpjService implements DocumentoService {

    @Override
    public RestResponse<String> gerar() {
        return ResponseBuilder.ok("gerado").build();
    }

    @Override
    public RestResponse<String> validar(DocumentoDTO dto) {
        return ResponseBuilder.ok("valido").build();
    }
} 
