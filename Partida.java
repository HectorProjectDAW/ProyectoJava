package com.proyecto.mi_proyecto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class Partida {
    protected String tematica;
    protected String palabra;
    protected String user;
    protected List<Character> aciertos;
    protected List<Character> letrasFallidas;
    protected int racha;

    public Partida(String tematica, String palabra, String user, List<Character> aciertos, List<Character> letrasFallidas, int racha) {
        if (tematica == null || palabra == null || user == null) {
            throw new IllegalArgumentException("Tematica, palabra y usuario no pueden ser null");
        }
        this.tematica = tematica;
        this.palabra = palabra;
        this.user = user;
        this.aciertos = (aciertos != null) ? new ArrayList<>(aciertos) : new ArrayList<>();
        this.letrasFallidas = (letrasFallidas != null) ? new ArrayList<>(letrasFallidas) : new ArrayList<>();
        this.racha = racha;
    }

    public String getTematica() {
        return tematica;
    }

    public String getPalabra() {
        return palabra;
    }

    public String getUser() {
        return user;
    }

    
    public List<Character> getAciertos() {
        return new ArrayList<>(aciertos);
    }

    public List<Character> getLetrasFallidas() {
        return new ArrayList<>(letrasFallidas);
    }

    public int getRacha() {
        return racha;
    }

    protected void setTematica(String tematica) {
        this.tematica = tematica;
    }

    protected void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public void setUser(String user) {
        this.user = user;
    }

    protected void setAciertos(List<Character> aciertos) {
        this.aciertos = new ArrayList<>(aciertos);
    }

    protected void setLetrasFallidas(List<Character> letrasFallidas) {
        this.letrasFallidas = new ArrayList<>(letrasFallidas);
    }

    public void setRacha(int racha) {
        this.racha = racha;
    }
    public String quitarAcentos(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(texto).replaceAll("");
    }
    @Override
    public String toString() {
        return "Partida{" +
            "tematica='" + tematica + '\'' +
            ", palabra='" + palabra + '\'' +
            ", user='" + user + '\'' +
            ", aciertos=" + aciertos +
            ", letrasFallidas=" + letrasFallidas +
            ", racha=" + racha +
            '}';
    }

    public void intentarLetra(char letra) {
        letra = Character.toLowerCase(letra);
        char letraNormalizada = quitarAcentos(String.valueOf(letra)).charAt(0);
        String palabraNormalizada = quitarAcentos(palabra.toLowerCase());
        

        if (palabraNormalizada.indexOf(letraNormalizada) >= 0) {
            if (!aciertos.contains(letraNormalizada)) {
                aciertos.add(letraNormalizada);
                racha++;
            }
        } else {
            if (!letrasFallidas.contains(letraNormalizada)) {
                letrasFallidas.add(letraNormalizada);
                racha = 0;
            }
        }
    }

    public boolean palabraCompleta() {
        String palabraNormalizada = quitarAcentos(palabra.toLowerCase());
        for (char c : palabraNormalizada.toCharArray()) {
            if (Character.isLetter(c) && !aciertos.contains(c)) {
                return false;
            }
        }
        return true;
    }


    public String mostrarEstadoPalabra() {
        StringBuilder estado = new StringBuilder();
        String palabraNormalizada = quitarAcentos(palabra.toLowerCase());
        for (char c : palabraNormalizada.toCharArray()) {
            if (aciertos.contains(Character.toLowerCase(c))) {
                estado.append(c);
            } else {
                estado.append('_');
            }
            estado.append(' ');
        }
        return estado.toString().trim();
    }

    public List<Character> getLetrasAdivinadas() {
        return new ArrayList<>(aciertos);
    }

    public String letrasFallidasComoString() {
        return letrasFallidas.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(" "));
    }
    
}
