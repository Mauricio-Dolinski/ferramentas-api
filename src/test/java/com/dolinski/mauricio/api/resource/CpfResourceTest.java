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
                .when().get("/cpf")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("gerado"));
    }

    @Test
    public void deveriaValidarCpf() {
        given()
                .param("cpf", "848.509.150-78")
                .when().post("/cpf")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CPF é válido"));
    }

    @Test
    public void naoDeveriaValidarCpf() {
        given()
                .param("cpf", "10987654321")
                .when().post("/cpf")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CPF não é válido"));
    }
}