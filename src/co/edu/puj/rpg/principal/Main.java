package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.modelo.Personaje;
import co.edu.puj.rpg.modelo.enemigo.Enemigo;

import java.util.Scanner;

public class Main {
    private static final double COSTO_POCION_GRANDE = 40.0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Personaje heroe = new Personaje("Aragorn", 100, 15, 50.0);
        Enemigo enemigo = new Enemigo("Orco Bruto", 80, 12, 60, 30.0);
        StringBuilder historial = new StringBuilder();
        boolean huyo = false;
        int ronda = 0;

        System.out.println("=== ARENA DE HEROES ===");
        System.out.printf("%s (Nv. %d) se enfrenta a %s%n",
                heroe.getNombre(), heroe.getNivel(), enemigo.getNombre());

        do {
            ronda++;
            System.out.printf("%n--- Ronda %d ---%n", ronda);
            System.out.println("1. Atacar");
            System.out.println("2. Usar Pocion");
            System.out.println("3. Ver Estado");
            System.out.println("4. Huir");
            System.out.println("5. Tienda");
            System.out.print("Elige una opcion: ");

            if (!sc.hasNextInt()) {
                sc.next();
                System.out.println("Entrada invalida. Ingresa un numero.");
                ronda--;
                continue;
            }
            int opcion = sc.nextInt();
            boolean consumeTurno = false;

            switch (opcion) {
                case 1:
                    heroe.atacar(enemigo);
                    historial.append(heroe.getUltimoEvento()).append("\n");
                    consumeTurno = true;
                    break;
                case 2:
                    int cura = heroe.consumirPocion();
                    if (cura == 0) {
                        System.out.println("No tienes pociones.");
                    } else {
                        heroe.usarPocion(cura);
                        historial.append(heroe.getUltimoEvento()).append("\n");
                        consumeTurno = true;
                    }
                    break;
                case 3:
                    mostrarEstado(heroe, enemigo);
                    break;
                case 4:
                    huyo = true;
                    historial.append(heroe.getNombre()).append(" huye del combate.\n");
                    break;
                case 5:
                    tienda(sc, heroe, historial);
                    break;
                default:
                    System.out.println("Opcion invalida. Elige entre 1 y 5.");
            }

            // Contraataque del enemigo
            if (consumeTurno && enemigo.isEstaVivo()) {
                enemigo.atacar(heroe);
                historial.append(enemigo.getNombre()).append(" contraataca. ")
                        .append(heroe.getUltimoEvento()).append("\n");
            }

            if (consumeTurno) {
                System.out.println("\n[Historial de batalla]");
                System.out.print(historial);
                System.out.printf("%s: %d/%d HP | %s: %d/%d HP%n",
                        heroe.getNombre(), heroe.getPuntosVida(), heroe.getVidaMaxima(),
                        enemigo.getNombre(), enemigo.getPuntosVida(), enemigo.getVidaMaxima());
            }
        } while (heroe.isEstaVivo() && enemigo.isEstaVivo() && !huyo);

        // Resultado final
        System.out.println("\n=== FIN DEL COMBATE ===");
        if (huyo) {
            System.out.println("Escapaste de la arena.");
        } else if (!heroe.isEstaVivo()) {
            System.out.println("Has sido derrotado...");
        } else {
            System.out.printf("Victoria! Derrotaste a %s.%n", enemigo.getNombre());
            heroe.setOro(heroe.getOro() + enemigo.getOroRecompensa());
            boolean subio = heroe.ganarExperiencia(enemigo.getExperienciaRecompensa());
            System.out.printf("Ganas %d XP y %.2f de oro.%n",
                    enemigo.getExperienciaRecompensa(), enemigo.getOroRecompensa());
            if (subio) {
                System.out.printf("SUBES A NIVEL %d! (Vida max: %d, Ataque: %d)%n",
                        heroe.getNivel(), heroe.getVidaMaxima(), heroe.getPuntosAtaque());
            }
        }
        mostrarEstado(heroe, enemigo);
        sc.close();
    }

    private static void mostrarEstado(Personaje h, Enemigo e) {
        System.out.println("\n----- ESTADO -----");
        System.out.printf("Heroe   : %s | Nivel: %d | Vida: %d/%d | Ataque: %d | Oro: %.2f | Vivo: %b%n",
                h.getNombre(), h.getNivel(), h.getPuntosVida(), h.getVidaMaxima(),
                h.getPuntosAtaque(), h.getOro(), h.isEstaVivo());
        System.out.printf("Pociones: normales %d | grandes %d | XP: %d%n",
                h.getPociones(), h.getPocionesGrandes(), h.getExperiencia());
        System.out.printf("Enemigo : %s | Vida: %d/%d | Vivo: %b%n",
                e.getNombre(), e.getPuntosVida(), e.getVidaMaxima(), e.isEstaVivo());
    }

    private static void tienda(Scanner sc, Personaje heroe, StringBuilder historial) {
        System.out.printf("%n== TIENDA == (Oro: %.2f)%n", heroe.getOro());
        System.out.printf("1. Pocion Grande de Salud (+60 HP) - %.2f oro%n", COSTO_POCION_GRANDE);
        System.out.println("2. Salir");
        System.out.print("Opcion: ");
        if (!sc.hasNextInt()) {
            sc.next();
            System.out.println("Entrada invalida.");
            return;
        }
        int op = sc.nextInt();
        if (op == 1) {
            double saldoAntes = heroe.getOro();
            heroe.setOro(saldoAntes - COSTO_POCION_GRANDE);
            if (heroe.getOro() == saldoAntes) {
                System.out.println("Compra cancelada.");
            } else {
                heroe.agregarPocionGrande();
                System.out.printf("Compraste una Pocion Grande. Oro restante: %.2f%n", heroe.getOro());
                historial.append("Compra: Pocion Grande de Salud.\n");
            }
        } else if (op != 2) {
            System.out.println("Opcion invalida.");
        }
    }
}
