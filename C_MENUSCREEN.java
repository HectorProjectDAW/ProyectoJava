package com.proyecto.mi_proyecto;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;

public class C_MenuPrincipalScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private String usuarioActual;

	
	public C_MenuPrincipalScreen(String usuarioActual) {
		this.usuarioActual = usuarioActual;

		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Menú Principal");

		contentPane = new JPanel(new GridBagLayout());
		contentPane.setBackground(new Color(230, 230, 250));
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		setContentPane(contentPane);

		Font fuenteBoton = new Font("SansSerif", Font.BOLD, 18);

		JLabel titulo = new JLabel("Menú Principal");
		titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
		titulo.setForeground(new Color(60, 60, 60));
		GridBagConstraints gbcTitulo = new GridBagConstraints();
		gbcTitulo.gridx = 0;
		gbcTitulo.gridy = 0;
		gbcTitulo.insets = new Insets(20, 0, 30, 0);
		contentPane.add(titulo, gbcTitulo);

		JPanel panelBotones = new JPanel(new GridLayout(0, 1, 20, 20));
		panelBotones.setBackground(new Color(255, 255, 255));
		panelBotones.setBorder(BorderFactory.createEmptyBorder(40, 150, 40, 150));

		JButton btnPartidaNueva = crearBoton("Nueva partida", fuenteBoton, e -> {
			D_SelectorTemasScreen selecTema = new D_SelectorTemasScreen(usuarioActual);
			selecTema.setVisible(true);
			dispose();
		});
		panelBotones.add(btnPartidaNueva);

		JButton btnContinuarPartida = crearBoton("Continuar partida", fuenteBoton, e -> {
			continuarPartidaGuardada();
		});
		panelBotones.add(btnContinuarPartida);

		
		
		JButton btnDesafio = crearBoton("Desafío", fuenteBoton, e -> {
		    PantallaConexion pantallaConexion = new PantallaConexion();
		    pantallaConexion.setVisible(true);
		    dispose();
		});

		panelBotones.add(btnDesafio);

		
		
		JButton btnRanking = crearBoton("Ranking", fuenteBoton, e -> { 
			H_RankingScreen rankingScreen = new H_RankingScreen();
		rankingScreen.setVisible(true);});
			
	
		panelBotones.add(btnRanking);

		GridBagConstraints gbcBotones = new GridBagConstraints();
		gbcBotones.gridx = 0;
		gbcBotones.gridy = 1;
		gbcBotones.anchor = GridBagConstraints.CENTER;
		contentPane.add(panelBotones, gbcBotones);

		JPanel panelInferior = new JPanel(new BorderLayout(20, 0));
		panelInferior.setBackground(new Color(255, 255, 255));
		panelInferior.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));

		JButton btnDeslogearte = crearBoton("Cerrar sesión", fuenteBoton, e -> {
			A_LoginScreen login = new A_LoginScreen();
			login.setVisible(true);
			dispose();
		});
		panelInferior.add(btnDeslogearte, BorderLayout.WEST);

		JButton btnSalir = crearBoton("Salir al escritorio", fuenteBoton, e -> {
			System.exit(0);
		});
		panelInferior.add(btnSalir, BorderLayout.EAST);

		GridBagConstraints gbcInferior = new GridBagConstraints();
		gbcInferior.gridx = 0;
		gbcInferior.gridy = 2;
		gbcInferior.insets = new Insets(40, 0, 0, 0);
		contentPane.add(panelInferior, gbcInferior);
	}

	private JButton crearBoton(String texto, Font fuente, ActionListener accion) {
		JButton boton = new JButton(texto);
		boton.setFont(fuente);
		boton.setBackground(new Color(100, 149, 237));
		boton.setForeground(Color.WHITE);
		boton.setFocusPainted(false);
		boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		boton.addActionListener(accion);
		return boton;
	}

	private void continuarPartidaGuardada() {
		try {
			Partida partidaGuardada = PartidaDAO.cargarPartida(usuarioActual);
			if (partidaGuardada == null) {
				JOptionPane.showMessageDialog(this, "No hay partida guardada para el usuario.", "Información", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			E_JuegoScreen juego = new E_JuegoScreen(partidaGuardada);
			juego.setVisible(true);
			dispose();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error al cargar la partida guardada: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}
