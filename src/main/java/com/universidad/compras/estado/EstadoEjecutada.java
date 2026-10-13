package com.universidad.compras.estado;

public class EstadoEjecutada extends EstadoSolicitudBase {
    @Override
    public String getNombre() {
        return "EJECUTADA";
    }
    // Estado final: no permite transicionar a ningún otro estado ni volver a ejecutarse
}