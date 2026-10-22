package co.edu.puj.rpg.tablero;

import co.edu.puj.rpg.modelo.Personaje;

import java.util.List;

public class Tablero {
    private final char[][] matriz;
    private final int cofreX;
    private final int cofreY;

    public Tablero(int filas, int columnas, int cofreX, int cofreY) {
        this.matriz = new char[filas][columnas];
        this.cofreX = cofreX;
        this.cofreY = cofreY;
    }


    public void actualizar(List<? extends Personaje> heroes, List<? extends Personaje> enemigos) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                matriz[i][j] = '.';
            }
        }
        matriz[cofreX][cofreY] = 'C';
        colocar(enemigos);
        colocar(heroes);
    }

    private void colocar(List<? extends Personaje> lista) {
        for (Personaje p : lista) {
            if (p.estaVivo() && esValida(p.getPosicionX(), p.getPosicionY())) {
                matriz[p.getPosicionX()][p.getPosicionY()] = p.getSimbolo();
            }
        }
    }

    public boolean esValida(int x, int y) {
        return x >= 0 && x < matriz.length && y >= 0 && y < matriz[0].length;
    }

    public boolean estaLibre(int x, int y) {
        return matriz[x][y] == '.' || matriz[x][y] == 'C';
    }

    public boolean esCofre(int x, int y) {
        return x == cofreX && y == cofreY;
    }

    public void mostrar() {
        mostrarTablero(matriz);
    }

    public void mostrarTablero(char[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                System.out.print("[" + m[i][j] + "]");
            }
            System.out.println();
        }
    }
}
