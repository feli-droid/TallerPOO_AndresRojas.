package co.edu.puj.rpg.modelo;

import co.edu.puj.rpg.modelo.enums.TipoPersonaje;

public class Arquero extends Personaje {

    public Arquero(String nombre, int vida, int ataqueBase, int posX, int posY) {
        super(nombre, TipoPersonaje.ARQUERO, vida, ataqueBase, posX, posY);
    }

    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        System.out.printf("%s dispara una LLUVIA DE FLECHAS sobre %s!%n", nombre, objetivo.getNombre());
        objetivo.defender(calcularAtaqueTotal() + 10);
    }

    @Override
    public char getSimbolo() { return 'A'; }
}