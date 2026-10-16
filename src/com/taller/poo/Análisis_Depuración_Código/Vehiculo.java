package com.taller.poo.Análisis_Depuración_Código;

/*
 * CASO DE ESTUDIO 4: Sombreado de Variables (Variable Shadowing) por Ausencia de this
 *
 * PREGUNTAS ANALÍTICAS:
 *
 * 1. Si se instancia la clase mediante Vehiculo v = new Vehiculo(200, true); y luego
 *    se invoca v.imprimirEstado();, ¿qué valores se proyectarán en la consola y por qué?
 *    R: Con el código ORIGINAL se imprime:
 *           Velocidad Máxima: 0 | Encendido: false
 *       Los parámetros ocultan a los atributos, así que "velocidadMaxima = velocidadMaxima;"
 *       asigna el parámetro a sí mismo. Los atributos nunca reciben valor y conservan
 *       su valor por defecto (int = 0, boolean = false).
 *
 * 2. Explique el fenómeno de sombreado de variables (variable shadowing) y cómo afecta
 *    la asignación cuando el nombre del parámetro coincide con el del atributo.
 *    R: Ocurre cuando una variable local o parámetro tiene el mismo nombre que un
 *       atributo de la clase: dentro de ese bloque el nombre simple se refiere a la
 *       variable local y "oculta" al atributo. Por eso la asignación no modifica el
 *       atributo. Para acceder a él hay que usar this.nombreAtributo. Java no lanza
 *       error, por lo que es un fallo silencioso.
 *
 * 3. Reescriba el bloque del constructor corrigiendo el error mediante la palabra clave this.
 *    R: Ver el constructor de abajo (versión corregida).
 *
 * ESTADO: CORREGIDO. Al ejecutar main se imprime:
 *         Velocidad Máxima: 200 | Encendido: true
 *
 * NOTA: esta clase está en el paquete com.taller.poo.depuracion para poder llamarse
 * Vehiculo (igual que en el enunciado) sin chocar con la clase Vehiculo del taller.
 */
public class Vehiculo {
    private int velocidadMaxima;
    private boolean encendido;

    public Vehiculo(int velocidadMaxima, boolean encendido) {
        // CÓDIGO ORIGINAL CON ERROR (asignación de variables omitiendo la referencia 'this'):
        // velocidadMaxima = velocidadMaxima;
        // encendido = encendido;

        // CORRECCIÓN: this distingue el atributo del parámetro
        this.velocidadMaxima = velocidadMaxima;
        this.encendido = encendido;
    }

    public void imprimirEstado() {
        System.out.printf("Velocidad Máxima: %d | Encendido: %b%n",
                velocidadMaxima, encendido);
    }

    public static void main(String[] args) {
        Vehiculo v = new Vehiculo(200, true);
        v.imprimirEstado();
    }
}
