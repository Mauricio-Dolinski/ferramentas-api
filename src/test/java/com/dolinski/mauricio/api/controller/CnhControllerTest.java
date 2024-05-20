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
                .when().get("/cnh")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("0"));
    }

    @Test
    public void deveriaValidarCnh() {
        given()
                .param("cnh", "12345678900")
                .when().post("/cnh")
                .then()
                .statusCode(200)
                .assertThat().body(containsString(" é válido."));
    }

    @Test
    public void deveriaValidarCnh2() {
        given()
                .param("cnh", "93012679304")
                .when().post("/cnh")
                .then()
                .statusCode(200)
                .assertThat().body(containsString(" é válido."));
    }

    @Test
    public void deveriaValidarCnh3() {
        given()
                .param("cnh", "61274364119")
                .when().post("/cnh")
                .then()
                .statusCode(200)
                .assertThat().body(containsString(" é válido."));
    }

    @Test
    public void deveriaValidarCnh4() {
        given()
                .param("cnh", "77175131760")
                .when().post("/cnh")
                .then()
                .statusCode(200)
                .assertThat().body(containsString(" é válido."));
    }

    @Test
    public void deveriaValidarCnh5() {
        given()
                .param("cnh", "32142598597")
                .when().post("/cnh")
                .then()
                .statusCode(200)
                .assertThat().body(containsString(" é válido."));
    }

    @Test
    public void deveriaValidarCnh6() {
        given()
                .param("cnh", "98765432100")
                .when().post("/cnh")
                .then()
                .statusCode(200)
                .assertThat().body(containsString(" é válido."));
    }

    @Test
    public void naoDeveriaValidarCnh6() {
        given()
                .param("cnh", "98765432100")
                .when().post("/cnh")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CNH não é válido."));
    }
} 