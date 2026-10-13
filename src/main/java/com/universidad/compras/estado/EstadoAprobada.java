package com.universidad.compras.estado;

public class EstadoAprobada extends EstadoSolicitudBase {
    @Override
    public String getNombre() {
        return "APROBADA";
    }

    @Override
    public boolean ejecutar(SolicitudContexto contexto) {
        contexto.setEstado(new EstadoEjecutada());
        return true;
    }

    @Override
    public boolean cancelar(SolicitudContexto contexto) {
        contexto.setEstado(new EstadoCancelada());
        return true;
    }
}