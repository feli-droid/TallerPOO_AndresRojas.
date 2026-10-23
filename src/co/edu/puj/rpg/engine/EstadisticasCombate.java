package co.edu.puj.rpg.engine;

import java.util.Map;
import java.util.TreeMap;

public class EstadisticasCombate {
    private final Map<String, Double> estadisticas = new TreeMap<>();

    public void acumular(String clave, double valor) {
        estadisticas.put(clave, estadisticas.getOrDefault(clave, 0.0) + valor);
    }

    public boolean estaVacio() {
        return estadisticas.isEmpty();
    }

    public void mostrarReporte() {
        for (Map.Entry<String, Double> entry : estadisticas.entrySet()) {
            System.out.printf("  %-20s %8.2f%n", entry.getKey(), entry.getValue());
        }
    }
}
