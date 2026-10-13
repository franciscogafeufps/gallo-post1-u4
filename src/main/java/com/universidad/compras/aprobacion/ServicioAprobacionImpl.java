package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.stereotype.Service;

@Service
public class ServicioAprobacionImpl implements ServicioAprobacion {

    private NivelAprobacion cadena;

    public ServicioAprobacionImpl() {
        this(false);
    }

    public ServicioAprobacionImpl(boolean incluirCumplimientoNormativo) {
        if (incluirCumplimientoNormativo) {
            this.cadena = construirCadenaConCumplimiento();
        } else {
            this.cadena = construirCadenaEstandar();
        }
    }

    public ServicioAprobacionImpl(NivelAprobacion cadena) {
        this.cadena = cadena;
    }

    public static NivelAprobacion construirCadenaEstandar() {
        NivelAprobacion supervisor = new SupervisorArea();
        NivelAprobacion gerente = new GerenteArea();
        NivelAprobacion director = new DirectorFinanciero();

        supervisor.setSiguiente(gerente);
        gerente.setSiguiente(director);
        return supervisor;
    }

    public static NivelAprobacion construirCadenaConCumplimiento() {
        NivelAprobacion cumplimiento = new RevisorCumplimientoNormativo();
        NivelAprobacion supervisor = new SupervisorArea();
        NivelAprobacion gerente = new GerenteArea();
        NivelAprobacion director = new DirectorFinanciero();

        cumplimiento.setSiguiente(supervisor);
        supervisor.setSiguiente(gerente);
        gerente.setSiguiente(director);
        return cumplimiento;
    }

    public void setCadena(NivelAprobacion cadena) {
        this.cadena = cadena;
    }

    @Override
    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        if (cadena != null) {
            return cadena.procesar(solicitud);
        }
        return new ResultadoAprobacion(false, "Sin resolutor", "Cadena de aprobación no configurada");
    }
}