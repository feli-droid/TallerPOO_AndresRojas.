package co.edu.puj.rpg.inventario;

import java.util.ArrayList;
import java.util.List;

public class Inventario<T> {
    private List<T> items = new ArrayList<>();

    public void agregar(T item) { items.add(item); }
    public T obtener(int index) { return items.get(index); }
    public T remover(int index) { return items.remove(index); }
    public List<T> getItems() { return items; }
    public boolean estaVacio() { return items.isEmpty(); }
    public int tamano() { return items.size(); }
}
