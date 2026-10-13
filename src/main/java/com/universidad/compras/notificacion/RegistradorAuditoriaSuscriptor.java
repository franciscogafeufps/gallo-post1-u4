package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

public class RegistradorAuditoriaSuscriptor implements SuscriptorEstado {
    @Override
    public void onCambioEstado(Solicitud solicitud, String estadoAnterior, String nuevoEstado) {
        ClientesNotificacion.registrarAuditoria(
            solicitud.getId(),
            nuevoEstado,
            "Transición desde estado " + estadoAnterior
        );
    }
}