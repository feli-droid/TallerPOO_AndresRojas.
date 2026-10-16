package com.taller.poo.Análisis_Depuración_Código;

/*
 * CASO DE ESTUDIO 2: Invocación de método de instancia desde contexto estático
 *
 * CÓDIGO ORIGINAL CON ERROR (dentro de main):
 *     encenderMotor();   // encenderMotor() NO es static
 *
 * PREGUNTA 1. ¿Mensaje de error del compilador?
 *   R: "non-static method encenderMotor() cannot be referenced from a static context"
 *
 * PREGUNTA 2. ¿Por qué un método static no puede invocar uno de instancia?
 *   R: Un método static pertenece a la CLASE y puede ejecutarse sin que exista
 *      ningún objeto. Un método de instancia pertenece a un OBJETO y usa su estado
 *      (this). Dentro de main no existe "this", así que el compilador no sabe
 *      sobre qué objeto ejecutar encenderMotor().
 *
 * PREGUNTA 3. Dos alternativas de corrección válidas:
 *   A) Crear un objeto y llamar al método sobre él:
 *          GestorVehiculos gestor = new GestorVehiculos();
 *          gestor.encenderMotor();
 *   B) Declarar el método como static y llamarlo directamente:
 *          public static void encenderMotorEstatico() { ... }
 *
 * ESTADO: CORREGIDO (ambas alternativas se muestran en main).
 */
public class GestorVehiculos {

    // Método de instancia (Alternativa A)
    public void encenderMotor() {
        System.out.println("El motor del vehículo ha sido encendido.");
    }

    // Método estático (Alternativa B)
    public static void encenderMotorEstatico() {
        System.out.println("El motor del vehículo ha sido encendido (método estático).");
    }

    public static void main(String[] args) {
        // Alternativa A: instanciar y llamar
        GestorVehiculos gestor = new GestorVehiculos();
        gestor.encenderMotor();

        // Alternativa B: llamada directa al método static
        encenderMotorEstatico();
    }
}
