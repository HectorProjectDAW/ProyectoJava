package com.proyecto.mi_proyecto;

import com.mongodb.client.*;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TematicaMongo {

    //Devuelve lista de tematicas random segun la cantidad
    public static List<String> Temas(int cantidad) {
        List<String> lista = new ArrayList<>();

        String uri = "mongodb://root:alumnoalumno@localhost:27017/";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("AHORCADO_TEMATICA");
            MongoCollection<Document> coleccion = database.getCollection("tematicas");

            for (Document documento : coleccion.find()) {
                lista.add(documento.getString("nombre"));
            }

            Collections.shuffle(lista);
            return lista.subList(0, Math.min(cantidad, lista.size()));
        }
    }

    //Lo mismo q arriba pero con palabras y no con tematicas
    public static String palabraRandom(String tematica) {
        String uri = "mongodb://root:alumnoalumno@localhost:27017/";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("AHORCADO_TEMATICA");
            MongoCollection<Document> coleccion = database.getCollection("tematicas");

            // Buscar el documento con el nombre de la temática
            Document filtro = new Document("nombre", tematica);
            Document documento = coleccion.find(filtro).first();

            if (documento != null) {
                List<String> palabras = documento.getList("palabras", String.class);

                //Aleatorizar
                if (palabras != null && !palabras.isEmpty()) {
                    Collections.shuffle(palabras);
                    return palabras.get(0); 
                }
            }
        }
        return "sin_palabra"; 
    }

    //Devuelve todas las tematicas sin limitar cantidad
    public static List<String> obtenerTodas() {
        List<String> lista = new ArrayList<>();

        String uri = "mongodb://root:alumnoalumno@localhost:27017/";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("AHORCADO_TEMATICA");
            MongoCollection<Document> coleccion = database.getCollection("tematicas");

            for (Document documento : coleccion.find()) {
                lista.add(documento.getString("nombre"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
