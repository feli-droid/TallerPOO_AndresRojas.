package com.taller.poo;

public class Estudiante {
    // Sin inicialización en la declaración: el estado lo asignan los constructores.
    private String identificacion;
    private String nombreCompleto;
    private double promedioCalificaciones;
    private boolean estaMatriculado;

    public Estudiante() {
        this.identificacion = "SIN-ID";
        this.nombreCompleto = "Sin Nombre";
        this.promedioCalificaciones = 0.0;
        this.estaMatriculado = false;
    }

    public Estudiante(String identificacion, String nombreCompleto,
                      double promedioCalificaciones, boolean estaMatriculado) {
        this.identificacion = identificacion;
        this.nombreCompleto = nombreCompleto;
        this.promedioCalificaciones = promedioCalificaciones;
        this.estaMatriculado = estaMatriculado;
    }

    // Construye y RETORNA el texto; no imprime nada (eso lo hace la clase ejecutable).
    public String obtenerReporteAcademicos() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Estudiante: %s", nombreCompleto));
        sb.append(String.format(" | Promedio: %.2f", promedioCalificaciones));
        sb.append(String.format(" | Matriculado: %b", estaMatriculado));
        return sb.toString();
    }

    public String getIdentificacion() { return identificacion; }
    public String getNombreCompleto() { return nombreCompleto; }
    public double getPromedioCalificaciones() { return promedioCalificaciones; }
    public boolean isEstaMatriculado() { return estaMatriculado; }
}
