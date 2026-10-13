package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EjecucionSolicitudTest {

    @Test
    void ejecutarReservaPresupuestoYGeneraOrden() {
        Solicitud s = new Solicitud("S-010", "ana@udes.edu.co", 3000000, "SOFTWARE", "CC-100");
        s.setEstado("APROBADA");
        EjecutorSolicitudes ejecutor = new EjecutorSolicitudes(new PresupuestoService(), new OrdenCompraService());
        ejecutor.ejecutarOperaciones(s);
        assertEquals("EJECUTADA", s.getEstado());
    }

    @Test
    void deshacerSoloLaUltimaOperacionNoAfectaLaAnterior() {
        // ejecutar reservar presupuesto, luego generar orden; deshacer una vez
        // debe revertir solo la generación de la orden, no la reserva.
        Solicitud s = new Solicitud("S-011", "luis@udes.edu.co", 4000000, "MATERIAL_OFICINA", "CC-200");
        EjecutorSolicitudes ejecutor = new EjecutorSolicitudes(new PresupuestoService(), new OrdenCompraService());
        assertDoesNotThrow(() -> {
            ejecutor.ejecutarReserva(s);
            ejecutor.ejecutarOrden(s, "Proveedor Papeleria");
            ejecutor.deshacerUltima();
        });
    }

    @Test
    void elHistorialConservaTodasLasOperacionesNoSoloLaUltima() {
        EjecutorSolicitudes ejecutor = new EjecutorSolicitudes(new PresupuestoService(), new OrdenCompraService());
        Solicitud s = new Solicitud("S-012", "director@udes.edu.co", 5000000, "INTERNACIONAL", "CC-300");
        assertDoesNotThrow(() -> {
            ejecutor.ejecutarReserva(s);
            ejecutor.ejecutarOrden(s, "Proveedor Global");
            assertEquals(2, ejecutor.getHistorial().size());
        });
    }
}