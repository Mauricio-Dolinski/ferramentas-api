package com.dolinski.mauricio.api.resource;

import io.quarkus.test.junit.QuarkusTest;

import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;

@QuarkusTest
public class CpfResourceTest {

    @Test
    public void deveriaGerarCpf() {
        given()
                .when().get("/cpf/gerar")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("gerado"));
    }

    @Test
    public void deveriaValidarCpf() {
        given()
                .param("cpf", "123.456.789-09")
                .when().post("/cpf/validar")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CPF 123.456.789-09 é válido"));
    }

    @Test
    public void deveriaValidarCpf2() {
        given()
                .param("cpf", "12345678909")
                .when().post("/cpf/validar")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CPF 123.456.789-09 é válido"));
    }

    @Test
    public void deveriaValidarCpf3() {
        given()
                .param("cpf", "18.457.127-81")
                .when().post("/cpf/validar")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CPF 018.457.127-81 é válido"));
    }

    @Test
    public void naoDeveriaValidarCpf() {
        given()
                .param("cpf", "10987654321")
                .when().post("/cpf/validar")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CPF não é válido"));
    }

    @Test
    public void naoDeveriaValidarCpf2() {
        given()
                .param("cpf", "10987654321231231")
                .when().post("/cpf/validar")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CPF não é válido, deve conter 11 numeros"));
    }

    @Test
    public void naoDeveriaValidarCpf3() {
        given()
                .param("cpf", "231231")
                .when().post("/cpf/validar")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CPF não é válido, deve conter 11 numeros"));
    }

    @Test
    public void naoDeveriaValidarCpf4() {
        given()
                .param("cpf", "abcdefghijk")
                .when().post("/cpf/validar")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CPF não é válido, deve conter 11 numeros"));
    }
}