package com.dolinski.mauricio.api.controller;

import jakarta.validation.ValidationException;

public interface DocumentoDTO {
    public String getNumero();
    public void setNumero(String numero);
    public void parse() throws ValidationException;
}
