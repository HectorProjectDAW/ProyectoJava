package com.proyecto.mi_proyecto;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TematicaMongo {

    // URI y nombre base datos como constantes para no repetir
    private static final String URI = "mongodb://root:alumnoalumno@localhost:27017/";
    private static final String DB_NAME = "AHORCADO_TEMATICA";

    
    // Devuelve una lista con 'cantidad' temáticas aleatorias desde la base de datos.
     
    public static List<String> Temas(int cantidad) {
        List<String> lista = new ArrayList<>();

        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> coleccion = database.getCollection("tematicas");

            // Recoger todas las tematicas (campo "nombre") y agregarlas a la lista
            for (Document documento : coleccion.find()) {
                lista.add(documento.getString("nombre"));
            }

            // Mezclar la lista para aleatorizar
            Collections.shuffle(lista);

            // Retornar solo la cantidad solicitada (o menos si no hay tantas)
            return lista.subList(0, Math.min(cantidad, lista.size()));
        }
    }

    
     // Devuelve una palabra aleatoria de la temática dada.
     
    public static String palabraRandom(String tematica) {
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> coleccion = database.getCollection("tematicas");

            // Buscar el documento que tenga el nombre de la temática indicada
            Document filtro = new Document("nombre", tematica);
            Document documento = coleccion.find(filtro).first();

            if (documento != null) {
                // Obtener la lista de palabras asociadas a esa temática
                List<String> palabras = documento.getList("palabras", String.class);

                if (palabras != null && !palabras.isEmpty()) {
                    Collections.shuffle(palabras);
                    return palabras.get(0); // Retornar una palabra aleatoria
                }
            }
        }
        return "sin_palabra"; //Si no encuentra nada
    }

    
     //Devuelve todas las temáticas disponibles
     
    public static List<String> obtenerTodas() {
        List<String> lista = new ArrayList<>();

        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> coleccion = database.getCollection("tematicas");

            for (Document documento : coleccion.find()) {
                lista.add(documento.getString("nombre"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static void guardarPartida(String usuario, String tematica, String palabra, String estadoActual, 
                                     int errores, int racha, List<Character> letrasFallidas) {
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> partidas = database.getCollection("partidas");

            // Buscar si ya existe una partida guardada para ese usuario
            Document filtro = new Document("usuario", usuario);
            Document partidaExistente = partidas.find(filtro).first();

            // Crear el documento con todos los datos de la partida
            Document partidaDoc = new Document("usuario", usuario)
                    .append("tematica", tematica)
                    .append("palabra", palabra)
                    .append("estadoActual", estadoActual)
                    .append("errores", errores)
                    .append("racha", racha)
                    .append("letrasFallidas", letrasFallidas);

            if (partidaExistente == null) {
                // Insertar nueva partida
                partidas.insertOne(partidaDoc);
            } else {
                // Actualizar partida existente
                partidas.updateOne(filtro, new Document("$set", partidaDoc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    // Carga la partida guardada de un usuario.
    
    // Devuelve el documento con los datos o null si no existe.
     
    public static Document cargarPartida(String usuario) {
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> partidas = database.getCollection("partidas");

            Document filtro = new Document("usuario", usuario);
            return partidas.find(filtro).first();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
