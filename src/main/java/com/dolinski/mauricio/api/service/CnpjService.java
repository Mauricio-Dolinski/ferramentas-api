package com.dolinski.mauricio.api.service;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

import com.dolinski.mauricio.api.dto.DocumentoDTO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.validation.ValidationException;

@Named("cnpjService")
@ApplicationScoped
public class CnpjService implements DocumentoService {

    @Override
    public RestResponse<String> gerar() {
        String cnpjGerado = gerarDigitos();

        int[] soma = somarDigitos(cnpjGerado);

        if (cnpjGerado.equals("000000000000")) return gerar();

        cnpjGerado += gerarDigitosVerificadores(soma);

        return ResponseBuilder.ok(format(cnpjGerado)).build();
    }

    @Override
    public RestResponse<String> validar(DocumentoDTO dto) {

        try {
            dto.parse();
        } catch (ValidationException e) {
            return ResponseBuilder.create(RestResponse.Status.BAD_REQUEST, e.getMessage()).build();
        }

        String cnpj = dto.getNumero();

        if (cnpj.equals("00000000000000"))
            return ResponseBuilder.ok("CNPJ não é válido").build();

        String response = "";
        int[] soma = somarDigitos(cnpj);
        String digitosVerificadores = gerarDigitosVerificadores(soma);

        if (cnpj.substring(12).equals(digitosVerificadores)){
            response = "CNPJ " + format(cnpj) + " é válido.";
        }
        else {
            response = "CNPJ não é válido, dígito verificador deveria ser " + digitosVerificadores + ".";
        }
        
        return ResponseBuilder.ok(response).build();
    }

    private String gerarDigitos(){
        String cnpjGerado = "";
        int digito = 0;

        for (int i = 0; i < 12; i++) {
            digito = (int) (Math.random() * 9);
            cnpjGerado += "" + digito;
        }

        return cnpjGerado;
    }

    private int[] somarDigitos(String cnpj){
        int digito = 0;
        int[] soma = new int[]{0, 0};
        int[] peso = new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        for (int i = 0; i < 12; i++) {
            if (cnpj.isEmpty()){
                //digito = (int) (Math.random() * 9);
                //cnpjValido += "" + digito;
            }
            else {
                digito = Integer.valueOf(cnpj.substring(i, i+1)).intValue();
            }
            
            soma[0] += digito * peso[i+1];
            soma[1] += digito * peso[i];
        }

        return soma;
    }

    private String gerarDigitosVerificadores(int[] soma){
        String digitosVerificadores = "";
        int digito = 0;
        
        for (int i = 0; i < 2; i++) {
            digito = soma[i] % 11;
            if ((digito == 0) || (digito == 1)) digito = 0;
            else digito = 11 - digito;
            digitosVerificadores += "" + digito;
            if (i == 0) soma[1] += digito * 2;
        }

        return digitosVerificadores;
    }

    private String format(String cnpj){
		cnpj = cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8) + "/"
        + cnpj.substring(8, 12) + "-" + cnpj.substring(12);
		return cnpj;
	}
}
