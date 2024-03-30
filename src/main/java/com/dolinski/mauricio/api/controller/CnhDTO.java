package com.dolinski.mauricio.api.controller;

import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.FormParam;

public class CnhDTO implements DocumentoDTO {
    @NotBlank(message = "Campo cnh não pode ser vazio")
    @FormParam("cnh")
    String numero;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public CnhDTO(String numero) {
        this.numero = numero;
    }

    public CnhDTO() {
    }

    public void parse() throws ValidationException {
		String cnh = this.numero;

        //parse

        this.numero = cnh;
    }
} 
