package com.proyecto.mi_proyecto;


public class SesionUsuario {
    private static long idUsuarioActual = -1;

    public static void setIdUsuario(long id) {
        idUsuarioActual = id;
    }

    public static long getIdUsuario() {
        return idUsuarioActual;
    }
}

