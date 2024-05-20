package com.dolinski.mauricio.api.service;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

import com.dolinski.mauricio.api.dto.DocumentoDTO;

import jakarta.validation.ValidationException;

public class CnhService implements DocumentoService {

    @Override
    public RestResponse<String> gerar() {
        return ResponseBuilder.ok("gerado").build();   
    }

    @Override
    public RestResponse<String> validar(DocumentoDTO dto) {

        try {
            dto.parse();
        } catch (ValidationException e) {
            return ResponseBuilder.create(RestResponse.Status.BAD_REQUEST, e.getMessage()).build();
        }

        String cnh = dto.getNumero();

        String response = "";
        int[] soma = somarDigitos(cnh);
        String digitosVerificadores = gerarDigitosVerificadores(soma);
        

        if (cnh.substring(9).equals(digitosVerificadores)){
            response = "CNH " + cnh + " é válido.";
        }
        else {
            if (digitosVerificadores.contains("-")){
                response = "CNH não é válido.";
            }
            else response = "CNH não é válido, digito verificador deveria ser " + digitosVerificadores + ".";
        }
        
        return ResponseBuilder.ok(response).build();
    }

    private int[] somarDigitos(String cnh){
        int digito = 0;
        int peso = 9;
        int[] soma = new int[]{0, 0};

        for (int i = 0; i < 9; i++, peso--) {
            digito = Character.getNumericValue(cnh.charAt(i));
            soma[0] += digito * peso;
            soma[1] += digito * (i+1);
        }
        
        return soma;
    }

    private String gerarDigitosVerificadores(int[] soma){
        String digitosVerificadores = "";
        int digito, sobra = 0;
        
        for (int i = 0; i < 2; i++) {
            digito = soma[i] % 11;
            if (digito >= 10) {
                digito = 0;
                if (sobra == 0) sobra = 2;
            }
            else digito = (digito - sobra);
            digitosVerificadores += "" + digito;
        }

        return digitosVerificadores;
    }
} 
