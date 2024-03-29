package com.dolinski.mauricio.api.service;

import jakarta.xml.bind.ValidationException;

public interface DocumentoService {
    public String gerar();
    public String validar(String numero) throws ValidationException;
}
