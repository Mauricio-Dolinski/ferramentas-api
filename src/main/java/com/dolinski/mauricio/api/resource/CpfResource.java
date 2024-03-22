package com.dolinski.mauricio.api.resource;

import com.dolinski.mauricio.api.entity.Cpf;
import com.dolinski.mauricio.api.entity.Documento;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/cpf")
public class CpfResource {
    
    @GET
    public String gerar() {
        Documento documento = new Cpf();
        return documento.gerar();
    }

    
    @POST
    public String validar(@FormParam("cpf") String numero) {
        Documento documento = new Cpf();
        return documento.validar(numero);
    }
}
