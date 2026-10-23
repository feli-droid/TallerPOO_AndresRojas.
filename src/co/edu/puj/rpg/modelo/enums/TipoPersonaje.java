package co.edu.puj.rpg.modelo.enums;

/** Arquetipos de héroes. Cada constante lleva sus bonificadores inmutables. */
public enum TipoPersonaje {
    GUERRERO(3.0, 2.0),
    MAGO(5.0, 0.0),
    ARQUERO(4.0, 1.0);

    private final double bonificadorAtaqueBase;
    private final double bonificadorDefensaBase;

    // Constructor privado: solo lo usan las constantes de arriba
    private TipoPersonaje(double bonificadorAtaqueBase, double bonificadorDefensaBase) {
        this.bonificadorAtaqueBase = bonificadorAtaqueBase;
        this.bonificadorDefensaBase = bonificadorDefensaBase;
    }

    public double getBonificadorAtaqueBase() { return bonificadorAtaqueBase; }
    public double getBonificadorDefensaBase() { return bonificadorDefensaBase; }
}
