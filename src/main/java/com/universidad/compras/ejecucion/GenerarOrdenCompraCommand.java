package com.universidad.compras.ejecucion;

public class GenerarOrdenCompraCommand implements Comando {

    private final OrdenCompraService ordenCompraService;
    private final String solicitudId;
    private final String proveedor;
    private String numeroOrdenGenerado;

    public GenerarOrdenCompraCommand(OrdenCompraService ordenCompraService, String solicitudId, String proveedor) {
        this.ordenCompraService = ordenCompraService;
        this.solicitudId = solicitudId;
        this.proveedor = proveedor;
    }

    @Override
    public boolean ejecutar() {
        this.numeroOrdenGenerado = ordenCompraService.generar(solicitudId, proveedor);
        return this.numeroOrdenGenerado != null;
    }

    @Override
    public void deshacer() {
        if (this.numeroOrdenGenerado != null) {
            ordenCompraService.cancelar(this.numeroOrdenGenerado);
            this.numeroOrdenGenerado = null;
        }
    }

    @Override
    public String getNombre() {
        return "GenerarOrdenCompra";
    }
}