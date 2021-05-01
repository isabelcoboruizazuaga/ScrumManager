package com.example.scrummanager.Modelos;

public class Empresa {
    private String eid;
    private String name;

    public Empresa(String eid, String name) {
        this.eid = eid;
        this.name = name;
    }

    public String getEid() {
        return eid;
    }

    public String getName() {
        return name;
    }
}
