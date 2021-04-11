package com.example.scrummanager.Modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;

public class Proyecto implements Serializable {
    private String projectId;
    private Dictionary<Empleado, String> projectTeam;
    private Cliente projectCliente;
    private String projectSpecifications; //TODO maybe make it a class
    private ArrayList<Sprint> projectSprintList;
    private ArrayList<Date> projectDates;
    private long projectBudget;
    //TODO SLA implementation


    public Proyecto() {
    }

    public Proyecto(String projectId, Dictionary<Empleado, String> projectTeam, String projectSpecifications) {
        this.projectId = projectId;
        this.projectTeam = projectTeam;
        this.projectSpecifications = projectSpecifications;
    }

    public String getProjectId() {
        return projectId;
    }

    public Dictionary<Empleado, String> getProjectTeam() {
        return projectTeam;
    }

    public void setProjectTeam(Dictionary<Empleado, String> projectTeam) {
        this.projectTeam = projectTeam;
    }

    public Cliente getProjectCliente() {
        return projectCliente;
    }

    public void setProjectCliente(Cliente projectCliente) {
        this.projectCliente = projectCliente;
    }

    public String getProjectSpecifications() {
        return projectSpecifications;
    }

    public void setProjectSpecifications(String projectSpecifications) {
        this.projectSpecifications = projectSpecifications;
    }

    public ArrayList<Sprint> getProjectSprintList() {
        return projectSprintList;
    }

    public void setProjectSprintList(ArrayList<Sprint> projectSprintList) {
        this.projectSprintList = projectSprintList;
    }

    public ArrayList<Date> getProjectDates() {
        return projectDates;
    }

    public void setProjectDates(ArrayList<Date> projectDates) {
        this.projectDates = projectDates;
    }

    public long getProjectBudget() {
        return projectBudget;
    }

    public void setProjectBudget(long projectBudget) {
        this.projectBudget = projectBudget;
    }
}
