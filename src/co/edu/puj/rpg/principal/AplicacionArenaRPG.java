package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.modelo.Arma;
import co.edu.puj.rpg.modelo.Escuadron;
import co.edu.puj.rpg.modelo.Gremio;
import co.edu.puj.rpg.modelo.Guerrero;
import co.edu.puj.rpg.modelo.Mago;
import co.edu.puj.rpg.modelo.Personaje;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/** Punto de entrada: menú de gestión de la Arena (relaciones entre clases) + batalla. */
public class AplicacionArenaRPG {

    // Colecciones globales
    private static final List<Personaje> personajes = new ArrayList<>();
    private static final List<Gremio> gremios = new ArrayList<>();
    private static final List<Escuadron> escuadrones = new ArrayList<>();

    public static void main(String[] args) {
        int opcion = 0;
        try {
            do {
                System.out.println("\n=== MENU ARENA DE HEROES RPG ===");
                System.out.println("1. Crear Personaje");
                System.out.println("2. Crear Gremio");
                System.out.println("3. Reclutar Personaje a Escuadron");
                System.out.println("4. Equipar Arma a Personaje");
                System.out.println("5. Mostrar Estado Detallado (Personaje y Corazon RPG)");
                System.out.println("6. Salir");
                System.out.println("--- Extras ---");
                System.out.println("7. Unir Personaje a Gremio");
                System.out.println("8. Crear Escuadron");
                System.out.println("9. Expulsar Personaje de Escuadron");
                System.out.println("10. Desequipar Arma");
                System.out.println("11. Disolver Escuadron");
                System.out.println("12. Iniciar Batalla con un Escuadron");
                opcion = Consola.leerEntero("Seleccione una opcion: ");

                switch (opcion) {
                    case 1:  crearPersonaje(); break;
                    case 2:  crearGremio(); break;
                    case 3:  reclutarEnEscuadron(); break;
                    case 4:  equiparArma(); break;
                    case 5:  mostrarEstado(); break;
                    case 6:  System.out.println("Cerrando la Arena de Heroes..."); break;
                    case 7:  unirAGremio(); break;
                    case 8:  crearEscuadron(); break;
                    case 9:  expulsarDeEscuadron(); break;
                    case 10: desequiparArma(); break;
                    case 11: disolverEscuadron(); break;
                    case 12: iniciarBatalla(); break;
                    default: System.out.println("Opcion invalida.");
                }
            } while (opcion != 6);
        } catch (NoSuchElementException e) {
            System.out.println("\n(Se acabo la entrada de datos; fin del programa.)");
        }
    }

    // =====================================================================
    //  OPCIONES DEL MENÚ
    // =====================================================================
    private static void crearPersonaje() {
        String nombre = Consola.leerTexto("Nombre del personaje: ");
        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacio.");
            return;
        }
        System.out.println("1. Guerrero   2. Mago");
        int tipo = Consola.leerEntero("Clase: ");
        Personaje p;
        if (tipo == 1) {
            p = new Guerrero(nombre, 120, 15, 0, 0, 10);
        } else if (tipo == 2) {
            p = new Mago(nombre, 80, 25, 0, 0, 50);
        } else {
            System.out.println("Clase invalida.");
            return;
        }
        personajes.add(p);
        System.out.printf("%s (%s) creado. Corazon: %s%n", nombre, p.getClass().getSimpleName(), p.getCorazon());
    }

    private static void crearGremio() {
        String nombre = Consola.leerTexto("Nombre del gremio: ");
        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacio.");
            return;
        }
        gremios.add(new Gremio(nombre));
        System.out.println("Gremio " + nombre + " creado.");
    }

    private static void crearEscuadron() {
        String nombre = Consola.leerTexto("Nombre del escuadron: ");
        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacio.");
            return;
        }
        escuadrones.add(new Escuadron(nombre));
        System.out.println("Escuadron " + nombre + " creado.");
    }

    private static void reclutarEnEscuadron() {
        Personaje p = elegirPersonaje();
        if (p == null) return;
        Escuadron e = elegirEscuadron();
        if (e == null) return;
        e.reclutarPersonaje(p);                    // AGREGACIÓN
    }

    private static void expulsarDeEscuadron() {
        Escuadron e = elegirEscuadron();
        if (e == null) return;
        if (e.getMiembros().isEmpty()) {
            System.out.println("El escuadron esta vacio.");
            return;
        }
        System.out.println("Miembros de " + e.getNombre() + ":");
        List<Personaje> miembros = e.getMiembros();
        for (int i = 0; i < miembros.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, miembros.get(i).getNombre());
        }
        int n = Consola.leerEntero("Elige a quien expulsar (0 = cancelar): ");
        if (n < 1 || n > miembros.size()) return;
        e.expulsarPersonaje(miembros.get(n - 1));
    }

    private static void disolverEscuadron() {
        Escuadron e = elegirEscuadron();
        if (e == null) return;
        int cantidad = e.getMiembros().size();
        escuadrones.remove(e);
        System.out.printf("Escuadron %s disuelto. Sus %d integrantes siguen existiendo intactos.%n",
                e.getNombre(), cantidad);
    }

    private static void unirAGremio() {
        Personaje p = elegirPersonaje();
        if (p == null) return;
        Gremio g = elegirGremio();
        if (g == null) return;
        Gremio anterior = p.getGremio();
        p.unirseAGremio(g);                        // ASOCIACIÓN BIDIRECCIONAL
        if (anterior != null && anterior != g) {
            System.out.printf("%s dejo el gremio %s.%n", p.getNombre(), anterior.getNombre());
        }
        System.out.printf("%s ahora pertenece al gremio %s.%n", p.getNombre(), g.getNombre());
    }

    private static void equiparArma() {
        Personaje p = elegirPersonaje();
        if (p == null) return;
        String nombre = Consola.leerTexto("Nombre del arma: ");
        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacio.");
            return;
        }
        int danio = Consola.leerEntero("Danio base del arma: ");
        if (danio < 0) {
            System.out.println("El danio no puede ser negativo.");
            return;
        }
        p.equiparArma(new Arma(nombre, danio));    // ASOCIACIÓN UNIDIRECCIONAL
    }

    private static void desequiparArma() {
        Personaje p = elegirPersonaje();
        if (p == null) return;
        p.desequiparArma();
    }

    private static void mostrarEstado() {
        if (personajes.isEmpty()) {
            System.out.println("No hay personajes creados.");
        } else {
            System.out.println();
            System.out.printf("%-12s %-9s %3s %-9s %6s %-18s %-12s %-18s%n",
                    "Nombre", "Clase", "Nv", "Vida", "Ataque", "Arma", "Gremio", "Corazon (Sal/Man)");
            System.out.println("-".repeat(96));
            for (Personaje p : personajes) {
                System.out.printf("%-12s %-9s %3d %-9s %6d %-18s %-12s %-18s%n",
                        p.getNombre(),
                        p.getClass().getSimpleName(),
                        p.getNivel(),
                        p.getVida() + "/" + p.getVidaMaxima(),
                        p.calcularAtaqueTotal(),
                        p.getArmaEquipada() != null ? p.getArmaEquipada().getNombre() : "Desarmado",
                        p.getGremio() != null ? p.getGremio().getNombre() : "Sin gremio",
                        p.getCorazon().getSaludBase() + "/" + p.getCorazon().getManaBase());
            }
        }

        System.out.println("\n-- Gremios --");
        if (gremios.isEmpty()) System.out.println("(ninguno)");
        for (Gremio g : gremios) System.out.println(g);

        System.out.println("-- Escuadrones --");
        if (escuadrones.isEmpty()) System.out.println("(ninguno)");
        for (Escuadron e : escuadrones) System.out.println(e);

        if (!personajes.isEmpty()) {
            int n = Consola.leerEntero("\nVer reporte detallado de un personaje (0 = no): ");
            if (n >= 1 && n <= personajes.size()) {
                System.out.println(personajes.get(n - 1));   // toString() con StringBuilder
            }
        }
    }

    private static void iniciarBatalla() {
        Escuadron e = elegirEscuadron();
        if (e == null) return;
        if (e.getMiembros().isEmpty()) {
            System.out.println("El escuadron esta vacio: recluta personajes primero (opcion 3).");
            return;
        }
        Batalla.iniciar(new ArrayList<>(e.getMiembros()));
        System.out.println("\nVolviendo al menu... los personajes siguen en el escuadron " + e.getNombre() + ".");
    }

    // =====================================================================
    //  SELECTORES
    // =====================================================================
    private static Personaje elegirPersonaje() {
        if (personajes.isEmpty()) {
            System.out.println("No hay personajes. Crea uno con la opcion 1.");
            return null;
        }
        System.out.println("Personajes:");
        for (int i = 0; i < personajes.size(); i++) {
            System.out.printf("  %d. %s (%s)%n", i + 1, personajes.get(i).getNombre(),
                    personajes.get(i).getClass().getSimpleName());
        }
        int n = Consola.leerEntero("Elige personaje (0 = cancelar): ");
        if (n < 1 || n > personajes.size()) {
            if (n != 0) System.out.println("Seleccion invalida.");
            return null;
        }
        return personajes.get(n - 1);
    }

    private static Gremio elegirGremio() {
        if (gremios.isEmpty()) {
            System.out.println("No hay gremios. Crea uno con la opcion 2.");
            return null;
        }
        System.out.println("Gremios:");
        for (int i = 0; i < gremios.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, gremios.get(i).getNombre());
        }
        int n = Consola.leerEntero("Elige gremio (0 = cancelar): ");
        if (n < 1 || n > gremios.size()) {
            if (n != 0) System.out.println("Seleccion invalida.");
            return null;
        }
        return gremios.get(n - 1);
    }

    private static Escuadron elegirEscuadron() {
        if (escuadrones.isEmpty()) {
            System.out.println("No hay escuadrones. Crea uno con la opcion 8.");
            return null;
        }
        System.out.println("Escuadrones:");
        for (int i = 0; i < escuadrones.size(); i++) {
            System.out.printf("  %d. %s (%d miembros)%n", i + 1, escuadrones.get(i).getNombre(),
                    escuadrones.get(i).getMiembros().size());
        }
        int n = Consola.leerEntero("Elige escuadron (0 = cancelar): ");
        if (n < 1 || n > escuadrones.size()) {
            if (n != 0) System.out.println("Seleccion invalida.");
            return null;
        }
        return escuadrones.get(n - 1);
    }
}
