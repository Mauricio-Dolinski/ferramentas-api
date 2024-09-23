package com.dolinski.mauricio.api.service;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

import com.dolinski.mauricio.api.dto.DocumentoDTO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.validation.ValidationException;

@Named("cnhService")
@ApplicationScoped
public class CnhService implements DocumentoService {

    @Override
    public RestResponse<String> gerar() {
        String cnhGerado = gerarDigitos();

        int[] soma = somarDigitos(cnhGerado);

        cnhGerado += gerarDigitosVerificadores(soma);

        if (cnhGerado.contains("-")){
            return gerar();
        }

        return ResponseBuilder.ok(cnhGerado).build(); 
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

    private String gerarDigitos(){
        String cnhGerado = "";
        int digito = 0;

        for (int i = 0; i < 9; i++) {
            if (i == 0) digito = 0;
            else digito = (int) (Math.random() * 9);
            cnhGerado += "" + digito;
        }

        return cnhGerado;
    }

    private int[] somarDigitos(String cnh){
        int digito = 0;
        int peso = 9;
        int[] soma = new int[]{0, 0};

        for (int i = 0; i < 9; i++, peso--) {
            if (cnh.isEmpty()){
                //if (i == 0) digito = 0;
                //else digito = (int) (Math.random() * 9);
                //cnhValido += "" + digito;
            }else {
                digito = Character.getNumericValue(cnh.charAt(i));
            }
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
