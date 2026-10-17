package co.edu.puj.rpg.modelo.enemigo;

import co.edu.puj.rpg.modelo.Personaje;

public class Enemigo {
    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private int experienciaRecompensa;
    private double oroRecompensa;
    private boolean estaVivo;

    public Enemigo(String nombre, int vidaMaxima, int puntosAtaque,
                   int experienciaRecompensa, double oroRecompensa) {
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima;
        this.puntosVida = vidaMaxima;
        this.puntosAtaque = puntosAtaque;
        this.experienciaRecompensa = experienciaRecompensa;
        this.oroRecompensa = oroRecompensa;
        this.estaVivo = true;
    }

    // Setter validado (misma lógica que el del héroe)
    public void setPuntosVida(int vida) {
        if (vida <= 0) {
            this.puntosVida = 0;
            this.estaVivo = false;
        } else {
            this.puntosVida = Math.min(vida, vidaMaxima);
            this.estaVivo = true;
        }
    }

    public void recibirDano(int cantidadDano) {
        setPuntosVida(this.puntosVida - cantidadDano);
    }

    public void atacar(Personaje heroe) {
        heroe.recibirDano(this.puntosAtaque);
    }

    public String getNombre() { return nombre; }
    public int getPuntosVida() { return puntosVida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getPuntosAtaque() { return puntosAtaque; }
    public int getExperienciaRecompensa() { return experienciaRecompensa; }
    public double getOroRecompensa() { return oroRecompensa; }
    public boolean isEstaVivo() { return estaVivo; }
}
