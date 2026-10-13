package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

public class NotificadorCorreoSuscriptor implements SuscriptorEstado {
    @Override
    public void onCambioEstado(Solicitud solicitud, String estadoAnterior, String nuevoEstado) {
        ClientesNotificacion.enviarCorreo(
            solicitud.getSolicitanteEmail(),
            "Actualización de Solicitud " + solicitud.getId(),
            "Su solicitud cambió de estado a " + nuevoEstado
        );
    }
}