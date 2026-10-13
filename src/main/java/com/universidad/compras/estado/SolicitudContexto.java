package com.universidad.compras.estado;

import com.universidad.compras.modelo.Solicitud;

public class SolicitudContexto {

    private final Solicitud solicitud;
    private EstadoSolicitud estadoActual;

    public SolicitudContexto(Solicitud solicitud) {
        this.solicitud = solicitud;
        this.estadoActual = EstadoSolicitudFactory.crear(solicitud.getEstado());
    }

    public void setEstado(EstadoSolicitud nuevoEstado) {
        this.estadoActual = nuevoEstado;
        this.solicitud.setEstado(nuevoEstado.getNombre());
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public EstadoSolicitud getEstadoActual() {
        return estadoActual;
    }

    public boolean aprobar() {
        return estadoActual.aprobar(this);
    }

    public boolean rechazar() {
        return estadoActual.rechazar(this);
    }

    public boolean ejecutar() {
        return estadoActual.ejecutar(this);
    }

    public boolean cancelar() {
        return estadoActual.cancelar(this);
    }
}