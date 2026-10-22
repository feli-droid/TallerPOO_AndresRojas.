package co.edu.puj.rpg.modelo;

public class Mago extends Personaje {
    private static final int COSTO_MANA = 15;
    protected int puntosMana;

    public Mago(String nombre, int vida, int ataqueBase, int posX, int posY, int puntosMana) {
        super(nombre, vida, ataqueBase, posX, posY, puntosMana);   // el corazon guarda el mana base
        this.puntosMana = puntosMana;
    }

    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        if (puntosMana < COSTO_MANA) {
            System.out.printf("%s no tiene mana suficiente; golpea con su baston.%n", nombre);
            atacar(objetivo);
            return;
        }
        puntosMana -= COSTO_MANA;
        int dano = calcularAtaqueTotal() * 2;          // Bola de fuego
        System.out.printf("%s lanza una BOLA DE FUEGO sobre %s! (mana -%d)%n",
                nombre, objetivo.getNombre(), COSTO_MANA);
        objetivo.defender(dano);
    }

    @Override
    public void finalizarTurno() {
        puntosMana = Math.min(getCorazon().getManaBase(), puntosMana + 5);   // regenera 5 de mana por turno
    }

    @Override
    public void descansar() {
        super.descansar();
        puntosMana = getCorazon().getManaBase();       // el descanso tambien restaura el mana
    }

    @Override
    public char getSimbolo() { return 'M'; }

    @Override
    public String describir() {
        return super.describir() + String.format(" | Mana %d", puntosMana);
    }
}