package com.dolinski.mauricio.api.controller;

import io.quarkus.test.junit.QuarkusTest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.matchesPattern;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;

@QuarkusTest
public class CpfControllerTest {

    
    @Test
    public void deveriaGerarCpf() {
        given()
                .when().get("/cpf")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("-"));
    }

    @Test
    public void deveriaGerarCpfComFormatoValido() {
        given()
                .when().get("/cpf")
                .then()
                .statusCode(200)
                .assertThat().body(matchesPattern("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}"));
    }

    @Test
    public void deveriaValidarCpf() {
        given()
                .param("cpf", "123.456.789-09")
                .when().post("/cpf")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CPF 123.456.789-09 é válido."));
    }

    @Test
    public void deveriaValidarCpfComEspacos() {
        given()
                .param("cpf", " 123.456.789-09 ")
                .when().post("/cpf")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CPF 123.456.789-09 é válido."));
    }

    @Test
    public void deveriaValidarCpf2() {
        given()
                .param("cpf", "12345678909")
                .when().post("/cpf")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CPF 123.456.789-09 é válido."));
    }

    @Test
    public void deveriaValidarCpf3() {
        given()
                .param("cpf", "18.457.127-81")
                .when().post("/cpf")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CPF 018.457.127-81 é válido."));
    }

    @Test
    public void naoDeveriaValidarCpfComDigitoVerificadorErrado() {
        given()
                .param("cpf", "12345678910")
                .when().post("/cpf")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CPF não é válido, digito verificador deveria ser 09."));
    }

    @Test
    public void naoDeveriaValidarCpfComMaisDeOnzeNumeros() {
        given()
                .param("cpf", "1098765432123456")
                .when().post("/cpf")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CPF não é válido, deve conter 11 numeros."));
    }

    @Test
    public void naoDeveriaValidarCpfComMenosDeOnzeNumeros() {
        given()
                .param("cpf", "123456")
                .when().post("/cpf")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CPF não é válido, deve conter 11 numeros."));
    }

    @Test
    public void naoDeveriaValidarCpf4() {
        given()
                .param("cpf", "abcdefghijk")
                .when().post("/cpf")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CPF não é válido, deve conter 11 numeros."));
    }

    @Test
    public void naoDeveriaValidarCpfComLetras() {
        given()
                .param("cpf", "123.456.78A-09")
                .when().post("/cpf")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CPF não é válido, deve conter 11 numeros."));
    }

    @Test
    public void naoDeveriaValidarCpfComCaracteresEspeciais() {
        given()
                .param("cpf", "123.456.789-0@")
                .when().post("/cpf")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CPF não é válido, deve conter 11 numeros."));
    }

    @Test
    public void naoDeveriaValidarCpfComTodosNumerosIguais() {
        given()
                .param("cpf", "111.111.111-11")
                .when().post("/cpf")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CPF não é válido"));
    }

    @Test
    public void deveriaLidarComRequisicaoSemParametroCpf() {
        given()
                .when().post("/cpf")
                .then()
                .statusCode(400);
    }
}