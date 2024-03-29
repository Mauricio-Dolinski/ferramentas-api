package com.dolinski.mauricio.api.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.FormParam;
import jakarta.xml.bind.ValidationException;

public class CpfDTO implements DocumentoDTO {
    @NotBlank(message = "Campo cpf não pode ser vazio")
    @FormParam("cpf")
    String numero;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public CpfDTO(String numero) {
        this.numero = numero;
    }

    public CpfDTO() {
    }

    public void parse() throws ValidationException {
		String cpf = this.numero;
        cpf = cpf.trim();
        if (cpf.length() > 14) {
            throw new ValidationException("CPF não é válido, deve conter 11 numeros.");
        }
           
        cpf = cpf.replaceAll("\\.", "");
        cpf = cpf.replaceAll("-", "");

        if (cpf.length() == 10){
            cpf = "0" + cpf;
        }

        if (cpf.length() != 11 || !cpf.matches("[0-9]+")){
            throw new ValidationException("CPF não é válido, deve conter 11 numeros.");
        }

        this.numero = cpf;
    }
}
