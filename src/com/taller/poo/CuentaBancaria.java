package com.taller.poo;

public class CuentaBancaria {
    private String numeroCuenta;
    private String titular;
    private double saldo;
    private Double limiteCredito;

    /*
     * PREGUNTAS DE ANÁLISIS:
     * 1. ¿Qué diferencia ocurre a nivel de memoria y asignación por defecto al
     *    declarar 'saldo' como double primitivo frente a 'limiteCredito' como Wrapper Double?
     *    R: 'saldo' (double) guarda el valor directamente (8 bytes) y por defecto
     *    vale 0.0. 'limiteCredito' (Double) es una referencia a un objeto en el heap
     *    (más memoria: cabecera + valor) y por defecto vale null, es decir, no apunta
     *    a ningún objeto hasta que se le asigne uno.
     *
     * 2. ¿Por qué una operación aritmética sobre un atributo no inicializado de
     *    tipo Double provoca una NullPointerException, mientras que sobre un double
     *    primitivo opera con 0.0?
     *    R: Para operar, Java hace unboxing automático (limiteCredito.doubleValue()).
     *    Si la referencia es null no hay objeto del cual extraer el valor, y se lanza
     *    NullPointerException. El primitivo siempre contiene un valor real (0.0),
     *    por eso opera sin problema.
     */

    // Constructor por defecto
    public CuentaBancaria() {
        this.numeroCuenta = "000000";
        this.titular = "Sin Nombre";
        this.saldo = 0.0;
        this.limiteCredito = 0.0;
    }

    // Constructor de 2 parámetros
    public CuentaBancaria(String numeroCuenta, String titular) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = 0.0;
        this.limiteCredito = 100.0;
    }

    // Constructor completo
    public CuentaBancaria(String numeroCuenta, String titular, double saldo, Double limiteCredito) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldo;
        this.limiteCredito = limiteCredito;
    }

    public boolean depositar(double monto) {
        if (monto > 0) {
            saldo += monto;
            return true;
        } else {
            return false;
        }
    }

    public boolean retirar(double monto) {
        if (monto <= 0) {
            return false;
        }
        // Si limiteCredito es null se considera 0 (evita NullPointerException).
        double credito = (limiteCredito != null) ? limiteCredito : 0.0;
        if (saldo + credito >= monto) {
            saldo -= monto;
            return true;
        } else {
            return false;
        }
    }

    public String getNumeroCuenta() { return numeroCuenta; }
    public String getTitular() { return titular; }
    public double getSaldo() { return saldo; }
    public Double getLimiteCredito() { return limiteCredito; }
}
