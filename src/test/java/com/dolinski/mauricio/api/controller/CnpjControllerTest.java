package com.dolinski.mauricio.api.controller;

import io.quarkus.test.junit.QuarkusTest;

import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;

@QuarkusTest
public class CnpjControllerTest {

    @Test
    public void deveriaGerarCnpj() {
        given()
                .when().get("/cnpj")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("-"));
    }

    @Test
    public void deveriaValidarCnpj() {
        given()
                .param("cnpj", "01.234.567/8912-51")
                .when().post("/cnpj")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("válido"));
    }

    @Test
    public void deveriaValidarCnpj2() {
        given()
                .param("cnpj", "01234567891251")
                .when().post("/cnpj")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("válido"));
    }

    @Test
    public void naoDeveriaValidarCnpj() {
        given()
                .param("cnpj", "01.234.567/8912-34")
                .when().post("/cnpj")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CNPJ não é válido, digito verificador deveria ser "));
    }

    @Test
    public void naoDeveriaValidarCnpj2() {
        given()
                .param("cnpj", "01.234.567/8912-345")
                .when().post("/cnpj")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CNPJ não é válido, deve conter 14 numeros."));
    }

    @Test
    public void naoDeveriaValidarCnpj3() {
        given()
                .param("cnpj", "")
                .when().post("/cnpj")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("Campo cnpj não pode ser vazio"));
    }

    @Test
    public void naoDeveriaValidarCnpj4() {
        given()
                .param("cnpj", "abcdefghijklmn")
                .when().post("/cnpj")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CNPJ não é válido, deve conter 14 numeros."));
    }
} 