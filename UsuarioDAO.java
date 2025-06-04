package com.proyecto.mi_proyecto;

import java.sql.*;

public class UsuarioDAO {

    private static final String URL = "jdbc:mysql://localhost:33306/Login";
    private static final String USER = "root";
    private static final String PASSWORD = "alumnoalumno";

    private Connection conexion;

    public UsuarioDAO() throws SQLException {
        this.conexion = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void actualizarTiempoJugado(String nombreUsuario, int segundos) throws SQLException {
        String sql = "UPDATE Usuarios SET tiempo_jugado = tiempo_jugado + ?, fecha_actualizacion = CURRENT_TIMESTAMP WHERE nombre_usuario = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, segundos);
            stmt.setString(2, nombreUsuario);
            stmt.executeUpdate();
        }
    }

    public void actualizarPartidasJugadas(String nombreUsuario) throws SQLException {
        String sql = "UPDATE Usuarios SET partidas_jugadas = partidas_jugadas + 1, fecha_actualizacion = CURRENT_TIMESTAMP WHERE nombre_usuario = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            stmt.executeUpdate();
        }
    }

    public void actualizarRachaVictorias(String nombreUsuario, int racha) throws SQLException {
        String sql = "UPDATE Usuarios SET racha_victorias = ? WHERE nombre_usuario = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, racha);
            stmt.setString(2, nombreUsuario);
            stmt.executeUpdate();
        }
    }

    public void actualizarPuntuacion(String nombreUsuario, int puntos) throws SQLException {
        String sql = "UPDATE Usuarios SET puntuacion = puntuacion + ?, fecha_actualizacion = CURRENT_TIMESTAMP WHERE nombre_usuario = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, puntos);
            stmt.setString(2, nombreUsuario);
            stmt.executeUpdate();
        }
    }
    
    public int obtenerPuntuacion(String nombreUsuario) throws SQLException {
        String sql = "SELECT puntuacion FROM Usuarios WHERE nombre_usuario = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("puntuacion");
                }
            }
        }
        return 0;
    }
    
    
    public void resetearEstadisticas(String nombreUsuario) {
        String sql = "UPDATE Usuarios SET racha_victorias = 0, puntuacion = 0 WHERE nombre_usuario = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
