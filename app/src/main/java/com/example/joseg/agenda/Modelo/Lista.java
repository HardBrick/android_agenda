package com.example.joseg.agenda.Modelo;

import java.util.List;

public class Lista {

    private String nombre;
    private List<Tarea> tareas;

    public Lista(String nombre, List<Tarea> tareas) {
        this.nombre = nombre;
        this.tareas = tareas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;

    }

    @Override
    public String toString() {
        return nombre;
    }
}
