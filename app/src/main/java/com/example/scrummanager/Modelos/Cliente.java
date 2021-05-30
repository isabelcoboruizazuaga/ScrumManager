package com.example.scrummanager.Modelos;

import java.io.Serializable;

public class Cliente implements Serializable {
    private String idEmpresa;
    private String nifCliente;

    private String nombreCliente;
    private String apellidoCliente;
    private String emailCliente;
    private String telefonoCliente;
    private Direccion direccionCliente;
    private String  tipoCliente; //Company or individual
    private boolean showMenu= false;

    public Cliente() {
    }

    public Cliente(String nifCliente, String nombreCliente, String apellidoCliente, String emailCliente, String eid) {
        this.nifCliente = nifCliente;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.emailCliente = emailCliente;
        this.idEmpresa= eid;
    }

    public boolean isShowMenu() {
        return showMenu;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    public String getNifCliente() {
        return nifCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public Direccion getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(Direccion direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
}
