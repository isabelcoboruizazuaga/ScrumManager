package com.example.scrummanager.Modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Modelo de Tarea
 */
public class Tarea implements Serializable {
    private String idTarea;
    private  String nombreTarea;
    private String descripcionTarea;
    private String encargadoTarea;
    private int estadoTarea; //0 por hacer, 1 en progreso, 2 terminada
    private int tipoTarea; //-1 bug, 0 Tarea, 1 otros
    private int prioridadTarea; //0 baja, 1 normal, 2 alta, 3 terminal
    private Date fechaCreacion;
    private boolean showMenu;

    /**
     * Constructor por defecto
     */
    public Tarea() {
    }

    /**
     * Constructor
     * @param idTarea
     * @param nombreTarea
     * @param descripcionTarea
     * @param tipoTarea
     * @param prioridadTarea
     * @param encargadoTarea
     * @param fechaCreacion
     */
    public Tarea(String idTarea,String nombreTarea, String descripcionTarea, int tipoTarea, int prioridadTarea, String encargadoTarea,Date fechaCreacion) {
        this.idTarea=idTarea;
        this.nombreTarea = nombreTarea;
        this.descripcionTarea = descripcionTarea;
        this.tipoTarea = tipoTarea;
        this.prioridadTarea = prioridadTarea;
        this.fechaCreacion=fechaCreacion;
        this.encargadoTarea=encargadoTarea;
    }

    /**
     * Getter de idTarea
     * @return String
     */
    public String getIdTarea() {
        return idTarea;
    }

    /**
     * Getter de showMenu
     * @return boolean
     */
    public boolean isShowMenu() {
        return showMenu;
    }

    /**
     * Setter de showMenu
     * @param showMenu
     */
    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    /**
     * Getter de fechaCreacion
     * @return Date
     */
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     *Setter de fechaCreacion
     * @param fechaCreacion
     */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Getter de nombreTarea
     * @return String
     */
    public String getNombreTarea() {
        return nombreTarea;
    }

    /**
     * Setter de nombreTarea
     * @param nombreTarea
     */
    public void setNombreTarea(String nombreTarea) {
        this.nombreTarea = nombreTarea;
    }

    /**
     * Getter de descripcionTarea
     * @return String
     */
    public String getDescripcionTarea() {
        return descripcionTarea;
    }

    /**
     * Setter de descripcionTarea
     * @param descripcionTarea
     */
    public void setDescripcionTarea(String descripcionTarea) {
        this.descripcionTarea = descripcionTarea;
    }

    /**
     * Getter de encargadoTarea
     * @return String
     */
    public String getEncargadoTarea() {
        return encargadoTarea;
    }

    /**
     * Setter de encargadoTarea
     * @param encargadoTarea
     */
    public void setEncargadoTarea(String encargadoTarea) {
        this.encargadoTarea = encargadoTarea;
    }

    /**
     * Getter de estadoTarea
     * @return int
     */
    public int getEstadoTarea() {
        return estadoTarea;
    }

    /**
     * Setter de estadoTarea
     * @param estadoTarea
     */
    public void setEstadoTarea(int estadoTarea) {
        this.estadoTarea = estadoTarea;
    }

    /**
     * Getter de tipoTarea
     * @return int
     */
    public int getTipoTarea() {
        return tipoTarea;
    }

    /**
     * Setter de tipoTarea
     * @param tipoTarea
     */
    public void setTipoTarea(int tipoTarea) {
        this.tipoTarea = tipoTarea;
    }

    /**
     * Getter de prioridadTarea
     * @return int
     */
    public int getPrioridadTarea() {
        return prioridadTarea;
    }

    /**
     * Setter de prioridadTarea
     * @param prioridadTarea
     */
    public void setPrioridadTarea(int prioridadTarea) {
        this.prioridadTarea = prioridadTarea;
    }
}
