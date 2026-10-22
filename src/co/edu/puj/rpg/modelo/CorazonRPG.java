package co.edu.puj.rpg.modelo;

public class CorazonRPG {
    private int saludBase;
    private int manaBase;

    public CorazonRPG(int saludBase, int manaBase) {
        this.saludBase = saludBase;
        this.manaBase = manaBase;
    }

    public int getSaludBase() { return saludBase; }
    public int getManaBase() { return manaBase; }

    // Manipulación interna:
    void aumentarSaludBase(int cantidad) { this.saludBase += cantidad; }
    void aumentarManaBase(int cantidad) { this.manaBase += cantidad; }

    @Override
    public String toString() {
        return String.format("Salud base %d | Mana base %d", saludBase, manaBase);
    }
}
