package com.example.scrummanager.Modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Dictionary;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Modelo de proyecto
 */
public class Proyecto implements Serializable {
    private String idProyecto;
    private String idEmpresa;
    
    private String nombreProyecto;
    private String did;
    private String nifCliente;
    private String especificacionesProyecto;
    private ArrayList<Sprint> listaSprints;
    private ArrayList<Date> fechasProyecto;
    private String presupuesto;
    private boolean showMenu= false;
    private String color;
    private ArrayList<String> colors= new ArrayList<>();


    /**
     * Constructor por defecto
     */
    public Proyecto() {
    }

    /**
     * Constructor
     * @param idProyecto
     * @param nombreProyecto
     * @param nifCliente
     * @param did
     * @param idEmpresa
     */
    public Proyecto(String idProyecto, String nombreProyecto, String nifCliente, String did,String idEmpresa) {
        this.nombreProyecto =nombreProyecto; 
        this.idProyecto = idProyecto;
        this.nifCliente = nifCliente;
        this.did=did;
        this.idEmpresa=idEmpresa;
        this.setColors();
        this.setColor();
    }

    /**
     * Rellena el ArrayList de colores
     */
    private void setColors(){
        colors.add("#A9E7EF9D");
        colors.add("#A99F80D3");
        colors.add("#A8C483DC");
        colors.add("#A996EA9A");
        colors.add("#A868AAC8");
        colors.add("#089f80");
        colors.add("#8BC56B89");
    }

    /**
     * Setter de Color
     * Escoge un color aleatorio de la lista
     */
    private void setColor(){
        int numeroAleatorio = ThreadLocalRandom.current().nextInt(0, colors.size());
        this.color=colors.get(numeroAleatorio);
    }

    /**
     * Getter de idEmpresa
     * @return idEmpresa: String
     */
    public String getIdEmpresa() {
        return idEmpresa;
    }

    /**
     * Setter de idEmpresa
     * @param idEmpresa
     */
    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    /**
     * Getter de idDepartamento
     * @return did: String
     */
    public String getDid() {
        return did;
    }

    /**
     * Setter de idDepartamento
     * @param did
     */
    public void setDid(String did) {
        this.did = did;
    }

    /**
     * Getter de idProyecto
     * @return idProyecto: String
     */
    public String getIdProyecto() {
        return idProyecto;
    }

    /**
     * Getter de showMenu
     * @return showMenu: boolean
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
     * Getter de nombreProyecto
     * @return nombreProyecto: String
     */
    public String getNombreProyecto() {
        return nombreProyecto;
    }

    /**
     * Setter de nombreProyecto
     * @param nombreProyecto
     */
    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    /**
     * Getter de cliente
     * @return nifCliente: String
     */
    public String getCliente() {
        return nifCliente;
    }

    /**
     * Setter de cliente
     * @param nifCliente
     */
    public void setCliente(String nifCliente) {
        this.nifCliente = nifCliente;
    }

    /**
     * Getter de especificacionesProyecto
     * @return especificacionesProyecto: String
     */
    public String getEspecificacionesProyecto() {
        return especificacionesProyecto;
    }

    /**
     * Setter de especificacionesProyecto
     * @param especificacionesProyecto
     */
    public void setEspecificacionesProyecto(String especificacionesProyecto) {
        this.especificacionesProyecto = especificacionesProyecto;
    }

    /**
     * Getter de listaSrints
     * @return listaSprints: ArrayList<Sprint>
     * @see Sprint
     */
    public ArrayList<Sprint> getListaSprints() {
        return listaSprints;
    }

    /**
     * Setter de listaSprints
     * @param listaSprints
     * @see Sprint
     */
    public void setListaSprints(ArrayList<Sprint> listaSprints) {
        this.listaSprints = listaSprints;
    }

    /**
     * Setter de fechasProyecto
     * @return fechasProyecto: ArrayList<Date>
     */
    public ArrayList<Date> getFechasProyecto() {
        return fechasProyecto;
    }

    /**
     * Setter fechasProyecto
     * @param fechasProyecto
     */
    public void setFechasProyecto(ArrayList<Date> fechasProyecto) {
        this.fechasProyecto = fechasProyecto;
    }

    /**
     * Getter presupuesto
     * @return presupuesto: String
     */
    public String getPresupuesto() {
        return presupuesto;
    }

    /**
     * Setter de presupuesto
     * @param presupuesto
     */
    public void setPresupuesto(String presupuesto) {
        this.presupuesto = presupuesto;
    }

    /**
     * Geter de color
     * @return color: String
     */
    public String getColor() {
        return this.color;
    }

    /**
     * Devuelve el sprint de la lista que tenga el id proporcionado
     * @param sid id del Sprint a obtener
     * @return sprint: Sptint
     * @see Sprint
     */
    public Sprint extraerSprint(String sid){
        Sprint sprint= null;
        for(int i= 0; i<listaSprints.size();i++){
            listaSprints.get(i).setShowMenu(false);
            if(sid.equals(listaSprints.get(i).getIdSprint())){
                sprint=listaSprints.get(i);
            }
        }
        return sprint;
    }

    /**
     * Añadir Sprint a la lista de sprint
     * @param sprint
     */
    public void nuevoSprint(Sprint sprint){
        try {
            if(!comprobarListaSprints(sprint)) {
                this.listaSprints.add(sprint);
            }
        }catch (java.lang.NullPointerException e){
            listaSprints = new ArrayList<>();
            if(!comprobarListaSprints(sprint)) {
                this.listaSprints.add(sprint);
            }
        }
    }

    /**
     * Comprueba si un sprint está en la lista
     * @param sprint
     * @return existe: boolean
     */
    public boolean comprobarListaSprints(Sprint sprint){
        boolean existe= false;
        for(int i= 0; i<listaSprints.size();i++){
            listaSprints.get(i).setShowMenu(false);

            if(sprint.getIdSprint().equals(listaSprints.get(i).getIdSprint())){
                existe=true;
            }
        }
        return existe;
    }

    /**
     * Elimina un sprint de la lista de sprints
     * @param sprint
     */
    public void eliminarSprint(Sprint sprint){
        try {
            if(comprobarListaSprints(sprint)) {
                for (int i=0;i<listaSprints.size();i++){
                    if(sprint.getIdSprint().equals(listaSprints.get(i).getIdSprint())){
                        listaSprints.remove(listaSprints.get(i));
                    }
                }
                this.listaSprints.remove(sprint);
            }
        }catch (java.lang.NullPointerException e){
        }
    }

    /**
     * Ordena la lista de sprints por fecha de comienzo
     */
    public void ordenarSprints(){
        Collections.sort(listaSprints, new Comparator<Sprint>() {
            public int compare(Sprint o1, Sprint o2) {
                if (o1.getFechasSprint().get(0) == null || o2.getFechasSprint().get(0) == null)
                    return 0;
                return o1.getFechasSprint().get(0).compareTo(o2.getFechasSprint().get(0));
            }
        });
    }
}
