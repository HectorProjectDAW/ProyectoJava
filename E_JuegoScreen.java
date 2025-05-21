package com.proyecto.mi_proyecto;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class E_JuegoScreen extends JFrame {

	protected String tematica;
	protected Partida partida;
	protected int rachaPalabras = 0;

	protected JLabel labelTematica, labelPalabra, labelErrores, labelFallidas, labelRacha;
	protected JTextField textFieldIntento;
	protected JPanel panelDibujo;

	public E_JuegoScreen(String tematicaSeleccionada) {
		this.tematica = tematicaSeleccionada;
		setTitle("Ahorcado");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		getContentPane().setBackground(new Color(245, 245, 245));
		setLayout(new BorderLayout());

		JButton botonCerrar = new JButton("X");
		botonCerrar.setFont(new Font("Arial", Font.BOLD, 18));
		botonCerrar.setForeground(Color.RED);
		botonCerrar.setFocusPainted(false);
		botonCerrar.addActionListener(e -> dispose());

		JPanel panelCerrar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelCerrar.setBackground(new Color(245, 245, 245));
		panelCerrar.add(botonCerrar);
		add(panelCerrar, BorderLayout.NORTH);

		JPanel panelSuperior = new JPanel();
		panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
		panelSuperior.setBackground(new Color(230, 230, 250));
		panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

		labelTematica = new JLabel("Temática: " + tematica, SwingConstants.CENTER);
		labelTematica.setFont(new Font("Arial", Font.BOLD, 30));
		labelTematica.setAlignmentX(Component.CENTER_ALIGNMENT);

		labelPalabra = new JLabel("", SwingConstants.CENTER);
		labelPalabra.setFont(new Font("Courier New", Font.BOLD, 40));
		labelPalabra.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel panelInfo = new JPanel(new GridLayout(1, 3, 20, 0));
		panelInfo.setMaximumSize(new Dimension(800, 40));
		panelInfo.setBackground(new Color(230, 230, 250));

		labelErrores = new JLabel("Errores: 0", SwingConstants.CENTER);
		labelFallidas = new JLabel("Letras fallidas: ", SwingConstants.CENTER);
		labelRacha = new JLabel("Racha: 0", SwingConstants.CENTER);
		labelErrores.setFont(new Font("Arial", Font.PLAIN, 20));
		labelFallidas.setFont(new Font("Arial", Font.PLAIN, 20));
		labelRacha.setFont(new Font("Arial", Font.PLAIN, 20));

		panelInfo.add(labelErrores);
		panelInfo.add(labelFallidas);
		panelInfo.add(labelRacha);

		panelSuperior.add(labelTematica);
		panelSuperior.add(Box.createVerticalStrut(20));
		panelSuperior.add(labelPalabra);
		panelSuperior.add(Box.createVerticalStrut(20));
		panelSuperior.add(panelInfo);

		add(panelSuperior, BorderLayout.PAGE_START);

		panelDibujo = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				dibujarAhorcado(g, partida != null ? partida.getLetrasFallidas().size() : 0);
			}
		};
		panelDibujo.setBackground(Color.WHITE);
		add(panelDibujo, BorderLayout.CENTER);

		JPanel panelInferior = new JPanel();
		panelInferior.setBackground(new Color(230, 230, 250));
		panelInferior.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

		JLabel labelIntento = new JLabel("Letra o palabra:");
		labelIntento.setFont(new Font("Arial", Font.PLAIN, 20));
		textFieldIntento = new JTextField(20);
		textFieldIntento.setFont(new Font("Arial", Font.PLAIN, 24));

		JButton btnProbar = new JButton("Probar");
		btnProbar.setFont(new Font("Arial", Font.BOLD, 20));
		btnProbar.addActionListener(e -> manejarIntento());

		panelInferior.add(labelIntento);
		panelInferior.add(textFieldIntento);
		panelInferior.add(btnProbar);

		add(panelInferior, BorderLayout.SOUTH);

		cargarNuevaPalabra();
	}

	protected void manejarIntento() {
		String intento = textFieldIntento.getText().trim().toLowerCase();
		textFieldIntento.setText("");

		if (intento.isEmpty()) return;

		if (intento.length() > 1) {
			if (intento.equals(partida.getPalabra().toLowerCase())) {
				JOptionPane.showMessageDialog(this, "¡Adivinado!");
				rachaPalabras++;
				labelRacha.setText("Racha: " + rachaPalabras);
				if (rachaPalabras == 5) {
					cambiarTematica();
				} else {
					cargarNuevaPalabra();
				}
			} else {
				JOptionPane.showMessageDialog(this, "Palabra incorrecta. Has perdido.\nLa palabra era: " + partida.getPalabra());
				dispose();
				new F_PartidaPerdidaScreen().setVisible(true);
			}
		} else {
			char letra = intento.charAt(0);
			int erroresAntes = partida.getLetrasFallidas().size();

			partida.intentarLetra(letra);
			actualizarLabel();

			int erroresActuales = partida.getLetrasFallidas().size();
			if (erroresActuales > erroresAntes) {
				labelErrores.setText("Errores: " + erroresActuales);
				labelFallidas.setText("Letras fallidas: " + partida.getLetrasFallidas().toString().replaceAll("[\\[\\],]", ""));
				panelDibujo.repaint();
				if (erroresActuales >= 6) {
					JOptionPane.showMessageDialog(this, "Has perdido. La palabra era: " + partida.getPalabra());
					dispose();
					new F_PartidaPerdidaScreen().setVisible(true);
				}
			} else {
				if (partida.palabraCompleta()) {
					JOptionPane.showMessageDialog(this, "¡Adivinado!");
					rachaPalabras++;
					labelRacha.setText("Racha: " + rachaPalabras);
					if (rachaPalabras == 5) {
						cambiarTematica();
					} else {
						cargarNuevaPalabra();
					}
				}
			}
		}
	}

	protected void cargarNuevaPalabra() {
		String nuevaPalabra = TematicaMongo.palabraRandom(tematica);
		if (nuevaPalabra == null || nuevaPalabra.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No hay palabras para la temática: " + tematica);
			dispose();
			return;
		}

		partida = new Partida(tematica, nuevaPalabra, "Usuario", new ArrayList<>(), new ArrayList<>(), 0);
		panelDibujo.repaint();
		actualizarLabel();
		labelErrores.setText("Errores: 0");
		labelFallidas.setText("Letras fallidas: ");
		labelRacha.setText("Racha: " + rachaPalabras);
	}

	protected void actualizarLabel() {
		String palabra = partida.getPalabra().toLowerCase();
		List<Character> letrasAdivinadas = partida.getLetrasAdivinadas();

		StringBuilder estado = new StringBuilder();
		for (int i = 0; i < palabra.length(); i++) {
			char c = palabra.charAt(i);
			if (!Character.isLetter(c)) {
				estado.append(c);
			} else if (letrasAdivinadas.contains(c)) {
				estado.append(Character.toUpperCase(c));
			} else {
				estado.append('_');
			}
			estado.append(' ');
		}
		labelPalabra.setText(estado.toString());
	}

	protected void cambiarTematica() {
		List<String> opciones = new ArrayList<>(TematicaMongo.obtenerTodas());
		opciones.remove(tematica);
		if (opciones.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No hay más temáticas disponibles.");
			dispose();
			return;
		}

		tematica = opciones.get(new Random().nextInt(opciones.size()));
		JOptionPane.showMessageDialog(this, "¡Has acertado 5 palabras! Nueva temática: " + tematica);
		rachaPalabras = 0;
		labelTematica.setText("Temática: " + tematica);
		labelRacha.setText("Racha: 0");
		cargarNuevaPalabra();
	}

	protected void dibujarAhorcado(Graphics g, int errores) {
		g.drawLine(50, 450, 150, 450);
		g.drawLine(100, 450, 100, 100);
		g.drawLine(100, 100, 300, 100);
		g.drawLine(300, 100, 300, 140);

		if (errores > 0) g.drawOval(275, 140, 50, 50);
		if (errores > 1) g.drawLine(300, 190, 300, 290);
		if (errores > 2) g.drawLine(300, 200, 260, 240);
		if (errores > 3) g.drawLine(300, 200, 340, 240);
		if (errores > 4) g.drawLine(300, 290, 260, 340);
		if (errores > 5) g.drawLine(300, 290, 340, 340);
	}
}
