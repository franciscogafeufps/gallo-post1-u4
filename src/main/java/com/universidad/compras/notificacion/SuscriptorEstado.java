package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

public interface SuscriptorEstado {
    void onCambioEstado(Solicitud solicitud, String estadoAnterior, String nuevoEstado);
}