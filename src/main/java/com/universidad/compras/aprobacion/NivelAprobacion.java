package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public abstract class NivelAprobacion {
    protected NivelAprobacion siguiente;

    public NivelAprobacion setSiguiente(NivelAprobacion siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    public abstract ResultadoAprobacion procesar(Solicitud solicitud);

    protected ResultadoAprobacion pasarAlSiguiente(Solicitud solicitud) {
        if (siguiente != null) {
            return siguiente.procesar(solicitud);
        }
        return new ResultadoAprobacion(false, "Sin resolutor", "No hay nivel que pueda resolver la solicitud");
    }
}