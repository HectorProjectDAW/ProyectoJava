package com.proyecto.mi_proyecto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class User {
	
	private static String username;

    public static void iniciarSesion(String usuario) {
        username = usuario;
    }

    public static String getUsername() {
        return username;
    }

    public static void cerrarSesion() {
        username = null;
    }

    //Validar user y contraseña
    public static boolean validarCredenciales(String usuario, String contrasenya) {
        if (usuario.isEmpty() || contrasenya.isEmpty()) return false;

        String hashedPassword = hashPassword(contrasenya);

        //Prueba conexion a mysql a una BDD que se llama Login
        try (Connection conexion = DriverManager.getConnection(
                "jdbc:mysql://localhost:33306/Login", "root", "alumnoalumno")) {

        	
            String checkQuery = "SELECT * FROM Usuarios WHERE nombre_usuario = ?";
            try (PreparedStatement statement = conexion.prepareStatement(checkQuery)) {
                statement.setString(1, usuario);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        String contrasenyaBD = rs.getString("contrasenya");
                        return hashedPassword.equals(contrasenyaBD);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    
    //Lo mismo que antes, mira si existe el user
    public static boolean existeUsuario(String usuario) {
        try (Connection conexion = DriverManager.getConnection(
                "jdbc:mysql://localhost:33306/Login", "root", "alumnoalumno")) {

            String checkQuery = "SELECT * FROM Usuarios WHERE nombre_usuario = ?";
            try (PreparedStatement statement = conexion.prepareStatement(checkQuery)) {
                statement.setString(1, usuario);
                try (ResultSet rs = statement.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    //Igual, pero con el correo
    public static boolean existeCorreo(String correo) {
        try (Connection conexion = DriverManager.getConnection(
                "jdbc:mysql://localhost:33306/Login", "root", "alumnoalumno")) {

            String checkQuery = "SELECT * FROM Usuarios WHERE correo = ?";
            try (PreparedStatement statement = conexion.prepareStatement(checkQuery)) {
                statement.setString(1, correo);
                try (ResultSet rs = statement.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    //Registra el usuario
    public static boolean registrarUsuario(String nombreUsuario, String correo, String contrasenya) {
        String hashedPassword = hashPassword(contrasenya);

        try (Connection conexion = DriverManager.getConnection(
                "jdbc:mysql://localhost:33306/Login", "root", "alumnoalumno")) {

            String insertQuery = "INSERT INTO Usuarios (nombre_usuario, correo, contrasenya) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conexion.prepareStatement(insertQuery)) {
                ps.setString(1, nombreUsuario);
                ps.setString(2, correo);
                ps.setString(3, hashedPassword);
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    //Pone la contraseña en hashPassword
    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
