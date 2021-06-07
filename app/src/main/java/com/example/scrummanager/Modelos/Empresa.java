package com.example.scrummanager.Modelos;

/**
 * Modelo de Empresa
 */
public class Empresa {
    private String eid;
    private String name;

    /**
     * Constructor
     * @param eid
     * @param name
     */
    public Empresa(String eid, String name) {
        this.eid = eid;
        this.name = name;
    }

    /**
     * Getter de eid
     * @return String
     */
    public String getEid() {
        return eid;
    }

    /**
     * Getter de name
     * @return String
     */
    public String getName() {
        return name;
    }
}
