package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class SupervisorArea extends NivelAprobacion {
    public static final double LIMITE = 2000000.0;
    public static final String NOMBRE_NIVEL = "Supervisor de Área";

    @Override
    public ResultadoAprobacion procesar(Solicitud solicitud) {
        if (solicitud.getMonto() <= LIMITE) {
            solicitud.setEstado("APROBADA");
            solicitud.setNivelResolutor(NOMBRE_NIVEL);
            return new ResultadoAprobacion(true, NOMBRE_NIVEL, "Aprobada por Supervisor de Área dentro de su límite");
        }
        return pasarAlSiguiente(solicitud);
    }
}