package com.universidad.compras.estado;

public class EstadoCancelada extends EstadoSolicitudBase {
    @Override
    public String getNombre() {
        return "CANCELADA";
    }
}