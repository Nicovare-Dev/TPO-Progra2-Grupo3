package model;

public class SistemaGestion<T> {
    private Camion<T> camion;
    private CentroDistribucion<T> centro;
    private ArbolDepositos arbolDepositos;
    private RedRutas redRutas;

    public SistemaGestion(String patente) {
        this.camion = new Camion<>(patente);
        this.centro = new CentroDistribucion<>();
        this.arbolDepositos = new ArbolDepositos();
        this.redRutas = new RedRutas();
    }

    // Carga un paquete en el camión.
    // Complejidad temporal: O(1) — delega a Camion.cargarPaquete.
    public void cargarPaqueteEnCamion(Paquete<T> paquete) {
        camion.cargarPaquete(paquete);
    }

    // Transfiere todos los paquetes del camión al centro de distribución.
    // Complejidad temporal: O(n) — recorre cada paquete del camión una vez.
    // Complejidad espacial: O(1) — no usa estructuras auxiliares.
    public void transferirAlCentro() {
        while (!camion.estaVacio()) {
            Paquete<T> paquete = camion.descargarUltimoPaquete();
            try {
                centro.registrarPaquete(paquete);
            } catch (IllegalArgumentException e) {
                System.out.println("Advertencia: no se pudo registrar paquete id="
                        + paquete.getId() + " - " + e.getMessage());
                // el paquete ya salió del camión, al menos notificamos
            }
        }
    }

    // Despacha el próximo paquete del centro.
    // Complejidad temporal: O(1) — delega a CentroDistribucion.despacharPaquete.
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

    public ArbolDepositos getArbolDepositos() {
        return arbolDepositos;
    }

    public RedRutas getRedRutas() {
        return redRutas;
    }
}
