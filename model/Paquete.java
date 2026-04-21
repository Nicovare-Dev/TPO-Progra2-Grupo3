package model;

public class Paquete <T> {
    private TipoCarga tipoCarga;
    private int id;
    private double peso;
    private String destino;
    private boolean urgente;
    private T contenido;


public Paquete() {
}

//constructor
public Paquete(int id, double peso, String destino, boolean urgente, TipoCarga tipoCarga, T contenido) {
    this.id = id;
    this.peso = peso;
    this.destino = destino;
    this.urgente = urgente;
    this.tipoCarga = tipoCarga;
    this.contenido = contenido;
}

//getters
public TipoCarga getTipoCarga() {
    return tipoCarga;
}

public int getId() {
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
public void setTipoCarga(TipoCarga tipoCarga) {
    this.tipoCarga = tipoCarga;
}

public void setId(int id) {
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
           ", tipoCarga=" + tipoCarga + 
           ", contenido=" + contenido + "]";
}

}