package com.dolinski.mauricio.api.service;

import com.dolinski.mauricio.api.controller.DocumentoDTO;

import jakarta.validation.ValidationException;


public class CpfService implements DocumentoService{

	private int digito;
	private int[] soma = new int[2];
    
    @Override
    public String gerar() {
		String cpf = "";
        digito = soma[0] = soma[1] = 0;

        for (int i = 0; i < 9; i++) {
            digito = (int) (Math.random() * 9);
			soma[0] += digito * (i+1);
			soma[1] += digito * i;
			cpf += "" + digito;
        }

		for (int i = 0; i < 2; i++) {
			digito = soma[i] % 11;
			if (digito == 10) digito = 0;
			cpf += "" + digito;
			if (i == 0) soma[1] += digito * 9;
		}
 
        return format(cpf);
    }

    @Override
    public String validar(DocumentoDTO dto) throws ValidationException {

        dto.parse();
        String cpf = dto.getNumero();

        int[] digitoVerificador = new int[2];
        int resto = 0;
        String resultado;
        String verificador = cpf.substring(cpf.length() - 2, cpf.length());;
        digito = soma[0] = soma[1] = 0;

        for (int i = 1; i < cpf.length() - 1; i++) {
            digito = Integer.valueOf(cpf.substring(i - 1, i)).intValue();
            soma[0] += digito * (11 - i);
            soma[1] += digito * (12 - i);
        };

        for (int i = 0; i < 2; i++) {
            resto = soma[i] % 11;
            if (resto <= 1)
                digitoVerificador[i] = 0;
            else
                digitoVerificador[i] = 11 - resto;
            if (i == 0) soma[1] += 2 * digitoVerificador[0];
        }

        resultado = String.valueOf(digitoVerificador[0]) + String.valueOf(digitoVerificador[1]);
        if (verificador.equals(resultado)){
            return "CPF " + format(cpf) + " é válido.";
        }
        else{
            return "CPF não é válido, digito verificador deveria ser " + resultado + ".";
        }
    }

	private String format(String cpf){
		cpf = cpf.substring(0, 3) + "." + 
               cpf.substring(3, 6) + "." +
               cpf.substring(6, 9) + "-" +
               cpf.substring(9);
		return cpf;
	}
}