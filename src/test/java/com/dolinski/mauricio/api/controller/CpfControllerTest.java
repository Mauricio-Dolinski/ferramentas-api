package com.dolinski.mauricio.api.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.matchesPattern;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class CpfControllerTest {

    
    @Test
    public void testGerarCpf() {
        given()
                .when().get("/cpf")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("-"));
    }

    @Test
    public void testGerarCpfComFormatoValido() {
        given()
                .when().get("/cpf")
                .then()
                .statusCode(200)
                .assertThat().body(matchesPattern("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}"));
    }

    @Test
    public void testValidarCpfValido() {
        given()
                .param("cpf", "123.456.789-09")
                .when().post("/cpf")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CPF 123.456.789-09 é válido."));
    }

    @Test
    public void testValidarCpfValidoComEspacos() {
        given()
                .param("cpf", " 123.456.789-09 ")
                .when().post("/cpf")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CPF 123.456.789-09 é válido."));
    }

    @Test
    public void testValidarCpfValidoSemPontuacao() {
        given()
                .param("cpf", "12345678909")
                .when().post("/cpf")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CPF 123.456.789-09 é válido."));
    }

    @Test
    public void testValidarCpfValidoSemZeroInicial() {
        given()
                .param("cpf", "18.457.127-81")
                .when().post("/cpf")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CPF 018.457.127-81 é válido."));
    }

    @Test
    public void testRejeicaoCpfComDigitoVerificadorInvalido() {
        given()
                .param("cpf", "12345678910")
                .when().post("/cpf")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CPF não é válido, digito verificador deveria ser 09."));
    }

    @Test
    public void testRejeicaoCpfComMaisDeOnzeDigitos() {
        given()
                .param("cpf", "1098765432123456")
                .when().post("/cpf")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CPF não é válido, deve conter 11 numeros."));
    }

    @Test
    public void testRejeicaoCpfComMenosDeOnzeDigitos() {
        given()
                .param("cpf", "123456")
                .when().post("/cpf")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CPF não é válido, deve conter 11 numeros."));
    }

    @Test
    public void testRejeicaoCpfComLetras() {
        given()
                .param("cpf", "123.456.78A-09")
                .when().post("/cpf")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CPF não é válido, deve conter 11 numeros."));
    }

    @Test
    public void testRejeicaoCpfComCaracteresEspeciais() {
        given()
                .param("cpf", "123.456.789-0@")
                .when().post("/cpf")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CPF não é válido, deve conter 11 numeros."));
    }

    @Test
    public void testRejeicaoCpfComTodosNumerosIguais() {
        given()
                .param("cpf", "111.111.111-11")
                .when().post("/cpf")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CPF não é válido, todos os números são iguais."));
    }

    @Test
    public void testRequisicaoSemParametroCpf() {
        given()
                .when().post("/cpf")
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
                    .when().get("/cpf")
                    .then()
                    .statusCode(200)
                    .body(matchesPattern("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}"));
            });
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
    }

    @Test
    public void testIgnorarParametrosExtrasNaRequisicaoGet() {
        given()
            .queryParam("extra", "parametro")
            .when().get("/cpf")
            .then()
            .statusCode(200)
            .body(matchesPattern("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}"));
    }

    @Test
    public void testIgnorarParametrosExtrasNaRequisicaoPost() {
        given()
            .param("cpf", "123.456.789-09")
            .param("extra", "parametro")
            .when().post("/cpf")
            .then()
            .statusCode(200)
            .body(containsString("CPF 123.456.789-09 é válido."));
    }

    @Test
    public void testRejeicaoCpfVazio() {
        given()
            .param("cpf", "")
            .when().post("/cpf")
            .then()
            .statusCode(400)
            .assertThat().body(containsString("CPF não pode ser vazio."));
    }

    @Test
    public void testRejeicaoCpfComPayloadMuitoGrande() {
        String cpfGrande = "1234567890".repeat(10); // 100 digits

        given()
            .param("cpf", cpfGrande)
            .when().post("/cpf")
            .then()
            .statusCode(400)
            .assertThat().body(containsString("CPF não é válido, deve conter 11 numeros."));
    }

    @Test
    public void testGerarEValidarCpfsConcorrentemente() {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                String cpfGerado = given()
                    .when().get("/cpf")
                    .then()
                    .statusCode(200)
                    .extract().asString();
                System.out.println("CPF gerado: " + cpfGerado);

                given()
                    .param("cpf", cpfGerado)
                    .when().post("/cpf")
                    .then()
                    .statusCode(200)
                    .assertThat().body(containsString("CPF " + cpfGerado + " é válido."));
                
            }, executor);

            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executor.shutdown();
    }

    @Test
    public void testGerarEValidarCpf() {
        String cpfGerado = given()
            .when().get("/cpf")
            .then()
            .statusCode(200)
            .extract().asString();

        given()
            .param("cpf", cpfGerado)
            .when().post("/cpf")
            .then()
            .statusCode(200)
            .assertThat().body(containsString("CPF " + cpfGerado + " é válido."));
    }
}