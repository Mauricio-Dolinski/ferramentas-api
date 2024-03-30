package com.dolinski.mauricio.api.controller;

import io.quarkus.test.junit.QuarkusTest;

import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;

@QuarkusTest
public class CnhControllerTest {

    @Test
    public void deveriaGerarCnh() {
        given()
                .when().get("/cnh/gerar")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("gerado"));
    }

    @Test
    public void deveriaValidarCnh() {
        given()
                .param("cnh", "123")
                .when().post("/cnh/validar")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("valido"));
    }
} 