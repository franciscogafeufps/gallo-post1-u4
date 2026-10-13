package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

@Service
public class EjecutorSolicitudes {

    private final PresupuestoService presupuestoService;
    private final OrdenCompraService ordenCompraService;
    private final List<Comando> historial;
    private final Deque<Comando> pilaDeshacer;

    public EjecutorSolicitudes() {
        this(new PresupuestoService(), new OrdenCompraService());
    }

    public EjecutorSolicitudes(PresupuestoService presupuestoService, OrdenCompraService ordenCompraService) {
        this.presupuestoService = presupuestoService;
        this.ordenCompraService = ordenCompraService;
        this.historial = new ArrayList<>();
        this.pilaDeshacer = new ArrayDeque<>();
    }

    public boolean ejecutar(Comando comando) {
        boolean ok = comando.ejecutar();
        if (ok) {
            historial.add(comando);
            pilaDeshacer.push(comando);
        }
        return ok;
    }

    public boolean deshacerUltima() {
        if (!pilaDeshacer.isEmpty()) {
            Comando ultimo = pilaDeshacer.pop();
            ultimo.deshacer();
            return true;
        }
        return false;
    }

    public boolean ejecutarReserva(Solicitud solicitud) {
        Comando cmd = new ReservarPresupuestoCommand(presupuestoService, solicitud.getCentroCosto(), solicitud.getMonto());
        return ejecutar(cmd);
    }

    public boolean ejecutarOrden(Solicitud solicitud, String proveedor) {
        Comando cmd = new GenerarOrdenCompraCommand(ordenCompraService, solicitud.getId(), proveedor);
        return ejecutar(cmd);
    }

    public void ejecutarOperaciones(Solicitud solicitud) {
        ejecutarOperaciones(solicitud, "Proveedor General");
    }

    public void ejecutarOperaciones(Solicitud solicitud, String proveedor) {
        boolean res = ejecutarReserva(solicitud);
        boolean ord = ejecutarOrden(solicitud, proveedor);
        if (res && ord) {
            solicitud.setEstado("EJECUTADA");
        }
    }

    public List<Comando> getHistorial() {
        return Collections.unmodifiableList(historial);
    }
}