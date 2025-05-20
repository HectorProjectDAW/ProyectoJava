package com.proyecto.mi_proyecto;

import java.util.List;

public class Partida {

	protected String tematica;
	protected String palabra;
	protected char[] estadoActual;
	protected int errores;
	protected List<Character> letrasFallidas;
	protected int racha;

	public Partida(String tematica, String palabra, char[] estadoActual, int errores, List<Character> letrasFallidas, int racha) {
		this.tematica = tematica;
		this.palabra = palabra;
		this.estadoActual = estadoActual;
		this.errores = errores;
		this.letrasFallidas = letrasFallidas;
		this.racha = racha;
	}

	protected String getTematica() {
		return tematica;
	}

	protected String getPalabra() {
		return palabra;
	}

	protected char[] getEstadoActual() {
		return estadoActual;
	}

	protected int getErrores() {
		return errores;
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

	protected void setEstadoActual(char[] estadoActual) {
		this.estadoActual = estadoActual;
	}

	protected void setErrores(int errores) {
		this.errores = errores;
	}

	protected void setLetrasFallidas(List<Character> letrasFallidas) {
		this.letrasFallidas = letrasFallidas;
	}

	protected void setRacha(int racha) {
		this.racha = racha;
	}
}
