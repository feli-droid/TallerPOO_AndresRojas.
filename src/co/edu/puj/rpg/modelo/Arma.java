package co.edu.puj.rpg.modelo;

import co.edu.puj.rpg.modelo.enums.RaridadItem;

/** Arma: NO conoce a su dueño (asociación unidireccional Personaje -> Arma). */
public class Arma {
    private final String nombre;
    private final int danioBase;
    private final RaridadItem rareza;

    public Arma(String nombre, int danioBase) {
        this(nombre, danioBase, RaridadItem.COMUN);
    }

    public Arma(String nombre, int danioBase, RaridadItem rareza) {
        this.nombre = nombre;
        this.danioBase = Math.max(0, danioBase);
        this.rareza = rareza;
    }

    public String getNombre() { return nombre; }
    public RaridadItem getRareza() { return rareza; }

    /** Daño final: el daño base escalado por el multiplicador de la rareza. */
    public int getDanioBase() {
        return (int) Math.round(danioBase * rareza.getMultiplicadorStat());
    }

    @Override
    public String toString() {
        return String.format("%s [%s] (+%d dano)", nombre, rareza, getDanioBase());
    }
}
