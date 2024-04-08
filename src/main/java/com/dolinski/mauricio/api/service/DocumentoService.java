package com.dolinski.mauricio.api.service;

import org.jboss.resteasy.reactive.RestResponse;

import com.dolinski.mauricio.api.dto.DocumentoDTO;



public interface DocumentoService {
    public RestResponse<String> gerar();
    public RestResponse<String> validar(DocumentoDTO dto);
}
