package com.proyecto.mi_proyecto;

import com.mongodb.client.*;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Collections;

public class TematicaMongo {

    public static List<String> Temas(int cantidad) {
        List<String> lista = new ArrayList<>();

       
        String uri = "mongodb://root:alumnoalumno@localhost:27017";

       
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
}
