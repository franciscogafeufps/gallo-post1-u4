package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

public class ActualizadorDashboardSuscriptor implements SuscriptorEstado {
    @Override
    public void onCambioEstado(Solicitud solicitud, String estadoAnterior, String nuevoEstado) {
        ClientesNotificacion.actualizarDashboardContabilidad(
            solicitud.getId(),
            nuevoEstado,
            solicitud.getMonto()
        );
    }
}