package TPO.Gestion;

public class Paquete <T> {
    private String id;
    private double peso;
    private String destino;
    private boolean urgente;
    private T contenido;


public Paquete() {
}

//constructor
public Paquete(String id, double peso, String destino, boolean urgente, T contenido) {
    this.id = id;
    this.peso = peso;
    this.destino = destino;
    this.urgente = urgente;
    this.contenido = contenido;
}

//getters
public String getId() {
    return id;
}

public double getPeso() {
    return peso;
}

public String getDestino() {
    return destino;
}

public boolean isUrgente() {
    return urgente;
}

public T getContenido() {
    return contenido;
}

//setters
public void setId(String id) {
    this.id = id;
}

public void setPeso(double peso) {
    this.peso = peso;
}

public void setDestino(String destino) {
    this.destino = destino;
}

public void setUrgente(boolean urgente) {
    this.urgente = urgente;
}

public void setContenido(T contenido) {
    this.contenido = contenido;
}

//toString
@Override
public String toString() {
    return "Paquete [id=" + id + 
           ", peso=" + peso + 
           ", destino=" + destino + 
           ", urgente=" + urgente + 
           ", contenido=" + contenido + "]";
}

}