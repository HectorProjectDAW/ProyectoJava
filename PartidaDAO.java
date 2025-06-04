package com.proyecto.mi_proyecto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartidaDAO {

	
	//tener conexion a sql
    private static final String URL = "jdbc:mysql://localhost:33306/Login";
    private static final String USER = "root";
    private static final String PASS = "alumnoalumno";

    public static void guardarPartida(Partida partida) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {

            String aciertosStr = listaAString(partida.getAciertos());
            String fallidasStr = listaAString(partida.getLetrasFallidas());

            // Intentar UPDATE primero de tematica, palabra...
            String sqlUpdate = "UPDATE Partidas SET tematica=?, palabra=?, aciertos=?, letrasfallidas=?, racha=? WHERE usuario=?";
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setString(1, partida.getTematica());
            psUpdate.setString(2, partida.getPalabra());
            psUpdate.setString(3, aciertosStr);
            psUpdate.setString(4, fallidasStr);
            psUpdate.setInt(5, partida.getRacha());
            psUpdate.setString(6, partida.getUser());

            int filas = psUpdate.executeUpdate();

            if (filas == 0) {
                String sqlInsert = "INSERT INTO Partidas (usuario, tematica, palabra, aciertos, letrasfallidas, racha) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
                psInsert.setString(1, partida.getUser());
                psInsert.setString(2, partida.getTematica());
                psInsert.setString(3, partida.getPalabra());
                psInsert.setString(4, aciertosStr);
                psInsert.setString(5, fallidasStr);
                psInsert.setInt(6, partida.getRacha());
                psInsert.executeUpdate();
            }
        }
    }

    
    //Carga la partida con todas las cosas que se han guardado en la base de datos
    public static Partida cargarPartida(String usuario) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            String sql = "SELECT * FROM Partidas WHERE usuario=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String tematica = rs.getString("tematica");
                String palabra = rs.getString("palabra");
                List<Character> aciertos = stringALista(rs.getString("aciertos"));
                List<Character> fallidas = stringALista(rs.getString("letrasfallidas"));
                int racha = rs.getInt("racha");

                return new Partida(tematica, palabra, usuario, aciertos, fallidas, racha);
            }
            return null;
        }
    }

    private static String listaAString(List<Character> lista) {
        StringBuilder sb = new StringBuilder();
        for (char c : lista) {
            sb.append(c);
        }
        return sb.toString();
    }

    private static List<Character> stringALista(String s) {
        List<Character> lista = new ArrayList<>();
        if (s != null) {
            for (char c : s.toCharArray()) {
                lista.add(c);
            }
        }
        return lista;
    }
}
