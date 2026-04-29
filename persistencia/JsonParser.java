package persistencia;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Parser JSON minimalista, sin dependencias externas.
// Devuelve estructuras de Java estándar:
//   objeto JSON  -> Map<String, Object>
//   arreglo JSON -> List<Object>
//   string       -> String
//   número       -> Long o Double
//   booleano     -> Boolean
//   null         -> null
public class JsonParser {
    private final String input;
    private int pos;

    private JsonParser(String input) {
        this.input = input;
        this.pos = 0;
    }

    // Punto de entrada: parsea un texto JSON completo.
    // Complejidad temporal: O(n) sobre la longitud del texto.
    // Complejidad espacial: O(n) por la estructura resultante.
    public static Object parse(String input) {
        JsonParser p = new JsonParser(input);
        p.skipWhitespace();
        Object value = p.parseValue();
        p.skipWhitespace();
        if (p.pos < p.input.length()) {
            throw new IllegalArgumentException("Texto extra al final del JSON en posicion " + p.pos);
        }
        return value;
    }

    private Object parseValue() {
        skipWhitespace();
        if (pos >= input.length()) {
            throw new IllegalArgumentException("Fin inesperado del JSON");
        }
        char c = input.charAt(pos);
        if (c == '{') return parseObject();
        if (c == '[') return parseArray();
        if (c == '"') return parseString();
        if (c == 't' || c == 'f') return parseBoolean();
        if (c == 'n') return parseNull();
        if (c == '-' || Character.isDigit(c)) return parseNumber();
        throw new IllegalArgumentException("Caracter inesperado '" + c + "' en posicion " + pos);
    }

    private Map<String, Object> parseObject() {
        Map<String, Object> map = new LinkedHashMap<>();
        expect('{');
        skipWhitespace();
        if (peek() == '}') { pos++; return map; }
        while (true) {
            skipWhitespace();
            String key = parseString();
            skipWhitespace();
            expect(':');
            Object value = parseValue();
            map.put(key, value);
            skipWhitespace();
            char next = input.charAt(pos);
            if (next == ',') { pos++; continue; }
            if (next == '}') { pos++; break; }
            throw new IllegalArgumentException("Esperaba ',' o '}' en posicion " + pos);
        }
        return map;
    }

    private List<Object> parseArray() {
        List<Object> list = new ArrayList<>();
        expect('[');
        skipWhitespace();
        if (peek() == ']') { pos++; return list; }
        while (true) {
            Object value = parseValue();
            list.add(value);
            skipWhitespace();
            char next = input.charAt(pos);
            if (next == ',') { pos++; continue; }
            if (next == ']') { pos++; break; }
            throw new IllegalArgumentException("Esperaba ',' o ']' en posicion " + pos);
        }
        return list;
    }

    private String parseString() {
        expect('"');
        StringBuilder sb = new StringBuilder();
        while (pos < input.length()) {
            char c = input.charAt(pos++);
            if (c == '"') return sb.toString();
            if (c == '\\') {
                if (pos >= input.length()) {
                    throw new IllegalArgumentException("Escape sin completar al final del JSON");
                }
                char esc = input.charAt(pos++);
                switch (esc) {
                    case '"' -> sb.append('"');
                    case '\\' -> sb.append('\\');
                    case '/' -> sb.append('/');
                    case 'n' -> sb.append('\n');
                    case 't' -> sb.append('\t');
                    case 'r' -> sb.append('\r');
                    case 'b' -> sb.append('\b');
                    case 'f' -> sb.append('\f');
                    case 'u' -> {
                        if (pos + 4 > input.length()) {
                            throw new IllegalArgumentException("Escape unicode incompleto");
                        }
                        String hex = input.substring(pos, pos + 4);
                        sb.append((char) Integer.parseInt(hex, 16));
                        pos += 4;
                    }
                    default -> throw new IllegalArgumentException("Escape invalido: \\" + esc);
                }
            } else {
                sb.append(c);
            }
        }
        throw new IllegalArgumentException("Cadena sin cerrar");
    }

    private Number parseNumber() {
        int start = pos;
        if (peek() == '-') pos++;
        while (pos < input.length()) {
            char c = input.charAt(pos);
            if (Character.isDigit(c) || c == '.' || c == 'e' || c == 'E' || c == '+' || c == '-') {
                pos++;
            } else {
                break;
            }
        }
        String num = input.substring(start, pos);
        if (num.contains(".") || num.contains("e") || num.contains("E")) {
            return Double.parseDouble(num);
        }
        return Long.parseLong(num);
    }

    private Boolean parseBoolean() {
        if (input.startsWith("true", pos)) { pos += 4; return true; }
        if (input.startsWith("false", pos)) { pos += 5; return false; }
        throw new IllegalArgumentException("Booleano invalido en posicion " + pos);
    }

    private Object parseNull() {
        if (input.startsWith("null", pos)) { pos += 4; return null; }
        throw new IllegalArgumentException("Valor null invalido en posicion " + pos);
    }

    private void skipWhitespace() {
        while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) pos++;
    }

    private char peek() {
        return input.charAt(pos);
    }

    private void expect(char c) {
        if (pos >= input.length() || input.charAt(pos) != c) {
            throw new IllegalArgumentException("Esperaba '" + c + "' en posicion " + pos);
        }
        pos++;
    }
}
