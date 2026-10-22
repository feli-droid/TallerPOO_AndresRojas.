package co.edu.puj.rpg.modelo;

/** Arma: NO conoce a su dueño (asociación unidireccional Personaje -> Arma). */
public class Arma {
    private final String nombre;
    private final int danioBase;

    public Arma(String nombre, int danioBase) {
        this.nombre = nombre;
        this.danioBase = Math.max(0, danioBase);
    }

    public String getNombre() { return nombre; }
    public int getDanioBase() { return danioBase; }

    @Override
    public String toString() {
        return String.format("%s (+%d dano)", nombre, danioBase);
    }
}
