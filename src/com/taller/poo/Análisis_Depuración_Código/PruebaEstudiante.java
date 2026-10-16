package com.taller.poo.Análisis_Depuración_Código;

import com.taller.poo.Estudiante;

/*
 * CASO DE ESTUDIO 1: NullPointerException
 *
 * CÓDIGO ORIGINAL CON ERROR (main):
 *     Estudiante estudiante1;
 *     String reporte = estudiante1.obtenerReporteAcademicos();
 *     System.out.println(reporte);
 *
 * PREGUNTA 1. ¿Qué error indica el compilador o qué excepción se lanza?
 *   R: El compilador marca "variable estudiante1 might not have been initialized"
 *      (la variable local no fue inicializada). Si se escribiera
 *      "Estudiante estudiante1 = null;" compilaría, pero en ejecución se lanzaría
 *      java.lang.NullPointerException al invocar el método sobre null.
 *
 * PREGUNTA 2. ¿Por qué estudiante1 no puede invocar el método de instancia?
 *   R: Porque solo es una referencia (una "etiqueta") y no apunta a ningún objeto.
 *      Un método de instancia necesita un objeto real en memoria con sus
 *      atributos (nombreCompleto, promedio, etc.). Sin objeto no hay estado.
 *
 * PREGUNTA 3. Instrucción para instanciar correctamente:
 *   R: Estudiante estudiante1 = new Estudiante("E001", "Laura Martínez", 4.35, true);
 *
 * ESTADO: CORREGIDO (ver main).
 */
public class PruebaEstudiante {
    public static void main(String[] args) {
        // CORRECCIÓN: se crea el objeto con new antes de usarlo
        Estudiante estudiante1 = new Estudiante("E001", "Laura Martínez", 4.35, true);

        String reporte = estudiante1.obtenerReporteAcademicos();
        System.out.println(reporte);
    }
}
