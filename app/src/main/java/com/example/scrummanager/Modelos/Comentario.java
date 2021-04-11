package com.example.scrummanager.Modelos;

import java.io.Serializable;
import java.util.Date;

public class Comentario implements Serializable {
    private String nifEmpleado;
    private Date fechaComentario;
    private String textoComentario;

    public Comentario() {
    }

    public Comentario(String nifEmpleado, Date fechaComentario, String textoComentario) {
        this.nifEmpleado = nifEmpleado;
        this.fechaComentario = fechaComentario;
        this.textoComentario = textoComentario;
    }
}
