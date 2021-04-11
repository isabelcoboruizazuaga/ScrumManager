package com.example.scrummanager.Modelos;

import java.io.Serializable;

public class Direccion implements Serializable {
    private String tipoDireccion;
    private int numeroDireccion;
    private int codigoPostal;
    private String localidad;
    private String provincia;
    private String pais;
    private String extrasDireccion;

    public Direccion() {
    }

    public Direccion(String tipoDireccion, int numeroDireccion, int codigoPostal, String localidad, String provincia, String pais) {
        this.tipoDireccion = tipoDireccion;
        this.numeroDireccion = numeroDireccion;
        this.codigoPostal = codigoPostal;
        this.localidad = localidad;
        this.provincia = provincia;
        this.pais = pais;
    }

    public String getTipoDireccion() {
        return tipoDireccion;
    }

    public void setTipoDireccion(String tipoDireccion) {
        this.tipoDireccion = tipoDireccion;
    }

    public int getNumeroDireccion() {
        return numeroDireccion;
    }

    public void setNumeroDireccion(int numeroDireccion) {
        this.numeroDireccion = numeroDireccion;
    }

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getExtrasDireccion() {
        return extrasDireccion;
    }

    public void setExtrasDireccion(String extrasDireccion) {
        this.extrasDireccion = extrasDireccion;
    }
}
