package co.edu.puj.rpg.principal;

import java.util.Scanner;


public final class Consola {
    private static final Scanner sc = new Scanner(System.in);

    private Consola() { }

    public static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    public static int leerEntero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Ingresa un numero entero.");
            }
        }
    }
}
