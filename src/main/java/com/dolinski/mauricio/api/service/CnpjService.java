package com.dolinski.mauricio.api.service;

import com.dolinski.mauricio.api.controller.DocumentoDTO;

import jakarta.validation.ValidationException;

public class CnpjService implements DocumentoService {

    @Override
    public String gerar() {
        return "gerado";
    }

    @Override
    public String validar(DocumentoDTO dto) throws ValidationException {
        return "valido";
    }
} 
