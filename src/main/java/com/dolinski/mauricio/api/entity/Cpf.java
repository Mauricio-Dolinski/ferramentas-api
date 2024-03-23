package com.dolinski.mauricio.api.entity;

public class Cpf implements Documento{
    
    @Override
    public String gerar() {
        int[] digitos = new int[11];
		int soma1 = 0;
		int soma2 = 0;
		String gerado = "";

        for (int i = 0; i < 9; i++) {
            digitos[i] = (int) (Math.random() * 9);
			soma1 += digitos[i] * (i+1);
			soma2 += digitos[i] * i;
        }

		digitos[9] = soma1 % 11;
		if (digitos[9] == 10) digitos[9] = 0;

		soma2 += digitos[9] * 9;

		digitos[10] = soma2 % 11;
		if (digitos[10] == 10) digitos[9] = 0;

		for (int digito : digitos) {
			gerado += "" + digito;
		}
 
        return gerado;
    }

    @Override
    public String validar(String cpf) {
        cpf = cpf.trim();
        cpf = cpf.replaceAll("\\.", "");
        cpf = cpf.replaceAll("-", "");
        if (cpf.length() == 10 && cpf.matches("[0-9]+")){
            cpf = "0" + cpf;
        }
        if (cpf.length() != 11 || !cpf.matches("[0-9]+")){
            return "CPF não é válido, deve conter 11 numeros";
        }
        else {

            // Validação do CPF
            int d1, d2;
            int digito1, digito2, resto;
            int digitoCPF;
            String nDigResult;

            d1 = d2 = 0;
            digito1 = digito2 = resto = 0;

            for (int i = 1; i < cpf.length() - 1; i++) {
                digitoCPF = Integer.valueOf(cpf.substring(i - 1, i)).intValue();

                // multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4
                // e assim por diante.
                d1 = d1 + (11 - i) * digitoCPF;

                // para o segundo digito repita o procedimento incluindo o primeiro
                // digito calculado no passo anterior.
                d2 = d2 + (12 - i) * digitoCPF;
            };

            // Primeiro resto da divisão por 11.
            resto = (d1 % 11);

            // Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11
            // menos o resultado anterior.
            if (resto < 2)
                digito1 = 0;
            else
                digito1 = 11 - resto;

            d2 += 2 * digito1;

            // Segundo resto da divisão por 11.
            resto = (d2 % 11);

            // Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11
            // menos o resultado anterior.
            if (resto < 2)
                digito2 = 0;
            else
                digito2 = 11 - resto;

            // Digito verificador do CPF que está sendo validado.
            String nDigVerific = cpf.substring(cpf.length() - 2, cpf.length());

            // Concatenando o primeiro resto com o segundo.
            nDigResult = String.valueOf(digito1) + String.valueOf(digito2);

            // comparar o digito verificador do cpf com o primeiro resto + o segundo
            // resto.
            if (nDigVerific.equals(nDigResult)){
                cpf = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
            }
            else{
                return "CPF não é válido";
            }
        }
        cpf = cpf.substring(0, 3) + "." + 
              cpf.substring(4, 7) + "." +
              cpf.substring(8, 11) + "-" +
              cpf.substring(12);
              
        return "CPF " + cpf + " é válido";
    }
}