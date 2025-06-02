package com.proyecto.mi_proyecto;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class F_PartidaPerdidaScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String usuarioActual;

	public F_PartidaPerdidaScreen(String usuarioActual) {
		this.usuarioActual = usuarioActual;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);

		contentPane = new JPanel(new GridBagLayout());
		contentPane.setBackground(new Color(192, 192, 192));
		setContentPane(contentPane);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 15, 15, 15);
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel lblTitulo = new JLabel("¡Has perdido!", SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Dialog", Font.BOLD, 32));
		lblTitulo.setForeground(Color.RED);
		gbc.gridy = 0;
		contentPane.add(lblTitulo, gbc);

		
		JButton btnVolverAJugar = new JButton("Seleccionar Tema");
		btnVolverAJugar.setFont(new Font("Dialog", Font.PLAIN, 18));
		btnVolverAJugar.addActionListener(e -> {
			D_SelectorTemasScreen selector = new D_SelectorTemasScreen(usuarioActual); 
			selector.setVisible(true);
			dispose();
		});
		gbc.gridy = 1;
		contentPane.add(btnVolverAJugar, gbc);

		
		JButton btnTienda = new JButton("Ir al menú principal");
		btnTienda.setFont(new Font("Dialog", Font.PLAIN, 18));
		btnTienda.addActionListener(e -> {
			C_MenuPrincipalScreen menu = new C_MenuPrincipalScreen(usuarioActual); 
			menu.setVisible(true);
			dispose();
		});
		gbc.gridy = 2;
		contentPane.add(btnTienda, gbc);

		
		JButton btnSalir = new JButton("Salir de la app");
		btnSalir.setFont(new Font("Dialog", Font.PLAIN, 18));
		btnSalir.addActionListener(e -> {
			System.exit(0); 
		});
		gbc.gridy = 3;
		contentPane.add(btnSalir, gbc);
	}
}
