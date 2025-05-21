package com.proyecto.mi_proyecto;
import java.util.List;

public class Partida {
	protected String tematica;
	protected String palabra;
	protected String user;
	protected List<Character> aciertos;
	protected List<Character> letrasFallidas;
	protected int racha;
	public Partida(String tematica, String palabra,String user, List<Character> aciertos, List<Character> letrasFallidas, int racha) {
		this.tematica = tematica;
		this.palabra = palabra;
		this.user= user;
		this.aciertos=aciertos;
		this.letrasFallidas = letrasFallidas;
		this.racha = racha;
	}

	protected String getTematica() {
		return tematica;
	}

	protected String getPalabra() {
		return palabra;
	}
	protected String getUser() {
		return user;
	}
	protected List<Character> getAciertos() {
		return aciertos;
	}

	protected List<Character> getLetrasFallidas() {
		return letrasFallidas;
	}

	protected int getRacha() {
		return racha;
	}

	protected void setTematica(String tematica) {
		this.tematica = tematica;
	}

	protected void setPalabra(String palabra) {
		this.palabra = palabra;
	}
	protected void setUser(String user) {
		this.user = user;
	}
	protected void setAciertos(List<Character> aciertos) {
		this.aciertos = aciertos;
	}

	protected void setLetrasFallidas(List<Character> letrasFallidas) {
		this.letrasFallidas = letrasFallidas;
	}

	protected void setRacha(int racha) {
		this.racha = racha;
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
	    String minusucula = palabra.toLowerCase();

	    if (minusucula.indexOf(letra) >= 0) {
	        if (!aciertos.contains(letra)) {
	            aciertos.add(letra);
	            racha++;
	        }
	    } else {
	        if (!letrasFallidas.contains(letra)) {
	            letrasFallidas.add(letra);
	            racha = 0;
	        }
	    }
	}
	public boolean palabraCompleta() {
	    for (char c : palabra.toLowerCase().toCharArray()) {
	        if (!aciertos.contains(c)) {
	            return false;
	        }
	    }
	    return true;
	}
	public String mostrarEstadoPalabra() {
	    StringBuilder estado = new StringBuilder();
	    for (char c : palabra.toCharArray()) {
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
	    return aciertos;
	}


}
