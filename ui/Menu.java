package ui;

import model.*;
import java.util.Scanner;

public class Menu {
    private SistemaGestion<String> sistema;
    private Scanner scanner;

    public Menu() {
        this.scanner = new Scanner(System.in);
        System.out.print("Ingrese la patente del camión: ");
        String patente = scanner.nextLine().trim();
        this.sistema = new SistemaGestion<>(patente);
    }

    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1 -> cargarPaquete();
                case 2 -> deshacerUltimaCarga();
                case 3 -> transferirAlCentro();
                case 4 -> despacharPaquete();
                case 5 -> verEstado();
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void mostrarMenu() {
        System.out.println("\n===== LogiUADE 2026 =====");
        System.out.println("1. Cargar paquete al camión");
        System.out.println("2. Deshacer última carga");
        System.out.println("3. Transferir camión al centro");
        System.out.println("4. Despachar paquete del centro");
        System.out.println("5. Ver estado del sistema");
        System.out.println("0. Salir");
        System.out.println("=========================");
    }

    private void cargarPaquete() {
        System.out.println("\n-- Cargar Paquete --");
        int id = leerEntero("ID del paquete: ");
        double peso = leerDouble("Peso (kg): ");
        System.out.print("Destino: ");
        String destino = scanner.nextLine().trim();
        boolean urgente = leerBoolean("¿Es urgente? (s/n): ");
        TipoCarga tipo = leerTipoCarga();
        System.out.print("Contenido (descripción): ");
        String contenido = scanner.nextLine().trim();

        try {
            Paquete<String> paquete = new Paquete<>(id, peso, destino, urgente, tipo, contenido);
            sistema.cargarPaqueteEnCamion(paquete);
            System.out.println("Paquete cargado correctamente: " + paquete);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deshacerUltimaCarga() {
        try {
            Paquete<String> removido = sistema.getCamion().descargarUltimoPaquete();
            System.out.println("Última carga deshecha: " + removido);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void transferirAlCentro() {
        if (sistema.getCamion().estaVacio()) {
            System.out.println("El camión está vacío, no hay paquetes para transferir.");
            return;
        }
        sistema.transferirAlCentro();
        System.out.println("Todos los paquetes fueron transferidos al centro de distribución.");
    }

    private void despacharPaquete() {
        try {
            Paquete<String> despachado = sistema.despacharPaquete();
            System.out.println("Paquete despachado: " + despachado);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void verEstado() {
        System.out.println("\n-- Estado del Sistema --");
        System.out.println("Camión: " + sistema.getCamion());
        System.out.println("Centro: " + sistema.getCentro());
        System.out.println("  Prioritarios: " + sistema.getCentro().cantidadPrioritarios());
        System.out.println("  Normales:     " + sistema.getCentro().cantidadNormales());
    }

    // --- Helpers de lectura ---

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero válido.");
            }
        }
    }

    private double leerDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                double valor = Double.parseDouble(scanner.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido.");
            }
        }
    }

    private boolean leerBoolean(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String resp = scanner.nextLine().trim().toLowerCase();
            if (resp.equals("s")) return true;
            if (resp.equals("n")) return false;
            System.out.println("Ingrese 's' o 'n'.");
        }
    }

    private TipoCarga leerTipoCarga() {
        System.out.println("Tipos de carga: 1=ELECTRONICA  2=ALIMENTOS  3=FRAGILES  4=GENERAL");
        while (true) {
            int op = leerEntero("Seleccione tipo: ");
            switch (op) {
                case 1 -> { return TipoCarga.ELECTRONICA; }
                case 2 -> { return TipoCarga.ALIMENTOS; }
                case 3 -> { return TipoCarga.FRAGILES; }
                case 4 -> { return TipoCarga.GENERAL; }
                default -> System.out.println("Opción inválida, ingrese 1-4.");
            }
        }
    }
}
