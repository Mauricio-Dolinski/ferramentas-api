package com.dolinski.mauricio.api.dto;

import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.FormParam;

public class CnpjDTO implements DocumentoDTO {
    @NotBlank(message = "CNPJ não pode ser vazio.")
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

        cnpj = cnpj.trim();
        if (cnpj.length() > 18) {
            throw new ValidationException("CNPJ não é válido, deve conter 14 numeros.");
        }
        cnpj = cnpj.replaceAll("\\/", "");
        cnpj = cnpj.replaceAll("\\.", "");
        cnpj = cnpj.replaceAll("-", "");

        if (cnpj.length() != 14 || !cnpj.matches("[0-9]+")){
            throw new ValidationException("CNPJ não é válido, deve conter 14 numeros.");
        }

        this.numero = cnpj;
    }
} 
