package com.example.scrummanager.Modelos;

import java.io.Serializable;

public class Empleado implements Serializable {
    private String uid;
    private String idEmpresa;
    private String idDepartamento;

    private String nifEmpleado;
    private String nombreEmpleado;
    private String apellidoEmpleado;
    private String emailEmpleado;
    private String telefonoEmpleado;

    private Direccion direccionEmpleado;
    private String especialidadEmpleado;
    private int nivelJerarquia; //1 -> Jefe, 2 -> Jefe departamento,  3 -> Project Manager, 4-> Empleado base
    private boolean showMenu=false;

    public Empleado() {
    }

    public Empleado(String uid, String nombreEmpleado, String apellidoEmpleado, String idEmpresa) {
        this.uid = uid;
        this.nombreEmpleado = nombreEmpleado;
        this.apellidoEmpleado = apellidoEmpleado;
        this.nivelJerarquia=4;
        this.idEmpresa=idEmpresa;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public int getNivelJerarquia() {
        return nivelJerarquia;
    }

    public String getUid() {
        return uid;
    }

    public void setNifEmpleado(String nifEmpleado) {
        this.nifEmpleado = nifEmpleado;
    }

    public void setNivelJerarquia(int nivelJerarquia) {
        this.nivelJerarquia = nivelJerarquia;
    }

    public boolean isShowMenu() {
        return showMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    public String getNifEmpleado() {
        return nifEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getApellidoEmpleado() {
        return apellidoEmpleado;
    }

    public void setApellidoEmpleado(String apellidoEmpleado) {
        this.apellidoEmpleado = apellidoEmpleado;
    }

    public String getEmailEmpleado() {
        return emailEmpleado;
    }

    public void setEmailEmpleado(String emailEmpleado) {
        this.emailEmpleado = emailEmpleado;
    }

    public String getTelefonoEmpleado() {
        return telefonoEmpleado;
    }

    public void setTelefonoEmpleado(String telefonoEmpleado) {
        this.telefonoEmpleado = telefonoEmpleado;
    }

    public Direccion getDireccionEmpleado() {
        return direccionEmpleado;
    }

    public void setDireccionEmpleado(Direccion direccionEmpleado) {
        this.direccionEmpleado = direccionEmpleado;
    }

    public String getEspecialidadEmpleado() {
        return especialidadEmpleado;
    }

    public void setEspecialidadEmpleado(String especialidadEmpleado) {
        this.especialidadEmpleado = especialidadEmpleado;
    }

    public String getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(String idDepartamento) {
        this.idDepartamento = idDepartamento;
    }
}
