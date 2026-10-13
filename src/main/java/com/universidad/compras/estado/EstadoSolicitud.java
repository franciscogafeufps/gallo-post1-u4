package com.universidad.compras.estado;

public interface EstadoSolicitud {
    String getNombre();
    boolean aprobar(SolicitudContexto contexto);
    boolean rechazar(SolicitudContexto contexto);
    boolean ejecutar(SolicitudContexto contexto);
    boolean cancelar(SolicitudContexto contexto);
}