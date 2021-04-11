package com.example.scrummanager.Modelos;

public class TareaIncidencia extends Tarea {
    private String reporteError;

    public TareaIncidencia() {
    }

    public TareaIncidencia(String nombreTarea, String descripcionTarea, String sprintTarea, String reporteError) {
        super(nombreTarea,descripcionTarea,sprintTarea);
        this.reporteError = reporteError;
    }

    //TODO maybe make it a Task atribute
}
