package ui;

import model.*;
import persistencia.InventarioLoader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
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
                case 6 -> cargarInventarioDesdeJson();
                case 7 -> agregarDeposito();
                case 8 -> ejecutarAuditoria();
                case 9 -> reportePorNivel();
                case 10 -> agregarRuta();
                case 11 -> calcularDistanciaMinima();
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
        System.out.println("6. Cargar inventario desde JSON");
        System.out.println("--- Red de Depósitos ---");
        System.out.println("7. Agregar depósito (ABB)");
        System.out.println("8. Ejecutar auditoría (post-orden)");
        System.out.println("9. Reporte de depósitos por nivel");
        System.out.println("10. Agregar ruta entre depósitos");
        System.out.println("11. Distancia mínima entre depósitos (saltos)");
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
        if (sistema.getCamion().estaVacio()) {
            System.out.println("No hay paquetes en el camión para deshacer.");
            return;
        }
        Paquete<String> removido = sistema.getCamion().descargarUltimoPaquete();
        System.out.println("Última carga deshecha: " + removido);
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

    private void cargarInventarioDesdeJson() {
        System.out.println("\n-- Cargar Inventario desde JSON --");
        System.out.print("Ruta del archivo (ENTER para usar 'inventario.json'): ");
        String ruta = scanner.nextLine().trim();
        if (ruta.isEmpty()) {
            ruta = "inventario.json";
        }
        try {
            int cargados = InventarioLoader.cargarDesdeArchivo(ruta, sistema);
            System.out.println("Se cargaron " + cargados + " paquete(s) al camión desde " + ruta + ".");
        } catch (Exception e) {
            System.out.println("Error al cargar inventario: " + e.getMessage());
        }
    }

    private void verEstado() {
        System.out.println("\n-- Estado del Sistema --");
        System.out.println("Camión: " + sistema.getCamion());
        System.out.println("Centro: " + sistema.getCentro());
        System.out.println("  Prioritarios: " + sistema.getCentro().cantidadPrioritarios());
        System.out.println("  Normales:     " + sistema.getCentro().cantidadNormales());
        System.out.println("Depósitos registrados (in-orden):");
        sistema.getArbolDepositos().imprimirInOrden();
        System.out.println("Red de rutas:");
        sistema.getRedRutas().imprimirRed();
    }

    // --- Red de Depósitos (ABB + Grafo) ---

    private void agregarDeposito() {
        System.out.println("\n-- Agregar Depósito --");
        int id = leerEntero("ID del depósito: ");
        LocalDateTime fecha = leerFechaAuditoria();
        try {
            sistema.getArbolDepositos().insertar(id, fecha);
            sistema.getRedRutas().agregarDeposito(id);
            System.out.println("Depósito " + id + " agregado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void ejecutarAuditoria() {
        System.out.println("\n-- Auditoría de Depósitos (recorrido post-orden) --");
        if (sistema.getArbolDepositos().estaVacio()) {
            System.out.println("No hay depósitos registrados.");
            return;
        }
        int marcados = sistema.getArbolDepositos().auditar();
        System.out.println("Se marcaron como visitados " + marcados
                + " depósito(s) sin auditoría en los últimos 30 días.");
        sistema.getArbolDepositos().imprimirInOrden();
    }

    private void reportePorNivel() {
        System.out.println("\n-- Reporte por Nivel (la raíz es el nivel 1) --");
        if (sistema.getArbolDepositos().estaVacio()) {
            System.out.println("No hay depósitos registrados.");
            return;
        }
        int nivel = leerEntero("Nivel a consultar: ");
        sistema.getArbolDepositos().imprimirNivel(nivel);
    }

    private void agregarRuta() {
        System.out.println("\n-- Agregar Ruta --");
        int origen = leerEntero("ID depósito origen: ");
        int destino = leerEntero("ID depósito destino: ");
        if (sistema.getArbolDepositos().buscar(origen) == null
                || sistema.getArbolDepositos().buscar(destino) == null) {
            System.out.println("Error: ambos depósitos deben estar registrados en el árbol (opción 7).");
            return;
        }
        double distancia = leerDouble("Distancia (km): ");
        try {
            sistema.getRedRutas().agregarRuta(origen, destino, distancia);
            System.out.println("Ruta agregada: " + origen + " <-> " + destino + " (" + distancia + " km).");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void calcularDistanciaMinima() {
        System.out.println("\n-- Distancia Mínima entre Depósitos (BFS) --");
        int origen = leerEntero("ID depósito origen: ");
        int destino = leerEntero("ID depósito destino: ");
        try {
            int saltos = sistema.getRedRutas().distanciaMinimaEnSaltos(origen, destino);
            if (saltos == -1) {
                System.out.println("No existe camino entre los depósitos " + origen + " y " + destino + ".");
            } else {
                System.out.println("Distancia mínima entre " + origen + " y " + destino + ": "
                        + saltos + " salto(s).");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
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

    private LocalDateTime leerFechaAuditoria() {
        while (true) {
            System.out.print("Fecha de última auditoría (AAAA-MM-DD, ENTER si nunca fue auditado): ");
            String texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(texto).atStartOfDay();
            } catch (DateTimeParseException e) {
                System.out.println("Fecha inválida. Use el formato AAAA-MM-DD, por ejemplo 2026-05-20.");
            }
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
