package com.dolinski.mauricio.api.controller;

import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.FormParam;

public class CnpjDTO implements DocumentoDTO {
    @NotBlank(message = "Campo cnpj não pode ser vazio")
    @FormParam("cnpj")
    String numero;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public CnpjDTO(String numero) {
        this.numero = numero;
    }

    public CnpjDTO() {
    }

    public void parse() throws ValidationException {
		String cnpj = this.numero;

        //parse

        this.numero = cnpj;
    }
} 
