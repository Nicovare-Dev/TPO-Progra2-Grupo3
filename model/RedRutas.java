package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class RedRutas {
    private Map<Integer, List<Ruta>> adyacencias;

    public RedRutas() {
        this.adyacencias = new HashMap<>();
    }

    // Complejidad temporal: O(1).
    public void agregarDeposito(int idDeposito) {
        adyacencias.putIfAbsent(idDeposito, new ArrayList<>());
    }

    // Complejidad temporal: O(1) amortizado.
    public void agregarRuta(int idOrigen, int idDestino, double distanciaKm) {
        if (idOrigen == idDestino) {
            throw new IllegalArgumentException("Una ruta debe conectar dos depósitos distintos.");
        }
        if (distanciaKm <= 0) {
            throw new IllegalArgumentException("La distancia debe ser mayor a 0 km.");
        }
        agregarDeposito(idOrigen);
        agregarDeposito(idDestino);
        adyacencias.get(idOrigen).add(new Ruta(idDestino, distanciaKm));
        adyacencias.get(idDestino).add(new Ruta(idOrigen, distanciaKm));
    }

    // Complejidad temporal: O(1).
    public boolean existeDeposito(int idDeposito) {
        return adyacencias.containsKey(idDeposito);
    }

    // Complejidad temporal: O(V + E) — visita cada vértice y arista a lo sumo una vez.
    // Complejidad espacial: O(V) — cola de pendientes y conjunto de visitados.
    public int distanciaMinimaEnSaltos(int idOrigen, int idDestino) {
        if (!existeDeposito(idOrigen) || !existeDeposito(idDestino)) {
            throw new IllegalArgumentException("Ambos depósitos deben existir en la red.");
        }
        if (idOrigen == idDestino) {
            return 0;
        }

        Queue<Integer> pendientes = new ArrayDeque<>();
        Set<Integer> visitados = new HashSet<>();
        Map<Integer, Integer> saltos = new HashMap<>();

        pendientes.add(idOrigen);
        visitados.add(idOrigen);
        saltos.put(idOrigen, 0);

        while (!pendientes.isEmpty()) {
            int actual = pendientes.poll();
            for (Ruta ruta : adyacencias.get(actual)) {
                int vecino = ruta.getIdDestino();
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    saltos.put(vecino, saltos.get(actual) + 1);
                    if (vecino == idDestino) {
                        return saltos.get(vecino);
                    }
                    pendientes.add(vecino);
                }
            }
        }
        return -1;
    }

    // Complejidad temporal: O(V + E).
    public void imprimirRed() {
        if (adyacencias.isEmpty()) {
            System.out.println("  (no hay rutas cargadas)");
            return;
        }
        for (Map.Entry<Integer, List<Ruta>> entrada : adyacencias.entrySet()) {
            System.out.println("  Deposito " + entrada.getKey() + ": " + entrada.getValue());
        }
    }
}
