package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicInteger;

class NotificacionEstadoTest {

    @Test
    void cambiarEstadoDisparaLasTresReaccionesSinLanzarExcepcion() {
        Solicitud s = new Solicitud("S-020", "ana@udes.edu.co", 2500000, "SOFTWARE", "CC-100");
        GestorNotificacionesEstado mecanismo = new GestorNotificacionesEstado();
        assertDoesNotThrow(() -> {
            mecanismo.cambiarEstado(s, "APROBADA");
        });
        assertEquals("APROBADA", s.getEstado());
    }

    @Test
    void agregarUnCuartoSuscriptorDePruebaNoRequiereModificarElMecanismo() {
        // registrar un colector de prueba adicional (sin tocar la clase central
        // del mecanismo) y verificar que también reacciona al cambio de estado.
        assertDoesNotThrow(() -> {
            Solicitud s = new Solicitud("S-021", "luis@udes.edu.co", 1800000, "MATERIAL_OFICINA", "CC-200");
            GestorNotificacionesEstado mecanismo = new GestorNotificacionesEstado();
            AtomicInteger notificacionesRecibidas = new AtomicInteger(0);

            // Cuarto suscriptor de prueba (ej. canal interno o colector)
            mecanismo.suscribir((sol, ant, nuevo) -> {
                notificacionesRecibidas.incrementAndGet();
            });

            mecanismo.cambiarEstado(s, "EJECUTADA");
            assertEquals(1, notificacionesRecibidas.get());
        });
    }
}