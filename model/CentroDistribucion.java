package model;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class CentroDistribucion<T> {
    private Queue<Paquete<T>> colaPrioridad;
    private Queue<Paquete<T>> colaNormal;
    private Set<Integer> IdsRegistrados;

    public CentroDistribucion() {
        this.colaPrioridad = new ArrayDeque<>();
        this.colaNormal = new ArrayDeque<>();
        this.IdsRegistrados = new HashSet<>();
    }

    // Registra un paquete (valida ID único) y lo encola según su prioridad.
    public void registrarPaquete(Paquete<T> paquete){
        if (IdsRegistrados.contains(paquete.getId())){
            throw new IllegalArgumentException("El id de ese paquete ya es existente");
        }
        IdsRegistrados.add(paquete.getId());
        if (esPrioritario(paquete)){
            colaPrioridad.add(paquete);
        }
        else{
            colaNormal.add(paquete);
        }
    }

    // Indica si un paquete debe ir a la cola prioritaria.
    public boolean esPrioritario(Paquete<T> paquete){
        return paquete.isUrgente() || paquete.getPeso() > 50;
    }

    // Devuelve el próximo paquete a entregar (prioritarios primero), sin desencolarlo.
    public Paquete<T> obtenerProximoPaquete() {
        if (!colaPrioridad.isEmpty()) {
            return colaPrioridad.peek();
        } else if (!colaNormal.isEmpty()) {
            return colaNormal.peek();
        } else {
            throw new IllegalStateException("No hay paquetes pendientes en el centro de distribución.");
        }
    }

    // Indica si existe al menos un paquete pendiente en alguna cola.
    public boolean hayPaquetesPendientes(){
        return !colaNormal.isEmpty() || !colaPrioridad.isEmpty();
    }

    // Devuelve cuántos paquetes hay en la cola prioritaria.
    public int cantidadPrioritarios() {
        return colaPrioridad.size();
    }

    // Devuelve cuántos paquetes hay en la cola normal.
    public int cantidadNormales() {
        return colaNormal.size();
    }

    // Devuelve el total de paquetes pendientes (prioritarios + normales).
    public int cantidadTotalPaquetes() {
        return colaPrioridad.size() + colaNormal.size();
    }


}
