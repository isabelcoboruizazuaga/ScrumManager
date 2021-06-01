package com.example.scrummanager.Modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;

public class Proyecto implements Serializable {
    private String idProyecto;
    private String idEmpresa;
    
    private String nombreProyecto;
    private Dictionary<Empleado, String> equipoProyecto;
    private String did;
    private String nifCliente;
    private String especificacionesProyecto; //TODO maybe make it a class
    private ArrayList<Sprint> listaSprints;
    private ArrayList<Date> fechasProyecto;
    private String presupuesto;
    private boolean showMenu= false;
    //TODO SLA implementation


    public Proyecto() {
    }

    public Proyecto(String idProyecto, String nombreProyecto, String nifCliente, String did) {
        this.nombreProyecto =nombreProyecto; 
        this.idProyecto = idProyecto;
        this.nifCliente = nifCliente;
        this.did=did;
    }


    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getIdProyecto() {
        return idProyecto;
    }

    public boolean isShowMenu() {
        return showMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public Dictionary<Empleado, String> getEquipoProyecto() {
        return equipoProyecto;
    }

    public void setEquipoProyecto(Dictionary<Empleado, String> equipoProyecto) {
        this.equipoProyecto = equipoProyecto;
    }

    public String getCliente() {
        return nifCliente;
    }

    public void setCliente(String nifCliente) {
        this.nifCliente = nifCliente;
    }

    public String getEspecificacionesProyecto() {
        return especificacionesProyecto;
    }

    public void setEspecificacionesProyecto(String especificacionesProyecto) {
        this.especificacionesProyecto = especificacionesProyecto;
    }

    public ArrayList<Sprint> getListaSprints() {
        return listaSprints;
    }

    public void setListaSprints(ArrayList<Sprint> listaSprints) {
        this.listaSprints = listaSprints;
    }

    public ArrayList<Date> getFechasProyecto() {
        return fechasProyecto;
    }

    public void setFechasProyecto(ArrayList<Date> fechasProyecto) {
        this.fechasProyecto = fechasProyecto;
    }

    public String getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(String presupuesto) {
        this.presupuesto = presupuesto;
    }
}
