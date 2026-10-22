package co.edu.puj.rpg.modelo.enemigo;

import co.edu.puj.rpg.modelo.Personaje;

/** Enemigo: ahora es un Personaje (herencia), por eso cabe en las mismas listas y tablero. */
public class Enemigo extends Personaje {
    private int experienciaRecompensa;
    private double oroRecompensa;

    public Enemigo(String nombre, int vida, int ataqueBase, int posX, int posY,
                   int experienciaRecompensa, double oroRecompensa) {
        super(nombre, vida, ataqueBase, posX, posY);
        this.experienciaRecompensa = experienciaRecompensa;
        this.oroRecompensa = oroRecompensa;
    }

    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        System.out.printf("%s lanza un golpe brutal a %s!%n", nombre, objetivo.getNombre());
        objetivo.defender(calcularAtaqueTotal());
    }

    @Override
    public char getSimbolo() { return 'E'; }

    public int getExperienciaRecompensa() { return experienciaRecompensa; }
    public double getOroRecompensa() { return oroRecompensa; }
}