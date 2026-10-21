package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.inventario.Inventario;
import co.edu.puj.rpg.inventario.Pocion;
import co.edu.puj.rpg.modelo.Guerrero;
import co.edu.puj.rpg.modelo.Mago;
import co.edu.puj.rpg.modelo.Personaje;
import co.edu.puj.rpg.modelo.enemigo.Enemigo;
import co.edu.puj.rpg.tablero.Tablero;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static final double COSTO_POCION_GRANDE = 40.0;
    private static final Scanner sc = new Scanner(System.in);
    private static boolean huyo = false;
    private static boolean cofreAlcanzado = false;

    public static void main(String[] args) {
        // ---------- 1. Inicialización ----------
        Tablero tablero = new Tablero(5, 5, 2, 2);
        List<Personaje> heroes = new ArrayList<>();
        heroes.add(new Guerrero("Thorin", 120, 15, 0, 0, 10));
        heroes.add(new Mago("Gandalf", 80, 25, 1, 1, 50));
        Personaje principal = heroes.get(0);

        List<Enemigo> enemigos = new ArrayList<>();
        enemigos.add(new Enemigo("Orco Bruto", 60, 10, 0, 4, 60, 30.0));
        enemigos.add(new Enemigo("Trasgo", 40, 8, 4, 0, 40, 20.0));
        enemigos.add(new Enemigo("Ogro", 90, 14, 4, 4, 80, 50.0));

        Inventario<Pocion> inventario = new Inventario<>();
        inventario.agregar(new Pocion("Pocion Pequena", 30));
        inventario.agregar(new Pocion("Pocion Pequena", 30));

        System.out.println("=== ARENA RPG: BATALLA DE ESCUADRONES ===");
        int ronda = 0;

        try {
            // ---------- 2. Ciclo principal ----------
            do {
                ronda++;
                System.out.printf("%n========== RONDA %d ==========%n", ronda);
                tablero.actualizar(heroes, enemigos);
                tablero.mostrar();
                System.out.println("Leyenda: G Guerrero, M Mago, E Enemigo, C Cofre, . vacio");
                mostrarEstado(heroes, enemigos);

                // ---------- 4. Turno del jugador ----------
                for (Personaje heroe : heroes) {
                    if (!heroe.estaVivo()) continue;
                    if (vivos(enemigos).isEmpty() || huyo || cofreAlcanzado) break;
                    turnoHeroe(heroe, principal, tablero, heroes, enemigos, inventario);
                    heroe.finalizarTurno();
                }

                // ---------- 5. Turno enemigo ----------
                if (!huyo && !cofreAlcanzado) {
                    turnoEnemigos(heroes, enemigos);
                }

                // ---------- 6. Limpieza de bajas ----------
                heroes.removeIf(p -> !p.estaVivo());
                enemigos.removeIf(e -> !e.estaVivo());

            } while (!heroes.isEmpty() && sumaVida(heroes) > 0
                    && !enemigos.isEmpty() && !huyo && !cofreAlcanzado);
        } catch (NoSuchElementException e) {
            System.out.println("\n(Se acabo la entrada de datos; fin del programa.)");
            return;
        }

        // ---------- Resultado ----------
        System.out.println("\n=== FIN DE LA BATALLA ===");
        if (huyo) {
            System.out.println("Tu escuadron escapo de la arena.");
        } else if (cofreAlcanzado) {
            System.out.println("VICTORIA: " + principal.getNombre() + " alcanzo el cofre del tesoro!");
        } else if (enemigos.isEmpty()) {
            System.out.println("VICTORIA: todos los enemigos fueron eliminados!");
        } else {
            System.out.println("DERROTA: tu escuadron ha caido...");
        }
        tablero.actualizar(heroes, enemigos);
        tablero.mostrar();
    }

    // =====================================================================
    //  TURNO DE CADA HÉROE
    // =====================================================================
    private static void turnoHeroe(Personaje heroe, Personaje principal, Tablero tablero,
                                   List<Personaje> heroes, List<Enemigo> enemigos,
                                   Inventario<Pocion> inventario) {
        boolean turnoConsumido = false;
        do {
            System.out.printf("%n>> Turno de %s%n   %s%n", heroe.getNombre(), heroe.describir());
            System.out.println("1. Atacar");
            System.out.println("2. Usar Inventario");
            System.out.println("3. Mover en Tablero");
            System.out.println("4. Tienda");
            System.out.println("5. Huir");
            System.out.println("6. Ver Tablero");
            int opcion = leerEntero("Elige una opcion: ");

            switch (opcion) {
                case 1:
                    turnoConsumido = menuAtaque(heroe, enemigos);
                    break;
                case 2:
                    turnoConsumido = usarInventario(heroe, inventario);
                    break;
                case 3:
                    turnoConsumido = mover(heroe, tablero, heroes, enemigos);
                    if (turnoConsumido && heroe == principal
                            && tablero.esCofre(heroe.getPosicionX(), heroe.getPosicionY())) {
                        cofreAlcanzado = true;
                    }
                    break;
                case 4:
                    tienda(heroe, inventario);         
                    break;
                case 5:
                    huyo = true;
                    turnoConsumido = true;
                    break;
                case 6:
                    tablero.actualizar(heroes, enemigos);  
                    System.out.println();
                    tablero.mostrar();                    
                    break;
                default:
                    System.out.println("Opcion invalida. Elige entre 1 y 6.");
            }
        } while (!turnoConsumido);
    }

    private static boolean menuAtaque(Personaje heroe, List<Enemigo> enemigos) {
        List<Enemigo> objetivos = vivos(enemigos);
        if (objetivos.isEmpty()) return false;

        System.out.println("Objetivos:");
        for (int i = 0; i < objetivos.size(); i++) {
            Enemigo e = objetivos.get(i);
            System.out.printf("  %d. %s (vida %d/%d)%n", i + 1, e.getNombre(), e.getVida(), e.getVidaMaxima());
        }
        int t = leerEntero("Elige objetivo (0 = cancelar): ");
        if (t == 0) return false;
        if (t < 1 || t > objetivos.size()) {
            System.out.println("Objetivo invalido.");
            return false;
        }
        Enemigo objetivo = objetivos.get(t - 1);

        System.out.println("1. Ataque especial   2. Ataque normal   3. Ataque en modo furia");
        int tipo = leerEntero("Tipo de ataque: ");
        switch (tipo) {
            case 1: heroe.realizarAtaqueEspecial(objetivo); break;   
            case 2: heroe.atacar(objetivo); break;                   
            case 3: heroe.atacar(objetivo, true); break;             
            default:
                System.out.println("Tipo de ataque invalido.");
                return false;
        }

        if (!objetivo.estaVivo()) {
            heroe.setOro(heroe.getOro() + objetivo.getOroRecompensa());
            System.out.printf("%s obtiene %.2f de oro y %d XP.%n", heroe.getNombre(),
                    objetivo.getOroRecompensa(), objetivo.getExperienciaRecompensa());
            if (heroe.ganarExperiencia(objetivo.getExperienciaRecompensa())) {
                System.out.printf("%s SUBE A NIVEL %d!%n", heroe.getNombre(), heroe.getNivel());
            }
        }
        return true;
    }

    private static boolean usarInventario(Personaje heroe, Inventario<Pocion> inventario) {
        if (inventario.estaVacio()) {
            System.out.println("El inventario esta vacio.");
            return false;
        }
        if (heroe.getVida() == heroe.getVidaMaxima()) {
            System.out.println(heroe.getNombre() + " ya tiene la vida completa.");
            return false;
        }
        System.out.println("Inventario:");
        for (int i = 0; i < inventario.tamano(); i++) {
            System.out.printf("  %d. %s%n", i + 1, inventario.obtener(i));  
        }
        int o = leerEntero("Elige pocion (0 = cancelar): ");
        if (o == 0) return false;
        if (o < 1 || o > inventario.tamano()) {
            System.out.println("Seleccion invalida.");
            return false;
        }
        Pocion p = inventario.remover(o - 1);
        heroe.curar(p.getCuracion());
        return true;
    }

    private static boolean mover(Personaje heroe, Tablero tablero,
                                 List<Personaje> heroes, List<Enemigo> enemigos) {
        System.out.print("Direccion (w=arriba, s=abajo, a=izquierda, d=derecha, x=cancelar): ");
        String d = sc.next().toLowerCase();
        int x = heroe.getPosicionX();
        int y = heroe.getPosicionY();
        switch (d) {
            case "w": x--; break;
            case "s": x++; break;
            case "a": y--; break;
            case "d": y++; break;
            case "x": return false;
            default:
                System.out.println("Direccion invalida.");
                return false;
        }
        if (!tablero.esValida(x, y)) {                
            System.out.println("Movimiento fuera del tablero.");
            return false;
        }
        tablero.actualizar(heroes, enemigos);
        if (!tablero.estaLibre(x, y)) {
            System.out.println("Esa casilla esta ocupada.");
            return false;
        }
        heroe.setPosicion(x, y);
        System.out.printf("%s se mueve a (%d, %d).%n", heroe.getNombre(), x, y);
        return true;
    }

    private static void tienda(Personaje heroe, Inventario<Pocion> inventario) {
        System.out.printf("%n== TIENDA == (Oro de %s: %.2f)%n", heroe.getNombre(), heroe.getOro());
        System.out.printf("1. Pocion Grande (+60 HP) - %.2f oro%n", COSTO_POCION_GRANDE);
        System.out.println("2. Salir");
        int op = leerEntero("Opcion: ");
        if (op == 1) {
            double antes = heroe.getOro();
            heroe.setOro(antes - COSTO_POCION_GRANDE);        
            if (heroe.getOro() == antes) {
                System.out.println("Compra cancelada.");
            } else {
                inventario.agregar(new Pocion("Pocion Grande", 60));
                System.out.printf("Compraste una Pocion Grande. Oro restante: %.2f%n", heroe.getOro());
            }
        } else if (op != 2) {
            System.out.println("Opcion invalida.");
        }
    }

    // =====================================================================
    //  TURNO ENEMIGO (IA simple)
    // =====================================================================
    private static void turnoEnemigos(List<Personaje> heroes, List<Enemigo> enemigos) {
        System.out.println("\n--- Turno enemigo ---");
        for (Enemigo enemigo : vivos(enemigos)) {
            List<Personaje> objetivos = vivos(heroes);
            if (objetivos.isEmpty()) break;
            Personaje victima = objetivos.get((int) (Math.random() * objetivos.size()));
            enemigo.realizarAtaqueEspecial(victima);
        }
    }

    // =====================================================================
    //  UTILIDADES
    // =====================================================================
    private static <T extends Personaje> List<T> vivos(List<T> lista) {
        List<T> resultado = new ArrayList<>();
        for (T p : lista) {
            if (p.estaVivo()) resultado.add(p);
        }
        return resultado;
    }

    private static int sumaVida(List<Personaje> lista) {
        int suma = 0;
        for (Personaje p : lista) suma += p.getVida();
        return suma;
    }

    private static void mostrarEstado(List<Personaje> heroes, List<Enemigo> enemigos) {
        System.out.println("\n-- Heroes --");
        for (Personaje h : heroes) System.out.println("  " + h.describir());
        System.out.println("-- Enemigos --");
        for (Enemigo e : enemigos) {
            System.out.printf("  %s | Vida %d/%d | Ataque %d%n",
                    e.getNombre(), e.getVida(), e.getVidaMaxima(), e.getAtaqueBase());
        }
    }

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Entrada invalida. " + mensaje);
        }
        return sc.nextInt();
    }
}
