package com.taller.poo;

import java.util.Locale;

public class Principal {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US); // para que los decimales salgan con punto

        // ---- Vehículos ----
        Vehiculo v1 = new Vehiculo("Toyota", "Corolla", 15000.50, 180, true);
        Vehiculo v2 = new Vehiculo();
        Vehiculo v3 = new Vehiculo("Mazda", "3", 32000.0, 200, false);

        // ---- Estudiantes ----
        Estudiante e1 = new Estudiante("E001", "Laura Martínez", 4.35, true);
        Estudiante e2 = new Estudiante("E002", "Juan Rojas", 3.80, true);
        Estudiante e3 = new Estudiante("E003", "Sofía Díaz", 2.95, false);

        // ---- Cuentas (los 3 constructores) ----
        CuentaBancaria c1 = new CuentaBancaria("1001", "Carlos Pérez", 1000.0, 500.0);
        CuentaBancaria c2 = new CuentaBancaria("1002", "Ana Gómez");
        CuentaBancaria c3 = new CuentaBancaria();

        // ---- Modificación de estado ----
        c1.depositar(250.75);
        c2.depositar(100.0);
        c2.retirar(50.0);
        boolean ok = c3.retirar(50.0); // saldo 0 y crédito 0 -> debe fallar

        // ---- Métodos estáticos (sin instanciar) ----
        double euros = ConversorMoneda.convertirDolaresAEuros(c1.getSaldo(), 0.92);

        // ---- Reportes (solo printf) ----
        System.out.printf("=== REPORTE DE VEHÍCULOS ===%n");
        Vehiculo[] vehiculos = {v1, v2, v3};
        for (int i = 0; i < vehiculos.length; i++) {
            Vehiculo v = vehiculos[i];
            System.out.printf("[%d] Marca: %s | Modelo: %s | KM: %.2f | VelMax: %d km/h | Encendido: %b%n",
                    i + 1, v.getMarca(), v.getModelo(), v.getKilometraje(),
                    v.getVelocidadMaxima(), v.isEncendido());
        }

        System.out.printf("%n=== REPORTE DE ESTUDIANTES ===%n");
        System.out.printf("%s%n", e1.obtenerReporteAcademicos());
        System.out.printf("%s%n", e2.obtenerReporteAcademicos());
        System.out.printf("%s%n", e3.obtenerReporteAcademicos());

        System.out.printf("%n=== REPORTE DE CUENTAS BANCARIAS ===%n");
        CuentaBancaria[] cuentas = {c1, c2, c3};
        for (CuentaBancaria c : cuentas) {
            System.out.printf("Cuenta: %s | Titular: %s | Saldo Final: %s | Crédito: %s%n",
                    c.getNumeroCuenta(), c.getTitular(),
                    ConversorMoneda.formatearMoneda(c.getSaldo()),
                    ConversorMoneda.formatearMoneda(c.getLimiteCredito()));
        }
        System.out.printf("Retiro de $50.00 en cuenta %s exitoso: %b%n", c3.getNumeroCuenta(), ok);
        System.out.printf("Saldo de la cuenta 1001 en euros (tasa 0.92): %.2f EUR%n", euros);
    }
}
