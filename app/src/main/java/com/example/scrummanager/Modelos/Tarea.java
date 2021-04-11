package com.example.scrummanager.Modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Tarea implements Serializable {
    private int idTarea;
    private  String nombreTarea;
    private String descripcionTarea;
    private ArrayList<Comentario> listaComentariosTarea;
    private String sprintTarea;
    private Empleado encargadoTarea;
    private String estadoTarea;
    private String tipoTarea;
    private String prioridadTarea;
    private ArrayList<Date> fechaTarea;

    public Tarea() {
    }

    public Tarea(String nombreTarea, String descripcionTarea, String sprintTarea) {
        this.nombreTarea = nombreTarea;
        this.descripcionTarea = descripcionTarea;
        this.sprintTarea = sprintTarea;
    }

    public int getIdTarea() {
        return idTarea;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }

    public void setNombreTarea(String nombreTarea) {
        this.nombreTarea = nombreTarea;
    }

    public String getDescripcionTarea() {
        return descripcionTarea;
    }

    public void setDescripcionTarea(String descripcionTarea) {
        this.descripcionTarea = descripcionTarea;
    }

    public ArrayList<Comentario> getListaComentariosTarea() {
        return listaComentariosTarea;
    }

    public void setListaComentariosTarea(ArrayList<Comentario> listaComentariosTarea) {
        this.listaComentariosTarea = listaComentariosTarea;
    }

    public String getSprintTarea() {
        return sprintTarea;
    }

    public Empleado getEncargadoTarea() {
        return encargadoTarea;
    }

    public void setEncargadoTarea(Empleado encargadoTarea) {
        this.encargadoTarea = encargadoTarea;
    }

    public String getEstadoTarea() {
        return estadoTarea;
    }

    public void setEstadoTarea(String estadoTarea) {
        this.estadoTarea = estadoTarea;
    }

    public String getTipoTarea() {
        return tipoTarea;
    }

    public void setTipoTarea(String tipoTarea) {
        this.tipoTarea = tipoTarea;
    }

    public String getPrioridadTarea() {
        return prioridadTarea;
    }

    public void setPrioridadTarea(String prioridadTarea) {
        this.prioridadTarea = prioridadTarea;
    }

    public ArrayList<Date> getFechaTarea() {
        return fechaTarea;
    }

    public void setFechaTarea(ArrayList<Date> fechaTarea) {
        this.fechaTarea = fechaTarea;
    }
}
