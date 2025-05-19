package com.proyecto.mi_proyecto;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class E_JuegoScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel labelPalabra;
	private JTextField inputLetra;
	private JLabel labelErrores;
	private JLabel labelTematica;
	private DibujoAhorcado panelDibujo;

	private char[] letrasPalabra;
	private char[] estadoActual;
	private int errores = 0;
	private String palabra;
	private String tematica;
	private java.util.List<Character> letrasFallidas = new java.util.ArrayList<>();
	private JLabel labelFallidas;


	public E_JuegoScreen(String tematica) {
		this.tematica = tematica;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		labelTematica = new JLabel("Temática: " + tematica);
		labelTematica.setFont(new Font("Dialog", Font.BOLD, 20));
		labelTematica.setBounds(179, 0, 300, 30);
		contentPane.add(labelTematica);

		JLabel lblTitulo = new JLabel("Adivina la palabra:");
		lblTitulo.setBounds(30, 42, 200, 20);
		contentPane.add(lblTitulo);

		labelPalabra = new JLabel("");
		labelPalabra.setFont(new Font("Monospaced", Font.BOLD, 18));
		labelPalabra.setBounds(30, 70, 500, 30);
		contentPane.add(labelPalabra);

		JLabel lblIntroduceLetra = new JLabel("Introduce una letra:");
		lblIntroduceLetra.setBounds(30, 120, 200, 20);
		contentPane.add(lblIntroduceLetra);

		inputLetra = new JTextField();
		inputLetra.setBounds(180, 120, 50, 25);
		contentPane.add(inputLetra);
		inputLetra.setColumns(1);

		JButton btnProbar = new JButton("Probar");
		btnProbar.setBounds(250, 120, 100, 25);
		contentPane.add(btnProbar);

		labelErrores = new JLabel("Errores: 0");
		labelErrores.setBounds(30, 160, 200, 20);
		contentPane.add(labelErrores);
		labelFallidas = new JLabel("Letras fallidas: ");
		labelFallidas.setBounds(30, 190, 500, 20);
		contentPane.add(labelFallidas);


		panelDibujo = new DibujoAhorcado();
		panelDibujo.setBounds(350, 40, 200, 250);
		contentPane.add(panelDibujo);

		// Obtener palabra aleatoria desde MongoDB con la temática seleccionada
		palabra = TematicaMongo.palabraRandom(tematica).toUpperCase();
		letrasPalabra = palabra.toCharArray();
		estadoActual = new char[letrasPalabra.length];

		//Recorre estadoActual y comprueba si es una letra
		for (int i = 0; i < estadoActual.length; i++) {
			if (Character.isLetter(letrasPalabra[i])) {
				estadoActual[i] = '_';
			} else {
				estadoActual[i] = letrasPalabra[i];
			}
		}

		actualizarLabel();

		btnProbar.addActionListener(e -> {
			if (errores >= 6 || String.valueOf(estadoActual).equals(String.valueOf(letrasPalabra))) {
				return;
			}

			//Solo una letra
			String texto = inputLetra.getText().toUpperCase();
			if (texto.length() != 1 || !Character.isLetter(texto.charAt(0))) {
				JOptionPane.showMessageDialog(this, "Introduce una sola letra valida.");
				return;
			}
			
			//Al poner la letra, se borra el hueco donde escribes
			char letra = texto.charAt(0);
			inputLetra.setText("");

			//Mira si la letra está en la palabra del MongoDB y si está, se pone
			boolean acierto = false;
			for (int i = 0; i < letrasPalabra.length; i++) {
				if (letrasPalabra[i] == letra && estadoActual[i] == '_') {
					estadoActual[i] = letra;
					acierto = true;
				}
			}

			//Si la palabra no está, se guarda en letrasFallidas
			if (!acierto) {
				if (!letrasFallidas.contains(letra)) {
					letrasFallidas.add(letra);
				}
				
				//Se acumulan los errores y se dibuja (quitar despues y poner imagenes)
				errores++;
				labelErrores.setText("Errores: " + errores);
				labelFallidas.setText("Letras fallidas: " + letrasFallidas.toString().replaceAll("[\\[\\],]", ""));
				panelDibujo.repaint();

				//Perder si tienes mas de 6
				if (errores >= 6) {
					JOptionPane.showMessageDialog(this, "Has perdido. La palabra era: " + palabra);
					dispose();
					F_PartidaPerdidaScreen pantallaPerdida = new F_PartidaPerdidaScreen();
					pantallaPerdida.setVisible(true);
				}

			}

			//Ganas y pone esto
			else if (String.valueOf(estadoActual).equals(String.valueOf(letrasPalabra))) {
				JOptionPane.showMessageDialog(this, "Has adivinado la palabra: " + palabra + "!");
			}

			actualizarLabel();
		});
	}

	//Convertir Array en texto con espacios
	private void actualizarLabel() {
		StringBuilder cosa = new StringBuilder();
		for (char c : estadoActual) {
			cosa.append(c).append(" ");
		}
		labelPalabra.setText(cosa.toString().trim());
	}

	// Clase interna para dibujar el ahorcado
	private class DibujoAhorcado extends JPanel {
		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			dibujarAhorcado(g, errores);
		}

		private void dibujarAhorcado(Graphics g, int fallos) {
			g.setColor(Color.BLACK);
			// base
			g.drawLine(20, 180, 120, 180); // Suelo
			g.drawLine(70, 180, 70, 20); // Palitroque
			g.drawLine(70, 20, 150, 20); // Otro palitroque
			g.drawLine(150, 20, 150, 50); // Cuerda
			
			// Cuerpo
			
			if (fallos > 0) g.drawOval(130, 50, 40, 40); // Cabeza
			if (fallos > 1) g.drawLine(150, 90, 150, 140); // Pechito
			if (fallos > 2) g.drawLine(150, 100, 120, 120); // Brazo izq
			if (fallos > 3) g.drawLine(150, 100, 180, 120); // Brazo der
			if (fallos > 4) g.drawLine(150, 140, 130, 170); // Pierna izq
			if (fallos > 5) g.drawLine(150, 140, 170, 170); // Pierna der
		}
	}
}
