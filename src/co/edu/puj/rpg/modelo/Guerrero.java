package co.edu.puj.rpg.modelo;

import co.edu.puj.rpg.modelo.enums.TipoPersonaje;

public class Guerrero extends Personaje {
    protected int fuerzaEscudo;

    public Guerrero(String nombre, int vida, int ataqueBase, int posX, int posY, int fuerzaEscudo) {
        super(nombre, TipoPersonaje.GUERRERO, vida, ataqueBase, posX, posY);   // inicializa el estado heredado
        this.fuerzaEscudo = fuerzaEscudo;
    }

    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        int dano = calcularAtaqueTotal() + fuerzaEscudo;   // Golpe de escudo
        System.out.printf("%s lanza un GOLPE DE ESCUDO sobre %s!%n", nombre, objetivo.getNombre());
        objetivo.defender(dano);
    }

    @Override
    public void defender(int danoRecibido) {
        int reducido = Math.max(1, danoRecibido - fuerzaEscudo / 2);
        System.out.printf("  (el escudo de %s absorbe %d de dano)%n", nombre, danoRecibido - reducido);
        super.defender(reducido);
    }

    @Override
    public char getSimbolo() { return 'G'; }

    @Override
    public String describir() {
        return super.describir() + String.format(" | Escudo %d", fuerzaEscudo);
    }
}
