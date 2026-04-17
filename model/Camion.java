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
    // Devuelve true si se cargó correctamente, false si el camión está lleno.
    public boolean cargarPaquete(Paquete<T> paquete) {
        if (estaLleno()) {
            return false;
        }
        pila.push(paquete);
        return true;
    }

    // Descarga (remueve) el último paquete cargado en el camión.
    // Si el camión está vacío, devuelve null.
    public Paquete<T> descargarUltimoPaquete() {
        if (estaVacio()) {
            return null;
        }
        return pila.pop();
    }

    // Descarga todos los paquetes del camión.
    public void descargarTodoElCamion() {
        while (!pila.isEmpty()) {
            pila.pop();
        }
    }

    // Permite ver el último paquete cargado sin removerlo.
    // Devuelve null si el camión está vacío.
    public Paquete<T> verUltimoPaquete() {
        if (estaVacio()) {
            return null;
        }
        return pila.peek();
    }

    // Indica si el camión está lleno (alcanzó su capacidad máxima)
    public boolean estaLleno() {
        return pila.size() == capacidad;
    }

    // Indica si el camión está vacío
    public boolean estaVacio() {
        return pila.isEmpty();
    }

    // Devuelve la cantidad de paquetes cargados en el camión
    public int cantidadPaquetes() {
        return pila.size();
    }

    @Override
    public String toString() {
    return "Camion [patente=" + patente + 
           ", paquetes=" + pila.size() + "]";
    }
}
