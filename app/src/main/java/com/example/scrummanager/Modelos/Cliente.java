package com.example.scrummanager.Modelos;

import java.io.Serializable;

/**
 * Modelo de Ciente
 */
public class Cliente implements Serializable {
    private String idEmpresa;
    private String nifCliente;
    private String nombreCliente;
    private String apellidoCliente;
    private String emailCliente;
    private String telefonoCliente;
    private String  tipoCliente; //Empresa o individual
    private boolean showMenu= false;

    /**
     * Constructor por defecto
     */
    public Cliente() {
    }

    /**
     * Constructor
     * @param nifCliente
     * @param nombreCliente
     * @param apellidoCliente
     * @param emailCliente
     * @param eid
     */
    public Cliente(String nifCliente, String nombreCliente, String apellidoCliente, String emailCliente, String eid) {
        this.nifCliente = nifCliente;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.emailCliente = emailCliente;
        this.idEmpresa= eid;
    }

    /**
     * Getter de showMenu
     * @return showMenu : Boolean
     */
    public boolean isShowMenu() {
        return showMenu;
    }

    /**
     * Getter de idEmpresa
     * @return idEmpresa: String
     */
    public String getIdEmpresa() {
        return idEmpresa;
    }

    /**
     * Setter de showMenu
     * @param showMenu
     */
    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    /**
     * Getter de NifCliente
     * @return getNifCliente: String
     */
    public String getNifCliente() {
        return nifCliente;
    }

    /**
     * Getter de nombreCliente
     * @return getNombreCliente: String
     */
    public String getNombreCliente() {
        return nombreCliente;
    }

    /**
     * Setter de nombreCliente
     * @param nombreCliente
     */
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    /**
     * Getter de apellidoCliente
     * @return getApellidoCliente:String
     */
    public String getApellidoCliente() {
        return apellidoCliente;
    }

    /**
     * Setter de apellidoCliente
     * @param apellidoCliente
     */
    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    /**
     * Getter de emailCliente
     * @return emailCliente:String
     */
    public String getEmailCliente() {
        return emailCliente;
    }

    /**
     * Setter de emailCliente
     * @param emailCliente
     */
    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    /**
     * Getter de telefonoCliente
     * @return telefonoCliente: String
     */
    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    /**
     * Setter de telefonoCliente
     * @param telefonoCliente
     */
    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    /**
     * Getter de tipoCliente
     * @return tipoCliente: String
     */
    public String getTipoCliente() {
        return tipoCliente;
    }

    /**
     * Setter de tipoCliente
     * @param tipoCliente
     */
    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
}
