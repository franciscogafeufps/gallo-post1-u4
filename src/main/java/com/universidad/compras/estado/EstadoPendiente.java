package com.universidad.compras.estado;

public class EstadoPendiente extends EstadoSolicitudBase {
    @Override
    public String getNombre() {
        return "PENDIENTE";
    }

    @Override
    public boolean aprobar(SolicitudContexto contexto) {
        contexto.setEstado(new EstadoAprobada());
        return true;
    }

    @Override
    public boolean rechazar(SolicitudContexto contexto) {
        contexto.setEstado(new EstadoRechazada());
        return true;
    }

    @Override
    public boolean cancelar(SolicitudContexto contexto) {
        contexto.setEstado(new EstadoCancelada());
        return true;
    }

    @Override
    public boolean ejecutar(SolicitudContexto contexto) {
        // Inválido: debe estar aprobada antes de ejecutarse
        return false;
    }
}