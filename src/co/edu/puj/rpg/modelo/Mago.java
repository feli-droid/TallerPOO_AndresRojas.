package co.edu.puj.rpg.modelo;

public class Mago extends Personaje {
    private static final int COSTO_MANA = 15;
    protected int puntosMana;

    public Mago(String nombre, int vida, int ataqueBase, int posX, int posY, int puntosMana) {
        super(nombre, vida, ataqueBase, posX, posY);
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
        int dano = ataqueBase * 2;                  
        System.out.printf("%s lanza una BOLA DE FUEGO sobre %s! (mana -%d)%n",
                nombre, objetivo.getNombre(), COSTO_MANA);
        objetivo.defender(dano);
    }

    @Override
    public void finalizarTurno() {
        puntosMana = Math.min(50, puntosMana + 5);   
    }

    @Override
    public char getSimbolo() { return 'M'; }

    @Override
    public String describir() {
        return super.describir() + String.format(" | Mana %d", puntosMana);
    }
}
