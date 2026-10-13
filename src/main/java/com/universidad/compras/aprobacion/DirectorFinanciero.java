package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class DirectorFinanciero extends NivelAprobacion {
    public static final String NOMBRE_NIVEL = "Director Financiero";

    @Override
    public ResultadoAprobacion procesar(Solicitud solicitud) {
        solicitud.setEstado("APROBADA");
        solicitud.setNivelResolutor(NOMBRE_NIVEL);
        return new ResultadoAprobacion(true, NOMBRE_NIVEL, "Aprobada por Director Financiero sin límite superior");
    }
}