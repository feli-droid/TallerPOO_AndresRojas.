package com.taller.poo.Análisis_Depuración_Código;

/*
 * CASO DE ESTUDIO 3: Firma Incorrecta en la Declaración de un Constructor
 *
 * PREGUNTAS ANALÍTICAS:
 *
 * 1. ¿Por qué la inclusión de la palabra clave void altera la naturaleza del método y
 *    hace que Java deje de reconocerlo como un constructor?
 *    R: Un constructor tiene el mismo nombre de la clase y NO declara tipo de retorno
 *       (ni siquiera void). Al escribir "void", Java lo interpreta como un método
 *       normal que casualmente se llama igual que la clase. Compila, pero no es un
 *       constructor y no se ejecuta al hacer new.
 *
 * 2. Si desde otra clase se intenta ejecutar
 *    CuentaBancaria cuenta = new CuentaBancaria("A-101", 500.0);
 *    ¿por qué la compilación reportará que no se encuentra un constructor adecuado?
 *    R: Como no existe ningún constructor real, Java solo genera el constructor por
 *       defecto (sin parámetros). Al buscar uno con (String, double) no lo encuentra y
 *       reporta que el constructor no puede aplicarse a los tipos dados.
 *
 * 3. Escriba la firma y declaración correcta del constructor omitiendo el tipo de retorno.
 *    R: Ver el constructor de abajo (versión corregida).
 *
 * ESTADO: CORREGIDO.
 *
 * NOTA: esta clase está en el paquete com.taller.poo.depuracion para poder llamarse
 * CuentaBancaria (igual que en el enunciado) sin chocar con la del taller.
 */
public class CuentaBancaria {
    private String numeroCuenta;
    private double saldo;

    // DECLARACIÓN ERRÓNEA ORIGINAL (con void, NO es un constructor):
    // public void CuentaBancaria(String numeroCuenta, double saldoInicial) {
    //     this.numeroCuenta = numeroCuenta;
    //     this.saldo = saldoInicial;
    // }

    // CORRECCIÓN: sin tipo de retorno
    public CuentaBancaria(String numeroCuenta, double saldoInicial) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldoInicial;
    }

    public static void main(String[] args) {
        CuentaBancaria cuenta = new CuentaBancaria("A-101", 500.0);
        System.out.printf("Cuenta: %s | Saldo: %.2f%n", cuenta.numeroCuenta, cuenta.saldo);
    }
}
