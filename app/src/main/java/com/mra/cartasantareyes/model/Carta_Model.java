package com.mra.cartasantareyes.model;

public class Carta_Model {

    public int idCarta;
    public int edad;
    public byte[] nombreFirma;
    public String nombreNino;
    public String opcionUno;
    public String opcionDos;
    public String opcionTres;
    public String fechaCreacion;
    public int sePorto;

    public int getSePorto() {
        return sePorto;
    }

    public void setSePorto(int sePorto) {
        this.sePorto = sePorto;
    }

    public int getIdCarta() {
        return idCarta;
    }

    public void setIdCarta(int idCarta) {
        this.idCarta = idCarta;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public byte[] getNombreFirma() {
        return nombreFirma;
    }

    public void setNombreFirma(byte[] nombreFirma) {
        this.nombreFirma = nombreFirma;
    }

    public String getNombreNino() {
        return nombreNino;
    }

    public void setNombreNino(String nombreNino) {
        this.nombreNino = nombreNino;
    }

    public String getOpcionUno() {
        return opcionUno;
    }

    public void setOpcionUno(String opcionUno) {
        this.opcionUno = opcionUno;
    }

    public String getOpcionDos() {
        return opcionDos;
    }

    public void setOpcionDos(String opcionDos) {
        this.opcionDos = opcionDos;
    }

    public String getOpcionTres() {
        return opcionTres;
    }

    public void setOpcionTres(String opcionTres) {
        this.opcionTres = opcionTres;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}