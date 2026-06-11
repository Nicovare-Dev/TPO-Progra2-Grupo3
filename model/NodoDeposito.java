package model;

import java.time.LocalDateTime;

public class NodoDeposito {
    private int idDeposito;
    private boolean visitado;
    private LocalDateTime fechaUltimaAuditoria;
    private NodoDeposito izquierdo;
    private NodoDeposito derecho;

    public NodoDeposito(int idDeposito, LocalDateTime fechaUltimaAuditoria) {
        this.idDeposito = idDeposito;
        this.fechaUltimaAuditoria = fechaUltimaAuditoria;
        this.visitado = false;
        this.izquierdo = null;
        this.derecho = null;
    }

    public int getIdDeposito() {
        return idDeposito;
    }

    public boolean isVisitado() {
        return visitado;
    }

    public void setVisitado(boolean visitado) {
        this.visitado = visitado;
    }

    public LocalDateTime getFechaUltimaAuditoria() {
        return fechaUltimaAuditoria;
    }

    public void setFechaUltimaAuditoria(LocalDateTime fechaUltimaAuditoria) {
        this.fechaUltimaAuditoria = fechaUltimaAuditoria;
    }

    public NodoDeposito getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(NodoDeposito izquierdo) {
        this.izquierdo = izquierdo;
    }

    public NodoDeposito getDerecho() {
        return derecho;
    }

    public void setDerecho(NodoDeposito derecho) {
        this.derecho = derecho;
    }

    @Override
    public String toString() {
        String fecha = (fechaUltimaAuditoria == null) ? "nunca auditado" : fechaUltimaAuditoria.toLocalDate().toString();
        return "Deposito [id=" + idDeposito + ", visitado=" + visitado + ", ultimaAuditoria=" + fecha + "]";
    }
}
