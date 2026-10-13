package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class GerenteArea extends NivelAprobacion {
    public static final double LIMITE = 10000000.0;
    public static final String NOMBRE_NIVEL = "Gerente de Área";

    @Override
    public ResultadoAprobacion procesar(Solicitud solicitud) {
        if (solicitud.getMonto() <= LIMITE) {
            solicitud.setEstado("APROBADA");
            solicitud.setNivelResolutor(NOMBRE_NIVEL);
            return new ResultadoAprobacion(true, NOMBRE_NIVEL, "Aprobada por Gerente de Área dentro de su límite");
        }
        return pasarAlSiguiente(solicitud);
    }
}