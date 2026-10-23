package co.edu.puj.rpg.modelo;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class PerfilJugador {
    private final Set<String> logros = new TreeSet<>();

    public boolean registrarLogro(String logro) {
        return logros.add(logro);
    }

    public Set<String> listarLogrosOrdenados() {
        return Collections.unmodifiableSet(logros);
    }
}