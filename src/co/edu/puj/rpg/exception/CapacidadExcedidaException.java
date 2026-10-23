package co.edu.puj.rpg.exception;

/** Verificada: se intenta agregar un elemento a un contenedor que ya está lleno. */
public class CapacidadExcedidaException extends JuegoException {
    public CapacidadExcedidaException(String mensaje) {
        super(mensaje);
    }

    public CapacidadExcedidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
