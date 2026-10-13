package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GestorNotificacionesEstado {

    private final List<SuscriptorEstado> suscriptores = new ArrayList<>();

    public GestorNotificacionesEstado() {
        // Registra por defecto las tres reacciones exigidas
        suscribir(new NotificadorCorreoSuscriptor());
        suscribir(new ActualizadorDashboardSuscriptor());
        suscribir(new RegistradorAuditoriaSuscriptor());
    }

    public void suscribir(SuscriptorEstado suscriptor) {
        if (suscriptor != null && !suscriptores.contains(suscriptor)) {
            suscriptores.add(suscriptor);
        }
    }

    public void desuscribir(SuscriptorEstado suscriptor) {
        suscriptores.remove(suscriptor);
    }

    public void cambiarEstado(Solicitud solicitud, String nuevoEstado) {
        String estadoAnterior = solicitud.getEstado();
        solicitud.setEstado(nuevoEstado);
        notificar(solicitud, estadoAnterior, nuevoEstado);
    }

    public void notificar(Solicitud solicitud, String estadoAnterior, String nuevoEstado) {
        for (SuscriptorEstado s : suscriptores) {
            s.onCambioEstado(solicitud, estadoAnterior, nuevoEstado);
        }
    }

    public int getCantidadSuscriptores() {
        return suscriptores.size();
    }
}