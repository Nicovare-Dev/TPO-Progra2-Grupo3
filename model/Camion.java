package model;

import java.util.Deque;
import java.util.ArrayDeque;

public class Camion <T> {
    private String patente;
    private Deque<Paquete<T>> pila;
    private static final int capacidad = 10;

    //Constructor
    public Camion(String patente) {
        this.patente = patente;
        this.pila = new ArrayDeque<>();
    }

    //Getter para la patente del camion
    public String getPatente() {
        return patente;
    }

    // Setter para la patente del camión
    public void setPatente(String patente) {
        this.patente = patente;
    }

    // Carga un paquete al camión si hay espacio disponible.
    // Complejidad temporal: O(1) — push sobre ArrayDeque es constante amortizado.
    // Complejidad espacial: O(1) — solo se agrega un nodo.
    public void cargarPaquete(Paquete<T> paquete) {
        if (estaLleno()) {
            throw new IllegalStateException("El camión está lleno.");
        }
        pila.push(paquete);
    }

    // Descarga (remueve) el último paquete cargado en el camión.
    // Complejidad temporal: O(1) — pop sobre ArrayDeque es constante amortizado.
    // Complejidad espacial: O(1).
    public Paquete<T> descargarUltimoPaquete() {
        if (estaVacio()) {
            throw new IllegalStateException("El camión está vacío.");
        }
        return pila.pop();
    }

    // Permite ver el último paquete cargado sin removerlo.
    // Complejidad temporal: O(1) — peek no recorre la estructura.
    // Complejidad espacial: O(1).
    public Paquete<T> verUltimoPaquete() {
        if (estaVacio()) {
            throw new IllegalStateException("El camión está vacío.");
        }
        return pila.peek();
    }

    // Descarga todos los paquetes del camión.
    // Complejidad temporal: O(n) — recorre cada elemento una vez.
    // Complejidad espacial: O(1) — no usa memoria extra.
    public void descargarTodoElCamion() {
        while (!pila.isEmpty()) {
            pila.pop();
        }
    }

    // Indica si el camión está lleno (alcanzó su capacidad máxima)
    // Complejidad temporal: O(1).
    public boolean estaLleno() {
        return pila.size() == capacidad;
    }

    // Indica si el camión está vacío
    // Complejidad temporal: O(1).
    public boolean estaVacio() {
        return pila.isEmpty();
    }

    // Devuelve la cantidad de paquetes cargados en el camión
    // Complejidad temporal: O(1).
    public int cantidadPaquetes() {
        return pila.size();
    }

    @Override
    public String toString() {
    return "Camion [patente=" + patente + 
           ", paquetes=" + pila.size() + "]";
    }
}
