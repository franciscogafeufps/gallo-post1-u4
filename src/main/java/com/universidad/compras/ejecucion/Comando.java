package com.universidad.compras.ejecucion;

public interface Comando {
    boolean ejecutar();
    void deshacer();
    String getNombre();
}