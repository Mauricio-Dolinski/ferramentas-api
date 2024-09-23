package com.dolinski.mauricio.api.controller;

import io.quarkus.test.junit.QuarkusTest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.matchesPattern;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@QuarkusTest
public class CnpjControllerTest {

    @Test
    public void testGerarCnpj() {
        given()
                .when().get("/cnpj")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("-"));
    }

    @Test
    public void testGerarCnpjComFormatoValido() {
        given()
                .when().get("/cnpj")
                .then()
                .statusCode(200)
                .assertThat().body(matchesPattern("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}"));
    }

    @Test
    public void testValidarCnpjValido() {
        given()
                .param("cnpj", "01.234.567/8912-51")
                .when().post("/cnpj")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CNPJ 01.234.567/8912-51 é válido."));
    }

    @Test
    public void testValidarCnpjValidoComEspacos() {
        given()
                .param("cnpj", " 01.234.567/8912-51 ")
                .when().post("/cnpj")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CNPJ 01.234.567/8912-51 é válido."));
    }

    @Test
    public void testValidarCnpjValidoSemPontuacao() {
        given()
                .param("cnpj", "01234567891251")
                .when().post("/cnpj")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CNPJ 01.234.567/8912-51 é válido."));
    }

    @Test
    public void testRejeicaoCnpjComDigitoVerificadorInvalido() {
        given()
                .param("cnpj", "01.234.567/8912-34")
                .when().post("/cnpj")
                .then()
                .statusCode(200)
                .assertThat().body(containsString("CNPJ não é válido, dígito verificador deveria ser "));
    }

    @Test
    public void testRejeicaoCnpjComMaisDeQuatorzeDigitos() {
        given()
                .param("cnpj", "01.234.567/8912-345")
                .when().post("/cnpj")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CNPJ não é válido, deve conter 14 numeros."));
    }

    @Test
    public void testRejeicaoCnpjVazio() {
        given()
                .param("cnpj", "")
                .when().post("/cnpj")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CNPJ não pode ser vazio"));
    }

    @Test
    public void testRejeicaoCnpjComLetras() {
        given()
                .param("cnpj", "abcdefghijklmn")
                .when().post("/cnpj")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CNPJ não é válido, deve conter 14 numeros."));
    }

    @Test
    public void testRejeicaoCnpjComTodosNumerosIguais() {
        given()
                .param("cnpj", "11.111.111/1111-11")
                .when().post("/cnpj")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("CNPJ não é válido, todos os números são iguais."));
    }

    @Test
    public void testRequisicaoSemParametroCnpj() {
        given()
                .when().post("/cnpj")
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
                    .when().get("/cnpj")
                    .then()
                    .statusCode(200)
                    .body(matchesPattern("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}"));
            });
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
    }

    @Test
    public void testIgnorarParametrosExtrasNaRequisicaoGet() {
        given()
            .queryParam("extra", "parametro")
            .when().get("/cnpj")
            .then()
            .statusCode(200)
            .body(matchesPattern("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}"));
    }

    @Test
    public void testIgnorarParametrosExtrasNaRequisicaoPost() {
        given()
            .param("cnpj", "01.234.567/8912-51")
            .param("extra", "parametro")
            .when().post("/cnpj")
            .then()
            .statusCode(200)
            .assertThat().body(containsString("válido"));
    }

    @Test
    public void testRejeicaoCnpjComPayloadMuitoGrande() {
        String cnpjGrande = "1234567890".repeat(10); // 100 digits

        given()
            .param("cnpj", cnpjGrande)
            .when().post("/cnpj")
            .then()
            .statusCode(400)
            .assertThat().body(containsString("CNPJ não é válido, deve conter 14 numeros."));
    }

    @Test
    public void testGerarEValidarCnpjsConcorrentemente() {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                String cnpjGerado = given()
                    .when().get("/cnpj")
                    .then()
                    .statusCode(200)
                    .extract().asString();
                System.out.println("CNPJ gerado: " + cnpjGerado);

                given()
                    .param("cnpj", cnpjGerado)
                    .when().post("/cnpj")
                    .then()
                    .statusCode(200)
                    .assertThat().body(containsString("CNPJ " + cnpjGerado + " é válido."));
                
            }, executor);

            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executor.shutdown();
    }
}