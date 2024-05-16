package com.dolinski.mauricio.api.service;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

import com.dolinski.mauricio.api.dto.DocumentoDTO;

import jakarta.validation.ValidationException;

public class CnpjService implements DocumentoService {

    @Override
    public RestResponse<String> gerar() {
        String cnpj = "";
        int digito = 0;
        int[] soma = new int[]{0, 0};
        int[] peso = new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        

        for (int i = 0; i < 12; i++) {
            digito = (int) (Math.random() * 9);
			soma[0] += digito * peso[i+1];
			soma[1] += digito * peso[i];
			cnpj += "" + digito;
        }

        for (int i = 0; i < 2; i++) {
			digito = soma[i] % 11;
			if ((digito == 0) || (digito == 1)) digito = 0;
            else digito = 11 - digito;
			cnpj += "" + digito;
			if (i == 0) soma[1] += digito * 2;
		}

        return ResponseBuilder.ok(cnpj).build();
    }

    @Override
    public RestResponse<String> validar(DocumentoDTO dto) {

        try {
            dto.parse();
        } catch (ValidationException e) {
            return ResponseBuilder.create(RestResponse.Status.BAD_REQUEST, e.getMessage()).build();
        }

        String cnpj = dto.getNumero();

        if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111")
                || cnpj.equals("22222222222222") || cnpj.equals("33333333333333")
                || cnpj.equals("44444444444444") || cnpj.equals("55555555555555")
                || cnpj.equals("66666666666666") || cnpj.equals("77777777777777")
                || cnpj.equals("88888888888888") || cnpj.equals("99999999999999"))
            return ResponseBuilder.ok("CNPJ não é válido").build();
        
        char dig13, dig14;
        int soma, resto, digito, peso;
        String response = "";
        Boolean valido = true;

        soma = 0;
        peso = 2;
        for (int i = 12; i > 0; i--) {
            digito = Integer.valueOf(cnpj.substring(i - 1, i)).intValue();
            soma += (digito * peso);
            peso++;
            if (peso == 10)
                peso = 2;
        }

        resto = soma % 11;
        if ((resto == 0) || (resto == 1))
            dig13 = '0';
        else
            dig13 = (char) ((11 - resto) + 48);
        
        if (dig13 != cnpj.charAt(12)) {
            valido = false;
            cnpj = cnpj.substring(0, 12) + dig13 + cnpj.substring(13);
        }

        soma = 0;
        peso = 2;
        for (int i = 13; i > 0; i--) {
            digito = Integer.valueOf(cnpj.substring(i - 1, i)).intValue();
            soma += (digito * peso);
            peso++;
            if (peso == 10)
                peso = 2;
        }
        resto = soma % 11;
        if ((resto == 0) || (resto == 1))
            dig14 = '0';
        else
            dig14 = (char) ((11 - resto) + 48);

        
        String resultado = "" + dig13 + dig14;
        if (valido && dig14 == cnpj.charAt(13) )
            response = "CNPJ " + format(cnpj) + " é válido.";
        else{
            response = "CNPJ não é válido, digito verificador deveria ser " + resultado + ".";
        }

        return ResponseBuilder.ok(response).build();
    }

    private String format(String cnpj){
		cnpj = cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8) + "/"
        + cnpj.substring(8, 12) + "-" + cnpj.substring(12);
		return cnpj;
	}
}
