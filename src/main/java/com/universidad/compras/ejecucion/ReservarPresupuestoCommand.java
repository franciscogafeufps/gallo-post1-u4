package com.universidad.compras.ejecucion;

public class ReservarPresupuestoCommand implements Comando {

    private final PresupuestoService presupuestoService;
    private final String centroCosto;
    private final double monto;
    private boolean ejecutado = false;

    public ReservarPresupuestoCommand(PresupuestoService presupuestoService, String centroCosto, double monto) {
        this.presupuestoService = presupuestoService;
        this.centroCosto = centroCosto;
        this.monto = monto;
    }

    @Override
    public boolean ejecutar() {
        ejecutado = presupuestoService.reservar(centroCosto, monto);
        return ejecutado;
    }

    @Override
    public void deshacer() {
        if (ejecutado) {
            presupuestoService.liberar(centroCosto, monto);
            ejecutado = false;
        }
    }

    @Override
    public String getNombre() {
        return "ReservarPresupuesto";
    }
}