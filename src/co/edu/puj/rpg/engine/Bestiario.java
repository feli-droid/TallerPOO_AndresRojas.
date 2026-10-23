package co.edu.puj.rpg.engine;

import java.util.HashMap;
import java.util.Map;

public class Bestiario {
    private final Map<String, Integer> conteoDerrotas = new HashMap<>();


    public boolean registrarVictoria(String enemigo) {
        boolean esPrimera = !conteoDerrotas.containsKey(enemigo);
        int nuevoTotal = conteoDerrotas.getOrDefault(enemigo, 0) + 1;
        conteoDerrotas.put(enemigo, nuevoTotal);
        return esPrimera;
    }

    public int consultarDerrotas(String enemigo) {
        if (conteoDerrotas.containsKey(enemigo)) {
            return conteoDerrotas.get(enemigo);
        }
        return 0;
    }

    public boolean estaVacio() {
        return conteoDerrotas.isEmpty();
    }

    public void mostrarReporte() {
        for (Map.Entry<String, Integer> entry : conteoDerrotas.entrySet()) {
            System.out.printf("  %-20s %3d derrota(s)%n", entry.getKey(), entry.getValue());
        }
    }
}