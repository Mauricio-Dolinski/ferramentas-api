package com.dolinski.mauricio.api.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.matchesPattern;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@QuarkusTest
public class CnhControllerTest {

    @Test
    public void testGerarCnh() {
        given()
                .when().get("/cnh")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("0"));
    }

    @Test
    public void testGerarCnhComFormatoValido() {
        given()
                .when().get("/cnh")
                .then()
                .statusCode(200)
                .assertThat().body(matchesPattern("\\d{11}"));
    }

    @Test
    public void testValidarCnhValido() {
        given()
                .param("cnh", "12345678900")
                .when().post("/cnh")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CNH 12345678900 é válido."));
    }

    @Test
    public void testValidarCnhValidoSegundoExemplo() {
        given()
                .param("cnh", "93012679304")
                .when().post("/cnh")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CNH 93012679304 é válido."));
    }

    @Test
    public void testValidarCnhValidoTerceiroExemplo() {
        given()
                .param("cnh", "61274364119")
                .when().post("/cnh")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CNH 61274364119 é válido."));
    }

    @Test
    public void testValidarCnhInvalido() {
        given()
                .param("cnh", "98765432100")
                .when().post("/cnh")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CNH não é válido."));
    }

    @Test
    public void testValidarCnhValidoComEspacos() {
        given()
                .param("cnh", " 12345678900 ")
                .when().post("/cnh")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CNH 12345678900 é válido."));
    }

    @Test
    public void testRejeicaoCnhComDigitoVerificadorInvalido() {
        given()
                .param("cnh", "12345678901")
                .when().post("/cnh")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CNH não é válido"));
    }

    @Test
    public void testRejeicaoCnhComMaisDeOnzeDigitos() {
        given()
                .param("cnh", "123456789012")
                .when().post("/cnh")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CNH não é válido, deve conter 11 numeros."));
    }

    @Test
    public void testRejeicaoCnhComMenosDeOnzeDigitos() {
        given()
                .param("cnh", "1234567890")
                .when().post("/cnh")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CNH não é válido, deve conter 11 numeros."));
    }

    @Test
    public void testRejeicaoCnhComLetras() {
        given()
                .param("cnh", "1234567890A")
                .when().post("/cnh")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CNH não é válido, deve conter 11 numeros."));
    }

    @Test
    public void testRejeicaoCnhComCaracteresEspeciais() {
        given()
                .param("cnh", "12345678@00")
                .when().post("/cnh")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CNH não é válido, deve conter 11 numeros."));
    }

    @Test
    public void testRejeicaoCnhComTodosNumerosIguais() {
        given()
                .param("cnh", "11111111111")
                .when().post("/cnh")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CNH não é válido, todos os números são iguais."));
    }

    @Test
    public void testRequisicaoSemParametroCnh() {
        given()
                .when().post("/cnh")
                .then()
                .statusCode(400);
    }

    @Test
    public void testRequisicoesConcorrentes() throws InterruptedException {
        int numeroDeThreads = 50;
        ExecutorService executor = Executors.newFixedThreadPool(numeroDeThreads);

        for (int i = 0; i < numeroDeThreads; i++) {
            executor.submit(() -> {
                given()
                    .when().get("/cnh")
                    .then()
                    .statusCode(200)
                    .body(matchesPattern("\\d{11}"));
            });
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
    }

    @Test
    public void testIgnorarParametrosExtrasNaRequisicaoGet() {
        given()
            .queryParam("extra", "parametro")
            .when().get("/cnh")
            .then()
            .statusCode(200)
            .body(matchesPattern("\\d{11}"));
    }

    @Test
    public void testIgnorarParametrosExtrasNaRequisicaoPost() {
        given()
            .param("cnh", "12345678900")
            .param("extra", "parametro")
            .when().post("/cnh")
            .then()
            .statusCode(200)
            .assertThat().body(containsString(" é válido."));
    }

    @Test
    public void testRejeicaoCnhVazio() {
        given()
            .param("cnh", "")
            .when().post("/cnh")
            .then()
            .statusCode(400)
            .assertThat().body(containsString("CNH não pode ser vazio."));
    }

    @Test
    public void testRejeicaoCnhComPayloadMuitoGrande() {
        String cnhGrande = "1234567890".repeat(10); // 100 digits

        given()
            .param("cnh", cnhGrande)
            .when().post("/cnh")
            .then()
            .statusCode(400)
            .assertThat().body(containsString("CNH não é válido, deve conter 11 numeros."));
    }

    @Test
    public void testGerarEValidarCnhsConcorrentemente() {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                String cnhGerado = given()
                    .when().get("/cnh")
                    .then()
                    .statusCode(200)
                    .extract().asString();
                System.out.println("CNH gerado: " + cnhGerado);

                given()
                    .param("cnh", cnhGerado)
                    .when().post("/cnh")
                    .then()
                    .statusCode(200)
                    .assertThat().body(containsString("CNH " + cnhGerado + " é válido."));
                
            }, executor);

            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executor.shutdown();
    }
}