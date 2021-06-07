package com.example.scrummanager.Modelos;

import java.io.Serializable;

/**
 * Modelo de Empleado
 */
public class Empleado implements Serializable {
    private String uid;
    private String idEmpresa;
    private String idDepartamento;

    private String nifEmpleado;
    private String nombreEmpleado;
    private String apellidoEmpleado;
    private String emailEmpleado;
    private int nivelJerarquia; //1 -> Jefe, 2 -> Jefe departamento,  3 -> Project Manager, 4-> Empleado base
    private boolean showMenu=false;

    /**
     * Constructor por defecto
     */
    public Empleado() {
    }

    /**
     * Constructor
     * @param uid
     * @param nombreEmpleado
     * @param apellidoEmpleado
     * @param idEmpresa
     */
    public Empleado(String uid, String nombreEmpleado, String apellidoEmpleado, String idEmpresa) {
        this.uid = uid;
        this.nombreEmpleado = nombreEmpleado;
        this.apellidoEmpleado = apellidoEmpleado;
        this.nivelJerarquia=4;
        this.idEmpresa=idEmpresa;
    }

    /**
     * Getter de idEmpresa
     * @return idEmpresa
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
     * Setter de nivelJerarquia
     * @param nivelJerarquia
     */
    public void setNivelJerarquia(int nivelJerarquia) {
        this.nivelJerarquia = nivelJerarquia;
    }

    /**
     * Getter de nivelJerarquia
     * @return
     */
    public int getNivelJerarquia() {
        return nivelJerarquia;
    }

    /**
     * Getter de uid
     * @return
     */
    public String getUid() {
        return uid;
    }

    /**
     * Setter de nifEmpleado
     * @param nifEmpleado
     */
    public void setNifEmpleado(String nifEmpleado) {
        this.nifEmpleado = nifEmpleado;
    }

    /**
     * Getter de showMenu
     * @return showMenu
     */
    public boolean isShowMenu() {
        return showMenu;
    }

    /**
     * Setter showMenu
     * @param showMenu
     */
    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    /**
     * Getter de nifEmpleado
     * @return nifEmpleado
     */
    public String getNifEmpleado() {
        return nifEmpleado;
    }

    /**
     * Getter de nombreEmpleado
     * @return nombreEmpleado
     */
    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    /**
     * Setter de nombreEmpleado
     * @param nombreEmpleado
     */
    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    /**
     * Getter de apellidoEmpleado
     * @return apellidoEmpleado
     */
    public String getApellidoEmpleado() {
        return apellidoEmpleado;
    }

    /**
     * Seter de apellidoEmpleado
     * @param apellidoEmpleado
     */
    public void setApellidoEmpleado(String apellidoEmpleado) {
        this.apellidoEmpleado = apellidoEmpleado;
    }

    /**
     * Getter de emailEmpleado
     * @return emailEmpleado
     */
    public String getEmailEmpleado() {
        return emailEmpleado;
    }

    /**
     * Setter de emailEmpleado
     * @param emailEmpleado
     */
    public void setEmailEmpleado(String emailEmpleado) {
        this.emailEmpleado = emailEmpleado;
    }

    /**
     * Getter de idDepartamento
     * @return idDepartamento
     */
    public String getIdDepartamento() {
        return idDepartamento;
    }

    /**
     * Setter de idDepartamento
     * @param idDepartamento
     */
    public void setIdDepartamento(String idDepartamento) {
        this.idDepartamento = idDepartamento;
    }
}
