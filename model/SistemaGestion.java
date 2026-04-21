package model;

public class SistemaGestion<T> {
    private Camion<T> camion;
    private CentroDistribucion<T> centro;

    public SistemaGestion(String patente) {
        this.camion = new Camion<>(patente);
        this.centro = new CentroDistribucion<>();
    }

    // Carga un paquete en el camión.
    public void cargarPaqueteEnCamion(Paquete<T> paquete) {
        camion.cargarPaquete(paquete);
    }

    // Transfiere todos los paquetes del camión al centro de distribución.
    public void transferirAlCentro() {
        while (!camion.estaVacio()) {
            Paquete<T> paquete = camion.descargarUltimoPaquete();
            centro.registrarPaquete(paquete);
        }
    }

    // Despacha el próximo paquete del centro.
    public Paquete<T> despacharPaquete() {
        return centro.despacharPaquete();
    }

    // Getters para acceder al estado desde Main.
    public Camion<T> getCamion() {
        return camion;
    }

    public CentroDistribucion<T> getCentro() {
        return centro;
    }
}
