package com.universidad.compras.estado;

public final class EstadoSolicitudFactory {
    private EstadoSolicitudFactory() {}

    public static EstadoSolicitud crear(String estado) {
        if (estado == null) {
            return new EstadoPendiente();
        }
        return switch (estado.toUpperCase()) {
            case "APROBADA" -> new EstadoAprobada();
            case "EJECUTADA" -> new EstadoEjecutada();
            case "RECHAZADA" -> new EstadoRechazada();
            case "CANCELADA" -> new EstadoCancelada();
            default -> new EstadoPendiente();
        };
    }
}