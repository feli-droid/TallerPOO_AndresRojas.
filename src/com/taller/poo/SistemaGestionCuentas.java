package com.taller.poo;

import java.util.Locale;
import java.util.Scanner;

public class SistemaGestionCuentas {

    // Búsqueda lineal por numeroCuenta; retorna null si no existe.
    private static CuentaBancaria buscarCuenta(CuentaBancaria[] cuentas, String numero) {
        for (int i = 0; i < cuentas.length; i++) {
            if (cuentas[i].getNumeroCuenta().equals(numero)) {
                return cuentas[i];
            }
        }
        return null;
    }

    // Lee un monto; si no es un número válido retorna -1 (que luego falla la validación > 0).
    private static double leerMonto(Scanner sc) {
        System.out.printf("Monto: ");
        try {
            return Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);

        CuentaBancaria[] cuentas = new CuentaBancaria[3];
        cuentas[0] = new CuentaBancaria("1001", "Carlos Pérez", 1000.0, 500.0);
        cuentas[1] = new CuentaBancaria("1002", "Ana Gómez");
        cuentas[2] = new CuentaBancaria("1003", "Luis Torres", 300.0, 200.0);

        int opcion = 0;
        do {
            System.out.printf("%n===== SISTEMA DE GESTIÓN DE CUENTAS =====%n");
            System.out.printf("1. Depositar%n2. Retirar%n3. Ver reporte consolidado%n4. Salir%n");
            System.out.printf("Seleccione una opción: ");
            try {
                opcion = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                opcion = 0;
            }

            switch (opcion) {
                case 1:
                case 2:
                    boolean esDeposito = (opcion == 1);
                    System.out.printf("Número de cuenta: ");
                    String numero = sc.nextLine().trim();
                    CuentaBancaria cuenta = buscarCuenta(cuentas, numero);
                    if (cuenta == null) {
                        System.out.printf("ERROR: la cuenta '%s' no existe.%n", numero);
                        break;
                    }
                    double monto = leerMonto(sc);
                    if (monto <= 0) {
                        System.out.printf("ERROR: el monto debe ser mayor a 0.%n");
                        break;
                    }
                    boolean exito = esDeposito ? cuenta.depositar(monto) : cuenta.retirar(monto);
                    if (exito) {
                        System.out.printf("Operación exitosa. Nuevo saldo de %s: %s%n",
                                cuenta.getNumeroCuenta(), ConversorMoneda.formatearMoneda(cuenta.getSaldo()));
                    } else {
                        System.out.printf("ERROR: fondos insuficientes (saldo + crédito).%n");
                    }
                    break;

                case 3:
                    StringBuilder sb = new StringBuilder();
                    double total = 0.0;
                    for (int i = 0; i < cuentas.length; i++) {
                        CuentaBancaria c = cuentas[i];
                        sb.append(String.format("Cuenta: %s | Titular: %s | Saldo: %s | Crédito: %s%n",
                                c.getNumeroCuenta(), c.getTitular(),
                                ConversorMoneda.formatearMoneda(c.getSaldo()),
                                ConversorMoneda.formatearMoneda(c.getLimiteCredito())));
                        total += c.getSaldo();
                    }
                    System.out.printf("%n=== REPORTE CONSOLIDADO ===%n%s", sb);
                    System.out.printf("Cuentas registradas: %d%n", cuentas.length);
                    System.out.printf("Saldo total acumulado: %.2f%n", total);
                    break;

                case 4:
                    System.out.printf("Saliendo del sistema...%n");
                    break;

                default:
                    System.out.printf("Opción inválida. Elija un número del 1 al 4.%n");
            }
        } while (opcion != 4);

        sc.close();
    }
}