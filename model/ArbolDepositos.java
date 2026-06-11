package model;

import java.time.LocalDateTime;

public class ArbolDepositos {
    private NodoDeposito raiz;

    public ArbolDepositos() {
        this.raiz = null;
    }

    // Complejidad temporal: O(1).
    public boolean estaVacio() {
        return raiz == null;
    }

    // Complejidad temporal: O(h), con h = altura del árbol (O(log n) si está
    // balanceado, O(n) en el peor caso degenerado).
    // Complejidad espacial: O(h) por la pila de recursión.
    public void insertar(int idDeposito, LocalDateTime fechaUltimaAuditoria) {
        raiz = insertarRec(raiz, idDeposito, fechaUltimaAuditoria);
    }

    private NodoDeposito insertarRec(NodoDeposito actual, int id, LocalDateTime fecha) {
        if (actual == null) {
            return new NodoDeposito(id, fecha);
        }
        if (id < actual.getIdDeposito()) {
            actual.setIzquierdo(insertarRec(actual.getIzquierdo(), id, fecha));
        } else if (id > actual.getIdDeposito()) {
            actual.setDerecho(insertarRec(actual.getDerecho(), id, fecha));
        } else {
            throw new IllegalArgumentException("Ya existe un depósito con id " + id);
        }
        return actual;
    }

    // Complejidad temporal: O(h).
    public NodoDeposito buscar(int idDeposito) {
        NodoDeposito actual = raiz;
        while (actual != null && actual.getIdDeposito() != idDeposito) {
            if (idDeposito < actual.getIdDeposito()) {
                actual = actual.getIzquierdo();
            } else {
                actual = actual.getDerecho();
            }
        }
        return actual;
    }

    // Complejidad temporal: O(n) — visita cada nodo exactamente una vez.
    // Complejidad espacial: O(h) por la pila de recursión.
    public int auditar() {
        LocalDateTime limite = LocalDateTime.now().minusDays(30);
        return auditarPostOrden(raiz, limite);
    }

    private int auditarPostOrden(NodoDeposito actual, LocalDateTime limite) {
        if (actual == null) {
            return 0;
        }
        int marcados = auditarPostOrden(actual.getIzquierdo(), limite);
        marcados += auditarPostOrden(actual.getDerecho(), limite);
        LocalDateTime fecha = actual.getFechaUltimaAuditoria();
        if (fecha == null || fecha.isBefore(limite)) {
            actual.setVisitado(true);
            marcados++;
        }
        return marcados;
    }

    // Complejidad temporal: O(n) — en el peor caso recorre todo el árbol.
    // Complejidad espacial: O(h) por la pila de recursión.
    public void imprimirNivel(int nivel) {
        if (nivel < 1) {
            System.out.println("El nivel debe ser 1 o mayor (la raíz es el nivel 1).");
            return;
        }
        System.out.println("Depósitos en el nivel " + nivel + ":");
        boolean huboAlguno = imprimirNivelRec(raiz, nivel);
        if (!huboAlguno) {
            System.out.println("  (no hay depósitos en ese nivel)");
        }
    }

    private boolean imprimirNivelRec(NodoDeposito actual, int nivelRestante) {
        if (actual == null) {
            return false;
        }
        if (nivelRestante == 1) {
            System.out.println("  " + actual);
            return true;
        }
        boolean izq = imprimirNivelRec(actual.getIzquierdo(), nivelRestante - 1);
        boolean der = imprimirNivelRec(actual.getDerecho(), nivelRestante - 1);
        return izq | der;
    }

    // Complejidad temporal: O(n).
    public void imprimirInOrden() {
        if (estaVacio()) {
            System.out.println("  (no hay depósitos registrados)");
            return;
        }
        imprimirInOrdenRec(raiz);
    }

    private void imprimirInOrdenRec(NodoDeposito actual) {
        if (actual == null) {
            return;
        }
        imprimirInOrdenRec(actual.getIzquierdo());
        System.out.println("  " + actual);
        imprimirInOrdenRec(actual.getDerecho());
    }
}
