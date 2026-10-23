package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.engine.Bestiario;
import co.edu.puj.rpg.engine.EstadisticasCombate;
import co.edu.puj.rpg.exception.CapacidadExcedidaException;
import co.edu.puj.rpg.exception.SinManaException;
import co.edu.puj.rpg.modelo.Arquero;
import co.edu.puj.rpg.modelo.Guerrero;
import co.edu.puj.rpg.modelo.Mago;
import co.edu.puj.rpg.modelo.Personaje;
import co.edu.puj.rpg.modelo.PerfilJugador;
import co.edu.puj.rpg.modelo.enums.TipoPersonaje;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Personaje personaje = null;
        PerfilJugador perfil = new PerfilJugador();
        Bestiario bestiario = new Bestiario();
        EstadisticasCombate estadisticas = new EstadisticasCombate();
        int opcion = -1;

        try {
            do {
                System.out.println("\n=== ARENA DE HEROES RPG ===");
                System.out.println("1. Crear Personaje");
                System.out.println("2. Asignar Habilidad Unica");
                System.out.println("3. Registrar Victoria en Bestiario");
                System.out.println("4. Ejecutar Ataque Especial");
                System.out.println("5. Consultar Bestiario y Logros");
                System.out.println("0. Salir");
                opcion = leerEntero(scanner, "Seleccione una opcion: ");

                switch (opcion) {
                    case 1:
                        Personaje nuevo = crearPersonaje(scanner);
                        if (nuevo != null) {
                            personaje = nuevo;
                            System.out.println("\nPersonaje creado:\n" + personaje);
                        }
                        break;
                    case 2:
                        if (personaje == null) {
                            System.out.println("Primero crea un personaje (opcion 1).");
                            break;
                        }
                        asignarHabilidad(scanner, personaje);
                        break;
                    case 3:
                        registrarVictoria(scanner, bestiario, perfil);
                        break;
                    case 4:
                        if (personaje == null) {
                            System.out.println("Primero crea un personaje (opcion 1).");
                            break;
                        }
                        ejecutarAtaqueEspecial(scanner, personaje, estadisticas);
                        break;
                    case 5:
                        consultar(bestiario, perfil, estadisticas);
                        break;
                    case 0:
                        System.out.println("Cerrando la Arena de Heroes...");
                        break;
                    default:
                        System.out.println("Opcion invalida.");
                }
            } while (opcion != 0);
        } catch (NoSuchElementException e) {
            System.out.println("\n(Se acabo la entrada de datos; fin del programa.)");
        }
    }

    // Opción 1: nombre + TipoPersonaje (valores del enum)
    private static Personaje crearPersonaje(Scanner scanner) {
        System.out.print("Nombre del personaje: ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacio.");
            return null;
        }
        TipoPersonaje[] tipos = TipoPersonaje.values();
        System.out.println("Tipos disponibles:");
        for (int i = 0; i < tipos.length; i++) {
            System.out.printf("  %d. %s (ataque +%.1f, defensa +%.1f)%n", i + 1, tipos[i],
                    tipos[i].getBonificadorAtaqueBase(), tipos[i].getBonificadorDefensaBase());
        }
        int n = leerEntero(scanner, "Elige el tipo: ");
        if (n < 1 || n > tipos.length) {
            System.out.println("Tipo invalido.");
            return null;
        }
        switch (tipos[n - 1]) {
            case GUERRERO: return new Guerrero(nombre, 120, 15, 0, 0, 10);
            case MAGO:     return new Mago(nombre, 80, 25, 0, 0, 50);
            case ARQUERO:  return new Arquero(nombre, 90, 18, 0, 0);
            default:       return null;
        }
    }

    // Opción 2: habilidad única (HashSet)
    private static void asignarHabilidad(Scanner scanner, Personaje personaje) {
        System.out.print("Nombre de la habilidad: ");
        String habilidad = scanner.nextLine().trim();
        if (habilidad.isEmpty()) {
            System.out.println("La habilidad no puede estar vacia.");
            return;
        }
        try {
            if (personaje.aprenderHabilidad(habilidad)) {
                System.out.printf("%s aprendio '%s'.%n", personaje.getNombre(), habilidad);
            } else {
                System.out.printf("Advertencia: '%s' ya esta aprendida.%n", habilidad);
            }
        } catch (CapacidadExcedidaException e) {
            System.out.println("No se pudo aprender: " + e.getMessage());
        }
    }

    // Opción 3: bestiario (HashMap) + logro automático (TreeSet)
    private static void registrarVictoria(Scanner scanner, Bestiario bestiario, PerfilJugador perfil) {
        System.out.print("Nombre del enemigo derrotado: ");
        String enemigo = scanner.nextLine().trim();
        if (enemigo.isEmpty()) {
            System.out.println("El nombre no puede estar vacio.");
            return;
        }
        boolean primera = bestiario.registrarVictoria(enemigo);
        System.out.printf("Victoria registrada contra %s. Total de derrotas: %d%n",
                enemigo, bestiario.consultarDerrotas(enemigo));
        if (primera) {
            String logro = "Primera victoria contra " + enemigo;
            perfil.registrarLogro(logro);
            System.out.println("Logro desbloqueado: " + logro);
        }
    }

    // Opción 4: lanzamiento con manejo de SinManaException
    private static void ejecutarAtaqueEspecial(Scanner scanner, Personaje personaje, EstadisticasCombate estadisticas) {
        int costo = leerEntero(scanner, "Costo de mana del ataque: ");
        if (costo < 0) {
            System.out.println("El costo no puede ser negativo.");
            return;
        }
        try {
            personaje.lanzarHechizo(costo);
            estadisticas.acumular("Hechizos lanzados", 1);
            estadisticas.acumular("Mana gastado", costo);
        } catch (SinManaException e) {
            System.out.println("Ataque fallido: " + e.getMessage());
        }
    }

    // Opción 5: recorrido con entrySet + logros ordenados
    private static void consultar(Bestiario bestiario, PerfilJugador perfil, EstadisticasCombate estadisticas) {
        System.out.println("\n--- BESTIARIO ---");
        if (bestiario.estaVacio()) {
            System.out.println("  (sin registros)");
        } else {
            bestiario.mostrarReporte();
        }
        System.out.println("--- LOGROS (orden alfabetico) ---");
        if (perfil.listarLogrosOrdenados().isEmpty()) {
            System.out.println("  (sin logros)");
        }
        for (String logro : perfil.listarLogrosOrdenados()) {
            System.out.println("  * " + logro);
        }
        System.out.println("--- ESTADISTICAS (orden alfabetico) ---");
        if (estadisticas.estaVacio()) {
            System.out.println("  (sin datos)");
        } else {
            estadisticas.mostrarReporte();
        }
    }

    // Lectura numérica blindada: try-catch-finally con InputMismatchException
    private static int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            boolean lecturaCorrecta = false;
            try {
                System.out.print(mensaje);
                int valor = scanner.nextInt();
                lecturaCorrecta = true;
                return valor;
            } catch (InputMismatchException e) {
                scanner.nextLine();                                   // limpia el buffer
                System.err.println("Entrada invalida: debes ingresar un numero entero.");
            } finally {
                if (lecturaCorrecta) {
                    scanner.nextLine();                               // consume el salto de linea pendiente
                }
            }
        }
    }
}