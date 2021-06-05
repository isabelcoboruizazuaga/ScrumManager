package com.example.scrummanager.Modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class Sprint implements Serializable {
    private String idSprint;
    private ArrayList<Tarea> listaTareasSprint;
    private ArrayList<Date> fechasSprint;
    private String objetivoSprint;
    private String color;
    private String nombre;
    private ArrayList<String> colors= new ArrayList<>();
    private boolean showMenu= false;

    public Sprint() {
    }

    public Sprint(String idSprint, String nombre, ArrayList<Date> fechasSprint) {
        this.nombre=nombre;
        this.idSprint = idSprint;
        this.fechasSprint = fechasSprint;
        this.setColors();
        this.setColor();
    }

    private void setColors(){
        colors.add("#A9E7EF9D");
        colors.add("#A99F80D3");
        colors.add("#A8C483DC");
        colors.add("#A996EA9A");
        colors.add("#A868AAC8");
        colors.add("#089f80");
        colors.add("#8BC56B89");
    }

    private void setColor(){
        int numeroAleatorio = ThreadLocalRandom.current().nextInt(0, colors.size());
        this.color=colors.get(numeroAleatorio);
    }

    public String getColor(){
        return this.color;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isShowMenu() {
        return showMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    public String getIdSprint() {
        return idSprint;
    }

    public ArrayList<Tarea> getListaTareasSprint() {
        return listaTareasSprint;
    }

    public void setListaTareasSprint(ArrayList<Tarea> listaTareasSprint) {
        this.listaTareasSprint = listaTareasSprint;
    }

    public ArrayList<Date> getFechasSprint() {
        return fechasSprint;
    }

    public void setFechasSprint(ArrayList<Date> fechasSprint) {
        this.fechasSprint = fechasSprint;
    }

    public String getObjetivoSprint() {
        return objetivoSprint;
    }

    public void setObjetivoSprint(String objetivoSprint) {
        this.objetivoSprint = objetivoSprint;
    }

    public void nuevaTarea(Tarea tarea){
        try {
            if(!comprobarListaTareas(tarea)) {
                this.listaTareasSprint.add(tarea);
            }
        }catch (java.lang.NullPointerException e){
            listaTareasSprint = new ArrayList<>();
            if(!comprobarListaTareas(tarea)) {
                this.listaTareasSprint.add(tarea);
            }
        }
    }

    public boolean comprobarListaTareas(Tarea tarea){
        boolean existe= false;
        for(int i= 0; i<listaTareasSprint.size();i++){
            //listaTareasSprint.get(i).setShowMenu(false);

            if(tarea.getIdTarea().equals(listaTareasSprint.get(i).getIdTarea())){
                existe=true;
            }
        }
        return existe;
    }
    public void eliminarTarea(Tarea tarea){
        try {
            if(comprobarListaTareas(tarea)) {
                for (int i=0;i<listaTareasSprint.size();i++){
                    if(tarea.getIdTarea().equals(listaTareasSprint.get(i).getIdTarea())){
                        listaTareasSprint.remove(listaTareasSprint.get(i));
                    }
                }
                this.listaTareasSprint.remove(tarea);
            }
        }catch (java.lang.NullPointerException e){
        }
    }
}
