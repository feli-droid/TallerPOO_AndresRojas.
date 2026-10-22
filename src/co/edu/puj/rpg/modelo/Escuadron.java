package co.edu.puj.rpg.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Escuadron {
    private String nombre;
    private List<Personaje> miembros; // Agregación (0..*)

    public Escuadron(String nombre) {
        this.nombre = nombre;
        this.miembros = new ArrayList<>();
    }

    public void reclutarPersonaje(Personaje p) {
        if (p == null) {
            System.out.println("No se puede reclutar un personaje nulo.");
            return;
        }
        if (miembros.contains(p)) {
            System.out.printf("%s ya pertenece al escuadron %s.%n", p.getNombre(), nombre);
            return;
        }
        miembros.add(p);
        System.out.printf("%s fue reclutado en el escuadron %s.%n", p.getNombre(), nombre);
    }

    public void expulsarPersonaje(Personaje p) {
        if (p != null && miembros.remove(p)) {
            System.out.printf("%s fue expulsado del escuadron %s (sigue existiendo con su estado intacto).%n",
                    p.getNombre(), nombre);
        } else {
            System.out.println("Ese personaje no pertenece al escuadron " + nombre + ".");
        }
    }

    public int calcularVidaTotal() {
        int total = 0;
        for (Personaje p : miembros) total += p.getVida();
        return total;
    }

    public String getNombre() { return nombre; }

    public List<Personaje> getMiembros() {
        return Collections.unmodifiableList(miembros);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Escuadron ").append(nombre).append(" [").append(miembros.size()).append(" miembros, vida total ")
                .append(calcularVidaTotal()).append("]: ");
        if (miembros.isEmpty()) {
            sb.append("(vacio)");
        }
        for (int i = 0; i < miembros.size(); i++) {
            sb.append(miembros.get(i).getNombre());
            if (i < miembros.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }
}