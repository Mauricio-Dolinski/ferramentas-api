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
                .assertThat().body(containsString("gerado"));
    }

    @Test
    public void deveriaValidarCnpj() {
        given()
                .param("cnpj", "72.379.775/0001-20")
                .when().post("/cnpj")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("válido"));
    }
} 