package com.dolinski.mauricio.api.service;

import com.dolinski.mauricio.api.controller.DocumentoDTO;

import jakarta.validation.ValidationException;



public interface DocumentoService {
    public String gerar();
    public String validar(DocumentoDTO dto) throws ValidationException;
}
