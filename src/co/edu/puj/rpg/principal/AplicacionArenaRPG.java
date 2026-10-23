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
import java.util.Scanner;

public class AplicacionArenaRPG {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        // Colecciones globales para listar Gremios, Escuadrones y Personajes
        List<Personaje> personajes = new ArrayList<>();
        List<Gremio> gremios = new ArrayList<>();
        List<Escuadron> escuadrones = new ArrayList<>();

        try {
            do {
                System.out.println("\n=== MENU ARENA DE HEROES RPG ===");
                System.out.println("1. Crear Personaje");
                System.out.println("2. Crear Gremio");
                System.out.println("3. Reclutar Personaje a Escuadron");
                System.out.println("4. Equipar Arma a Personaje");
                System.out.println("5. Mostrar Estado Detallado (Personaje y Corazon RPG)");
                System.out.println("6. Salir");
                opcion = leerEntero(scanner, "Seleccione una opcion: ");

                switch (opcion) {
                    case 1:
                        crearPersonaje(scanner, personajes);
                        break;
                    case 2:
                        crearGremio(scanner, gremios, personajes);
                        break;
                    case 3:
                        reclutarEnEscuadron(scanner, escuadrones, personajes);
                        break;
                    case 4:
                        equiparArma(scanner, personajes);
                        break;
                    case 5:
                        mostrarEstado(personajes, gremios, escuadrones);
                        break;
                    case 6:
                        System.out.println("Cerrando la Arena de Heroes...");
                        break;
                    default:
                        System.out.println("Opcion invalida.");
                }
            } while (opcion != 6);
        } catch (NoSuchElementException e) {
            System.out.println("\n(Se acabo la entrada de datos; fin del programa.)");
        }
    }

    // Opcion 1: solicitar nombre e instanciar un nuevo Personaje
    private static void crearPersonaje(Scanner scanner, List<Personaje> personajes) {
        String nombre = leerTexto(scanner, "Nombre del personaje: ");
        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacio.");
            return;
        }
        System.out.println("1. Guerrero   2. Mago");
        int tipo = leerEntero(scanner, "Clase: ");
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

    // Opcion 2: solicitar nombre e instanciar un nuevo Gremio (y, si se desea, unir un personaje)
    private static void crearGremio(Scanner scanner, List<Gremio> gremios, List<Personaje> personajes) {
        String nombre = leerTexto(scanner, "Nombre del gremio: ");
        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacio.");
            return;
        }
        Gremio gremio = new Gremio(nombre);
        gremios.add(gremio);
        System.out.println("Gremio " + nombre + " creado.");

        if (!personajes.isEmpty()) {
            int unir = leerEntero(scanner, "Unir un personaje a este gremio ahora? (1 = si, 0 = no): ");
            if (unir == 1) {
                Personaje p = elegirPersonaje(scanner, personajes);
                if (p != null) {
                    Gremio anterior = p.getGremio();
                    p.unirseAGremio(gremio);                       // asociacion bidireccional
                    if (anterior != null) {
                        System.out.printf("%s dejo el gremio %s.%n", p.getNombre(), anterior.getNombre());
                    }
                    System.out.printf("%s ahora pertenece al gremio %s.%n", p.getNombre(), nombre);
                }
            }
        }
    }

    // Opcion 3: seleccionar un Personaje existente y agregarlo a un Escuadron (Agregacion)
    private static void reclutarEnEscuadron(Scanner scanner, List<Escuadron> escuadrones, List<Personaje> personajes) {
        if (personajes.isEmpty()) {
            System.out.println("No hay personajes. Crea uno con la opcion 1.");
            return;
        }
        Personaje p = elegirPersonaje(scanner, personajes);
        if (p == null) return;

        System.out.println("Escuadrones:");
        for (int i = 0; i < escuadrones.size(); i++) {
            System.out.printf("  %d. %s (%d miembros)%n", i + 1, escuadrones.get(i).getNombre(),
                    escuadrones.get(i).getMiembros().size());
        }
        System.out.println("  0. Crear un escuadron nuevo");
        int n = leerEntero(scanner, "Elige escuadron: ");

        Escuadron escuadron;
        if (n == 0) {
            String nombre = leerTexto(scanner, "Nombre del nuevo escuadron: ");
            if (nombre.isEmpty()) {
                System.out.println("El nombre no puede estar vacio.");
                return;
            }
            escuadron = new Escuadron(nombre);
            escuadrones.add(escuadron);
        } else if (n >= 1 && n <= escuadrones.size()) {
            escuadron = escuadrones.get(n - 1);
        } else {
            System.out.println("Seleccion invalida.");
            return;
        }
        escuadron.reclutarPersonaje(p);
    }

    // Opcion 4: instanciar un Arma y asignarla a un Personaje (Asociacion Unidireccional)
    private static void equiparArma(Scanner scanner, List<Personaje> personajes) {
        if (personajes.isEmpty()) {
            System.out.println("No hay personajes. Crea uno con la opcion 1.");
            return;
        }
        Personaje p = elegirPersonaje(scanner, personajes);
        if (p == null) return;
        String nombre = leerTexto(scanner, "Nombre del arma: ");
        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacio.");
            return;
        }
        int danio = leerEntero(scanner, "Danio base del arma: ");
        if (danio < 0) {
            System.out.println("El danio no puede ser negativo.");
            return;
        }
        p.equiparArma(new Arma(nombre, danio));
    }

    // Opcion 5: reporte con StringBuilder + estadisticas con printf
    private static void mostrarEstado(List<Personaje> personajes, List<Gremio> gremios, List<Escuadron> escuadrones) {
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

        StringBuilder reporte = new StringBuilder();
        reporte.append("\n--- DETALLE DE PERSONAJES ---\n");
        for (Personaje p : personajes) {
            reporte.append(p).append("\n\n");              // Personaje.toString() usa StringBuilder
        }
        reporte.append("--- GREMIOS ---\n");
        if (gremios.isEmpty()) reporte.append("(ninguno)\n");
        for (Gremio g : gremios) reporte.append(g).append("\n");
        reporte.append("--- ESCUADRONES ---\n");
        if (escuadrones.isEmpty()) reporte.append("(ninguno)\n");
        for (Escuadron e : escuadrones) reporte.append(e).append("\n");
        System.out.print(reporte);
    }

    // ---------- Utilidades ----------
    private static Personaje elegirPersonaje(Scanner scanner, List<Personaje> personajes) {
        System.out.println("Personajes:");
        for (int i = 0; i < personajes.size(); i++) {
            System.out.printf("  %d. %s (%s)%n", i + 1, personajes.get(i).getNombre(),
                    personajes.get(i).getClass().getSimpleName());
        }
        int n = leerEntero(scanner, "Elige personaje (0 = cancelar): ");
        if (n < 1 || n > personajes.size()) {
            if (n != 0) System.out.println("Seleccion invalida.");
            return null;
        }
        return personajes.get(n - 1);
    }

    private static String leerTexto(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private static int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            String texto = leerTexto(scanner, mensaje);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Ingresa un numero entero.");
            }
        }
    }
}
