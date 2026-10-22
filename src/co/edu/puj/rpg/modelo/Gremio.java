package co.edu.puj.rpg.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Gremio: extremo "1" de la asociación bidireccional Personaje <-> Gremio (0..*). */
public class Gremio {
    private String nombre;
    private List<Personaje> miembros;

    public Gremio(String nombre) {
        this.nombre = nombre;
        this.miembros = new ArrayList<>();
    }

    public void agregarMiembro(Personaje p) {
        if (p == null) return;                               // 1. validar null
        if (!miembros.contains(p)) {                         // 2. no duplicar
            miembros.add(p);                                 // 3. añadir a la lista
        }
        if (p.getGremio() != this) {                         // 4. sincronización cruzada
            p.unirseAGremio(this);                           //    (el 'if' corta la recursión infinita)
        }
    }

    public void removerMiembro(Personaje p) {
        if (p == null) return;
        miembros.remove(p);
        if (p.getGremio() == this) {
            p.salirDeGremio();
        }
    }

    public String getNombre() { return nombre; }

    /** Vista de solo lectura: nadie externo puede modificar la lista interna. */
    public List<Personaje> getMiembros() {
        return Collections.unmodifiableList(miembros);
    }

    /** Imprime SOLO los nombres de los miembros (no su toString) para evitar StackOverflowError. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gremio ").append(nombre).append(" [").append(miembros.size()).append(" miembros]: ");
        if (miembros.isEmpty()) {
            sb.append("(vacio)");
        }
        for (int i = 0; i < miembros.size(); i++) {
            sb.append(miembros.get(i).getNombre());
            if (i < miembros.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }
}  @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gremio ").append(nombre).append(" [").append(miembros.size()).append(" miembros]: ");
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
