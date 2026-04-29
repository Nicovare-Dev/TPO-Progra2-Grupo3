package persistencia;

import model.Paquete;
import model.SistemaGestion;
import model.TipoCarga;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

// Carga paquetes desde un archivo JSON al SistemaGestion.
// Estructura esperada:
// {
//   "paquetes": [
//     { "id": int, "peso": double, "destino": string,
//       "urgente": bool, "tipoCarga": string, "contenido": string }, ...
//   ]
// }
public class InventarioLoader {

    // Lee el archivo, parsea el JSON y carga los paquetes en el camion del sistema.
    // Devuelve la cantidad de paquetes efectivamente cargados.
    // Complejidad temporal: O(n + m), donde n = tamaño del archivo y m = cantidad de paquetes.
    // Complejidad espacial: O(n) por la estructura intermedia del parser.
    public static int cargarDesdeArchivo(String rutaArchivo, SistemaGestion<String> sistema) throws Exception {
        String contenido = Files.readString(Path.of(rutaArchivo));
        Object raiz = JsonParser.parse(contenido);
        if (!(raiz instanceof Map<?, ?> obj)) {
            throw new IllegalArgumentException("El archivo JSON debe contener un objeto en su raiz.");
        }
        Object paquetesRaw = obj.get("paquetes");
        if (!(paquetesRaw instanceof List<?> lista)) {
            throw new IllegalArgumentException("Falta el arreglo 'paquetes' en el JSON.");
        }

        int cargados = 0;
        for (Object item : lista) {
            if (!(item instanceof Map<?, ?> p)) {
                System.out.println("Entrada invalida en 'paquetes', se omite.");
                continue;
            }
            Paquete<String> paquete;
            try {
                paquete = construirPaquete(p);
            } catch (RuntimeException e) {
                System.out.println("Paquete con datos invalidos, se omite: " + e.getMessage());
                continue;
            }
            try {
                sistema.cargarPaqueteEnCamion(paquete);
                cargados++;
            } catch (IllegalStateException e) {
                System.out.println("No se pudo cargar paquete id=" + paquete.getId() + ": " + e.getMessage());
                break;
            }
        }
        return cargados;
    }

    // Construye un Paquete<String> a partir de un mapa proveniente del JSON.
    // Complejidad temporal y espacial: O(1).
    private static Paquete<String> construirPaquete(Map<?, ?> p) {
        int id = leerEntero(p, "id");
        double peso = leerDouble(p, "peso");
        String destino = leerString(p, "destino");
        boolean urgente = p.get("urgente") instanceof Boolean b ? b : false;
        TipoCarga tipo = TipoCarga.valueOf(leerString(p, "tipoCarga").trim().toUpperCase());
        Object contRaw = p.get("contenido");
        String contenido = contRaw == null ? "" : contRaw.toString();
        return new Paquete<>(id, peso, destino, urgente, tipo, contenido);
    }

    private static int leerEntero(Map<?, ?> p, String campo) {
        Object v = p.get(campo);
        if (!(v instanceof Number n)) {
            throw new IllegalArgumentException("Campo '" + campo + "' faltante o no numerico");
        }
        return n.intValue();
    }

    private static double leerDouble(Map<?, ?> p, String campo) {
        Object v = p.get(campo);
        if (!(v instanceof Number n)) {
            throw new IllegalArgumentException("Campo '" + campo + "' faltante o no numerico");
        }
        return n.doubleValue();
    }

    private static String leerString(Map<?, ?> p, String campo) {
        Object v = p.get(campo);
        if (!(v instanceof String s)) {
            throw new IllegalArgumentException("Campo '" + campo + "' faltante o no es texto");
        }
        return s;
    }
}
