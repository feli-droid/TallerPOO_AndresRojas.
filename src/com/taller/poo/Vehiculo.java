package com.taller.poo;

/*
 * ELECCIÓN ENTRE PRIMITIVOS Y WRAPPERS
 * - Memoria: un primitivo (double, int, boolean) guarda el valor directamente
 *   (8, 4 y ~1 byte). Un Wrapper (Double, Integer, Boolean) es un objeto en el
 *   heap: además del valor tiene cabecera de objeto y la variable guarda una
 *   referencia, por lo que consume bastante más memoria.
 * - Nulidad: un primitivo NUNCA puede ser null (por defecto vale 0.0, 0 o false).
 *   Un Wrapper SÍ puede ser null, útil para representar "dato desconocido",
 *   pero obliga a validar null para evitar NullPointerException.
 * - Aquí un vehículo siempre tiene kilometraje, velocidad y estado del motor,
 *   así que se usan primitivos: menos memoria y sin riesgo de null.
 */
public class Vehiculo {
    private String marca;
    private String modelo;
    // double primitivo: el kilometraje siempre existe (mínimo 0.0), no necesita null.
    private double kilometraje;
    // int primitivo: la velocidad máxima siempre es un valor concreto.
    private int velocidadMaxima;
    // boolean primitivo: el motor solo está encendido o apagado (no hay tercer estado).
    private boolean encendido;

    // Constructor por defecto
    public Vehiculo() {
        this.marca = "Genérico";
        this.modelo = "Sin Modelo";
        this.kilometraje = 0.0;
        this.velocidadMaxima = 120;
        this.encendido = false;
    }

    // Constructor parametrizado (parámetros con el mismo nombre que los atributos)
    public Vehiculo(String marca, String modelo, double kilometraje,
                    int velocidadMaxima, boolean encendido) {
        this.marca = marca;
        this.modelo = modelo;
        this.kilometraje = kilometraje;
        this.velocidadMaxima = velocidadMaxima;
        this.encendido = encendido;
    }

    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public double getKilometraje() { return kilometraje; }
    public int getVelocidadMaxima() { return velocidadMaxima; }
    public boolean isEncendido() { return encendido; }
}