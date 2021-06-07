package com.example.scrummanager.Modelos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Modelo de departamento
 */
public class Departamento implements Serializable {
    private String idDepartamento;
    private String idEmpresa;

    private String nombreDepartamento;
    private String uidJefeDepartamento;
    private String urlFotoDepartamento;

    private ArrayList<String> miembrosDepartamento; //guarda la uid de cada miembro
    private ArrayList<Proyecto> ListaProyectosDepartamento;
    private boolean showMenu=false;

    /**
     * Constructor por defecto
     */
    public Departamento() {
    }

    /**
     * Constructor
     * @param idDepartamento
     * @param nombreDepartamento
     * @param idEmpresa
     */
    public Departamento(String idDepartamento, String nombreDepartamento, String idEmpresa) {
        this.idDepartamento = idDepartamento;
        this.nombreDepartamento = nombreDepartamento;
        this.idEmpresa= idEmpresa;
        miembrosDepartamento= new ArrayList<>();
        ListaProyectosDepartamento= new ArrayList<>();
    }

    /**
     * Getter de ShowMenu
     * @return ShowMenu
     */
    public boolean isShowMenu() {
        return showMenu;
    }

    /**
     * Setter de ShowMenu
     * @param showMenu
     */
    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    /**
     * Getter de idDepartamento
     * @return idDepartamento
     */
    public String getIdDepartamento() {
        return idDepartamento;
    }

    /**
     * Getter de nombreDepartamento
     * @return nombreDepartamento
     */
    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    /**
     * Setter de nombreDepartamento
     * @param nombreDepartamento
     */
    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    /**
     * Getter de uidJefeDepartamento
     * @return uidJefeDepartamento
     */
    public String getUidJefeDepartamento() {
        return uidJefeDepartamento;
    }

    /**
     * Setter de  uidJefeDepartamento
     * @param uidJefeDepartamento
     */
    public void setUidJefeDepartamento(String uidJefeDepartamento) {
        this.uidJefeDepartamento = uidJefeDepartamento;
    }

    /**
     * Getter de getMiembrosDepartamento
     * @return getMiembrosDepartamento
     */
    public ArrayList<String> getMiembrosDepartamento() {
        return miembrosDepartamento;
    }

    /**
     * Añade un empleado a la lista de miembros
     * @param uid
     */
    public void aniadirEmpleado(String uid){
        try {
            if(!comprobarListaEmpleados(uid)) {
                this.miembrosDepartamento.add(uid);
            }
        }catch (java.lang.NullPointerException e){
            miembrosDepartamento = new ArrayList<>();
            if(!comprobarListaEmpleados(uid)) {
                this.miembrosDepartamento.add(uid);
            }
        }
    }

    /**
     * Comprueba si un empleado está en la lista de miembros
     * @param uid
     * @return existe: boolean
     */
    public boolean comprobarListaEmpleados(String uid){
        boolean existe= false;
        for(int i= 0; i<miembrosDepartamento.size();i++){
            if(uid.equals(miembrosDepartamento.get(i))){
                existe=true;
            }
        }
        return existe;
    }

    /**
     * Elimina un empleado de la lista de miembros
     * @param uid
     */
    public void eliminarEmpleado(String uid){
        try {
            if(comprobarListaEmpleados(uid)) {
                this.miembrosDepartamento.remove(uid);
            }
        }catch (java.lang.NullPointerException e){

        }
    }
}
