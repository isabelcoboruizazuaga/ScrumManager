package com.example.scrummanager.Modelos;

import java.io.Serializable;
import java.util.ArrayList;

public class Departamento implements Serializable {
    private String idDepartamento;
    private String idEmpresa;

    private String nombreDepartamento;
    private String uidJefeDepartamento;
    private String urlFotoDepartamento;

    private ArrayList<String> miembrosDepartamento; //guarda la uid de cada miembro
    private ArrayList<Proyecto> ListaProyectosDepartamento;
    private boolean showMenu=false;

    public Departamento() {
    }

    public Departamento(String idDepartamento, String nombreDepartamento, String idEmpresa) {
        this.idDepartamento = idDepartamento;
        this.nombreDepartamento = nombreDepartamento;
        this.idEmpresa= idEmpresa;
    }

    public boolean isShowMenu() {
        return showMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    public void setIdDepartamento(String idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getUrlFotoDepartamento() {
        return urlFotoDepartamento;
    }

    public void setUrlFotoDepartamento(String urlFotoDepartamento) {
        this.urlFotoDepartamento = urlFotoDepartamento;
    }

    public String getIdDepartamento() {
        return idDepartamento;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public String getUidJefeDepartamento() {
        return uidJefeDepartamento;
    }

    public void setUidJefeDepartamento(String uidJefeDepartamento) {
        this.uidJefeDepartamento = uidJefeDepartamento;
    }

    public ArrayList<String> getMiembrosDepartamento() {
        return miembrosDepartamento;
    }

    public void setMiembrosDepartamento(ArrayList<String> miembrosDepartamento) {
        this.miembrosDepartamento = miembrosDepartamento;
    }

    public ArrayList<Proyecto> getListaProyectosDepartamento() {
        return ListaProyectosDepartamento;
    }

    public void setListaProyectosDepartamento(ArrayList<Proyecto> listaProyectosDepartamento) {
        this.ListaProyectosDepartamento = listaProyectosDepartamento;
    }
}
