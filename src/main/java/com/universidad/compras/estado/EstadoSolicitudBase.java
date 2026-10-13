package com.universidad.compras.estado;

public abstract class EstadoSolicitudBase implements EstadoSolicitud {
    @Override
    public boolean aprobar(SolicitudContexto contexto) {
        return false;
    }

    @Override
    public boolean rechazar(SolicitudContexto contexto) {
        return false;
    }

    @Override
    public boolean ejecutar(SolicitudContexto contexto) {
        return false;
    }

    @Override
    public boolean cancelar(SolicitudContexto contexto) {
        return false;
    }
}