package com.example.scrummanager.Modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Sprint implements Serializable {
    private String idSprint;
    private ArrayList<Tarea> listaTareasSprint;
    private ArrayList<Date> fechasSprint;
    private String objetivoSprint;

    public Sprint() {
    }

    public Sprint(String idSprint, ArrayList<Date> fechasSprint) {
        this.idSprint = idSprint;
        this.fechasSprint = fechasSprint;
    }

    public String getIdSprint() {
        return idSprint;
    }

    public ArrayList<Tarea> getListaTareasSprint() {
        return listaTareasSprint;
    }

    public void setListaTareasSprint(ArrayList<Tarea> listaTareasSprint) {
        this.listaTareasSprint = listaTareasSprint;
    }

    public ArrayList<Date> getFechasSprint() {
        return fechasSprint;
    }

    public void setFechasSprint(ArrayList<Date> fechasSprint) {
        this.fechasSprint = fechasSprint;
    }

    public String getObjetivoSprint() {
        return objetivoSprint;
    }

    public void setObjetivoSprint(String objetivoSprint) {
        this.objetivoSprint = objetivoSprint;
    }
}
