package com.proyecto.mi_proyecto;

import java.awt.EventQueue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class F_PartidaPerdidaScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				F_PartidaPerdidaScreen frame = new F_PartidaPerdidaScreen();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public F_PartidaPerdidaScreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa
		setUndecorated(true); // Elimina la barra de título si quieres pantalla completamente limpia

		contentPane = new JPanel(new GridBagLayout());
		contentPane.setBackground(new Color(192, 192, 192));
		setContentPane(contentPane);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 15, 15, 15); // Espaciado entre componentes
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel lblTitulo = new JLabel("¡Has perdido!", SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Dialog", Font.BOLD, 32));
		lblTitulo.setForeground(Color.RED);
		gbc.gridy = 0;
		contentPane.add(lblTitulo, gbc);

		JButton btnVolverAJugar = new JButton("Volver a jugar");
		btnVolverAJugar.setFont(new Font("Dialog", Font.PLAIN, 18));
		btnVolverAJugar.addActionListener(e -> {
			D_SelectorTemasScreen selector = new D_SelectorTemasScreen();
			selector.setVisible(true);
			dispose();
		});
		gbc.gridy = 1;
		contentPane.add(btnVolverAJugar, gbc);

		JButton btnTienda = new JButton("Tienda");
		btnTienda.setFont(new Font("Dialog", Font.PLAIN, 18));
		gbc.gridy = 2;
		contentPane.add(btnTienda, gbc);

		JButton btnSalir = new JButton("Salir al menú");
		btnSalir.setFont(new Font("Dialog", Font.PLAIN, 18));
		btnSalir.addActionListener(e -> {
			C_MenuPrincipalScreen menu = new C_MenuPrincipalScreen();
			menu.setVisible(true);
			dispose();
		});
		gbc.gridy = 3;
		contentPane.add(btnSalir, gbc);
	}
}
