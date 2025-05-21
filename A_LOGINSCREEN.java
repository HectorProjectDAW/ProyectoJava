package com.proyecto.mi_proyecto;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class A_LoginScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JPasswordField passwordField;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				A_LoginScreen frame = new A_LoginScreen();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public A_LoginScreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height);
		setLocationRelativeTo(null);

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBackground(Color.LIGHT_GRAY);
		setContentPane(contentPane);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;

		JLabel lblLogin = new JLabel("LOGIN");
		lblLogin.setFont(new Font("Dialog", Font.BOLD, 26));
		lblLogin.setForeground(Color.BLACK);
		contentPane.add(lblLogin, gbc);

		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		JLabel lblUsuario = new JLabel("Nombre de usuario:");
		lblUsuario.setFont(new Font("Dialog", Font.PLAIN, 16));
		contentPane.add(lblUsuario, gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		textField = new JTextField(20);
		contentPane.add(textField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.EAST;
		JLabel lblContrasena = new JLabel("Contraseña:");
		lblContrasena.setFont(new Font("Dialog", Font.PLAIN, 16));
		contentPane.add(lblContrasena, gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		passwordField = new JPasswordField(20);
		contentPane.add(passwordField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		JButton btnContinuar = new JButton("Continuar");
		btnContinuar.addActionListener(this::loginAction);
		contentPane.add(btnContinuar, gbc);

		gbc.gridy = 4;
		JLabel lblNoTienesCuenta = new JLabel("¿No tienes cuenta?");
		contentPane.add(lblNoTienesCuenta, gbc);

		gbc.gridy = 5;
		JButton btnCrearCuenta = new JButton("Crear cuenta");
		btnCrearCuenta.addActionListener(e -> {
			B_RegisterScreen registerScreen = new B_RegisterScreen();
			registerScreen.setVisible(true);
			dispose();
		});
		contentPane.add(btnCrearCuenta, gbc);
	}

	private void loginAction(ActionEvent e) {
		String usuario = textField.getText();
		String contrasenya = new String(passwordField.getPassword());

		if (usuario.isEmpty() || contrasenya.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Complete todos los campos por favor");
			return;
		}

		if (!User.existeUsuario(usuario)) {
			JOptionPane.showMessageDialog(this, "Usuario no encontrado");
			return;
		}

		if (User.validarCredenciales(usuario, contrasenya)) {
			JOptionPane.showMessageDialog(this, "Inicio de sesión correcto");
			C_MenuPrincipalScreen menu = new C_MenuPrincipalScreen();
			menu.setVisible(true);
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "Contraseña incorrecta");
		}
	}
}
