package com.example.scrummanager.Modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.concurrent.ThreadLocalRandom;

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
    private String color;
    private ArrayList<String> colors= new ArrayList<>();
    //TODO SLA implementation


    public Proyecto() {
    }

    public Proyecto(String idProyecto, String nombreProyecto, String nifCliente, String did,String idEmpresa) {
        this.nombreProyecto =nombreProyecto; 
        this.idProyecto = idProyecto;
        this.nifCliente = nifCliente;
        this.did=did;
        this.idEmpresa=idEmpresa;
        this.setColors();
        this.setColor();
    }

    private void setColors(){
        colors.add("#A9E7EF9D");
        colors.add("#A99F80D3");
        colors.add("#A8C483DC");
        colors.add("#A996EA9A");
        colors.add("#A868AAC8");
        colors.add("#089f80");
        colors.add("#8BC56B89");
    }

    private void setColor(){
        int numeroAleatorio = ThreadLocalRandom.current().nextInt(0, colors.size());
        this.color=colors.get(numeroAleatorio);
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
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

    public String getColor() {
        return this.color;
    }

    public void nuevoSprint(Sprint sprint){
        try {
            if(!comprobarListaSprints(sprint.getIdSprint())) {
                this.listaSprints.add(sprint);
            }
        }catch (java.lang.NullPointerException e){
            listaSprints = new ArrayList<>();
            if(!comprobarListaSprints(sprint.getIdSprint())) {
                this.listaSprints.add(sprint);
            }
        }
    }
    public boolean comprobarListaSprints(String sid){
        boolean existe= false;
        for(int i= 0; i<listaSprints.size();i++){
            if(sid.equals(listaSprints.get(i).getIdSprint())){
                existe=true;
            }
        }
        return existe;
    }
    public void eliminarEmpleado(Sprint sprint){
        try {
            if(comprobarListaSprints(sprint.getIdSprint())) {
                this.listaSprints.remove(sprint);
            }
        }catch (java.lang.NullPointerException e){

        }
    }
}
