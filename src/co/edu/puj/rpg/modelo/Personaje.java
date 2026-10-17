package co.edu.puj.rpg.modelo;

import co.edu.puj.rpg.modelo.enemigo.Enemigo;

public class Personaje {
    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private int nivel;
    private double oro;
    private boolean estaVivo;

    // Extras para los retos
    private int experiencia;
    private int pociones;
    private int pocionesGrandes;
    private String ultimoEvento;

    // Constructor completo
    public Personaje(String nombre, int vidaMaxima, int puntosAtaque, double oro) {
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima;
        this.puntosAtaque = puntosAtaque;
        this.oro = oro < 0 ? 0 : oro;
        this.nivel = 1;
        this.experiencia = 0;
        this.pociones = 2;
        this.pocionesGrandes = 0;
        this.estaVivo = true;
        this.puntosVida = vidaMaxima;
        this.ultimoEvento = "";
    }

    // Constructor reducido: reutiliza el completo con valores por defecto
    public Personaje(String nombre) {
        this(nombre, 100, 15, 50.0);
    }

    // ---------- Comportamiento ----------
    public void atacar(Enemigo enemigo) {
        int dano = this.puntosAtaque;
        boolean critico = Math.random() < 0.15;
        if (critico) {
            dano *= 2;
        }
        enemigo.recibirDano(dano);
        this.ultimoEvento = String.format("%s ataca a %s y causa %d de dano%s.",
                nombre, enemigo.getNombre(), dano, critico ? " (GOLPE CRITICO!)" : "");
    }

    public void usarPocion(int cantidadRestaurada) {
        int antes = this.puntosVida;
        setPuntosVida(this.puntosVida + cantidadRestaurada);
        this.ultimoEvento = String.format("%s usa una pocion y recupera %d de vida.",
                nombre, this.puntosVida - antes);
    }

    public void recibirDano(int cantidadDano) {
        setPuntosVida(this.puntosVida - cantidadDano);
        this.ultimoEvento = String.format("%s recibe %d de dano.", nombre, cantidadDano);
    }

    // ---------- Inventario ----------
    public int consumirPocion() {
        if (pocionesGrandes > 0) { pocionesGrandes--; return 60; }
        if (pociones > 0)        { pociones--;        return 30; }
        return 0;
    }

    public void agregarPocionGrande() { pocionesGrandes++; }

    // ---------- Experiencia ----------
    public boolean ganarExperiencia(int xp) {
        this.experiencia += xp;
        int umbral = nivel * 50;
        if (this.experiencia >= umbral) {
            this.experiencia -= umbral;
            setNivel(this.nivel + 1);
            this.vidaMaxima += 10;
            this.puntosAtaque += 3;
            setPuntosVida(this.vidaMaxima);
            return true;
        }
        return false;
    }

    // ---------- Setters validados ----------
    public void setPuntosVida(int vida) {
        if (vida <= 0) {
            this.puntosVida = 0;
            this.estaVivo = false;
        } else if (vida > this.vidaMaxima) {
            this.puntosVida = this.vidaMaxima;
            this.estaVivo = true;
        } else {
            this.puntosVida = vida;
            this.estaVivo = true;
        }
    }

    public void setOro(double oro) {
        if (oro < 0.0) {
            System.out.println("Saldo insuficiente: operacion rechazada.");
            return;
        }
        this.oro = oro;
    }

    public void setNivel(int nivel) {
        if (nivel < 1) {
            return; // se conserva el valor actual
        }
        this.nivel = nivel;
    }

    // ---------- Getters ----------
    public String getNombre() { return nombre; }
    public int getPuntosVida() { return puntosVida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getPuntosAtaque() { return puntosAtaque; }
    public int getNivel() { return nivel; }
    public double getOro() { return oro; }
    public boolean isEstaVivo() { return estaVivo; }
    public int getExperiencia() { return experiencia; }
    public int getPociones() { return pociones; }
    public int getPocionesGrandes() { return pocionesGrandes; }
    public String getUltimoEvento() { return ultimoEvento; }
}
