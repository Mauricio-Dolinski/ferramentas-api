package com.dolinski.mauricio.api.service;

import jakarta.xml.bind.ValidationException;

public interface Documento {
    public String gerar();
    public String validar(String numero) throws ValidationException;
}
