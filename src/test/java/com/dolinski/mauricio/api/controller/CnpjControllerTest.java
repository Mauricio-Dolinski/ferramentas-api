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
                .when().get("/cnpj/gerar")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("gerado"));
    }

    @Test
    public void deveriaValidarCnpj() {
        given()
                .param("cnpj", "123")
                .when().post("/cnpj/validar")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("valido"));
    }
}