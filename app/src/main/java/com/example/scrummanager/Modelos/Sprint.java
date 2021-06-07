package com.example.scrummanager.Modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Modelo de Sprint
 */
public class Sprint implements Serializable {
    private String idSprint;
    private ArrayList<Tarea> listaTareasSprint;
    private ArrayList<Date> fechasSprint;
    private String objetivoSprint;
    private String color;
    private String nombre;
    private ArrayList<String> colors= new ArrayList<>();
    private boolean showMenu= false;

    /**
     * Constructor por defecto
     */
    public Sprint() {
    }

    /**
     * Constructor
     * @param idSprint
     * @param nombre
     * @param fechasSprint
     */
    public Sprint(String idSprint, String nombre, ArrayList<Date> fechasSprint) {
        this.nombre=nombre;
        this.idSprint = idSprint;
        this.fechasSprint = fechasSprint;
        this.setColors();
        this.setColor();
    }

    /**
     * Rellena el ArrayList de colores
     */
    private void setColors(){
        colors.add("#A9E7EF9D");
        colors.add("#A99F80D3");
        colors.add("#A8C483DC");
        colors.add("#A996EA9A");
        colors.add("#A868AAC8");
        colors.add("#089f80");
        colors.add("#8BC56B89");
    }

    /**
     * Setter de Color
     * Escoge un color aleatorio de la lista
     */
    private void setColor(){
        int numeroAleatorio = ThreadLocalRandom.current().nextInt(0, colors.size());
        this.color=colors.get(numeroAleatorio);
    }

    /**
     * Getter de color
     * @return color: String
     */
    public String getColor(){
        return this.color;
    }

    /**
     * Getter de nombre
     * @return nombre: String
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Setter de nombre
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Getter de showMenu
     * @return showMenu: boolean
     */
    public boolean isShowMenu() {
        return showMenu;
    }

    /**
     * Setter de showMenu
     * @param showMenu
     */
    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    /**
     * Getter de idSprint
     * @return idSprint: String
     */
    public String getIdSprint() {
        return idSprint;
    }

    /**
     * Getter de listaTareasSprint
     * @return listaTareasSprint ArrayList<Tarea>
     * @see Tarea
     */
    public ArrayList<Tarea> getListaTareasSprint() {
        return listaTareasSprint;
    }

    /**
     * Setter de listaTareasSprint
     * @param listaTareasSprint
     * @see Tarea
     */
    public void setListaTareasSprint(ArrayList<Tarea> listaTareasSprint) {
        this.listaTareasSprint = listaTareasSprint;
    }

    /**
     * Getter de fechasSprint
     * @return fechasSprint: ArrayList<Date>
     */
    public ArrayList<Date> getFechasSprint() {
        return fechasSprint;
    }

    /**
     * Setter de fechasSprint
     * @param fechasSprint
     */
    public void setFechasSprint(ArrayList<Date> fechasSprint) {
        this.fechasSprint = fechasSprint;
    }

    /**
     * Getter de objetivoSprint
     * @return objetivoSprint: String
     */
    public String getObjetivoSprint() {
        return objetivoSprint;
    }

    /**
     * Setter de objetivoSprint
     * @param objetivoSprint
     */
    public void setObjetivoSprint(String objetivoSprint) {
        this.objetivoSprint = objetivoSprint;
    }

    /**
     * Añade una tarea a la lista de tareas del Sprint
     * @param tarea
     * @see Tarea
     */
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

    /**
     * Comprueba si una tarea está en la lista de Tareas del sprint
     * @param tarea
     * @return existe: boolean
     */
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

    /**
     * Elimina una tarea de la lista de Tareas de un Sprint
     * @param tarea
     */
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
