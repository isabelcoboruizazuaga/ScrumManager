package com.example.scrummanager.Modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Tarea implements Serializable {
    private String idTarea;
    private  String nombreTarea;
    private String descripcionTarea;
    private ArrayList<Comentario> listaComentariosTarea;
    private String sprintTarea;
    private String encargadoTarea;
    private int estadoTarea; //0 por hacer, 1 en progreso, 2 terminada
    private int tipoTarea; //-1 bug, 0 Tarea, 1 otros
    private int prioridadTarea; //0 baja, 1 normal, 2 alta, 3 terminal
    private ArrayList<Date> fechaTarea;
    private Date fechaCreacion;
    private boolean showMenu;

    public Tarea() {
    }

    public Tarea(String idTarea,String nombreTarea, String descripcionTarea, int tipoTarea, int prioridadTarea, String encargadoTarea,Date fechaCreacion) {
        this.idTarea=idTarea;
        this.nombreTarea = nombreTarea;
        this.descripcionTarea = descripcionTarea;
        this.tipoTarea = tipoTarea;
        this.prioridadTarea = prioridadTarea;
        this.fechaCreacion=fechaCreacion;
        this.encargadoTarea=encargadoTarea;
    }

    public String getIdTarea() {
        return idTarea;
    }

    public boolean isShowMenu() {
        return showMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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

    public String getEncargadoTarea() {
        return encargadoTarea;
    }

    public void setEncargadoTarea(String encargadoTarea) {
        this.encargadoTarea = encargadoTarea;
    }

    public int getEstadoTarea() {
        return estadoTarea;
    }

    public void setEstadoTarea(int estadoTarea) {
        this.estadoTarea = estadoTarea;
    }

    public int getTipoTarea() {
        return tipoTarea;
    }

    public void setTipoTarea(int tipoTarea) {
        this.tipoTarea = tipoTarea;
    }

    public int getPrioridadTarea() {
        return prioridadTarea;
    }

    public void setPrioridadTarea(int prioridadTarea) {
        this.prioridadTarea = prioridadTarea;
    }

    public ArrayList<Date> getFechaTarea() {
        return fechaTarea;
    }

    public void setFechaTarea(ArrayList<Date> fechaTarea) {
        this.fechaTarea = fechaTarea;
    }
}
