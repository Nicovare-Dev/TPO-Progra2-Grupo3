package model;

public class Ruta {
    private int idDestino;
    private double distanciaKm;

    public Ruta(int idDestino, double distanciaKm) {
        this.idDestino = idDestino;
        this.distanciaKm = distanciaKm;
    }

    public int getIdDestino() {
        return idDestino;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    @Override
    public String toString() {
        return "-> deposito " + idDestino + " (" + distanciaKm + " km)";
    }
}
