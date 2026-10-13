package com.universidad.compras.estado;

public class EstadoRechazada extends EstadoSolicitudBase {
    @Override
    public String getNombre() {
        return "RECHAZADA";
    }
}