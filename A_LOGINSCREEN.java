package com.proyecto.mi_proyecto;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

	
	//Manera de chatgpt para que quede bien y centrado
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
		
		gbc.gridy = 6;
		JButton btnInvitado = new JButton("Continuar sin iniciar sesión");
		btnInvitado.addActionListener(e -> {
		    JOptionPane.showMessageDialog(this, "Has iniciado como invitado. No se guardarán datos.");
		    C_MenuPrincipalScreen menu = new C_MenuPrincipalScreen("Invitado");
		    menu.setVisible(true);
		    dispose();
		});
		contentPane.add(btnInvitado, gbc);


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
	
	
	//Se conecta a la BDD de Login, hace un select y mira si el id del user
	private long obtenerIdUsuario(String usuario) {
	    long id = -1;
	    try (Connection conexion = DriverManager.getConnection(
	            "jdbc:mysql://localhost:33306/Login", "root", "alumnoalumno")) {

	        String query = "SELECT idusuario FROM Usuarios WHERE nombre_usuario = ?";
	        try (PreparedStatement ps = conexion.prepareStatement(query)) {
	            ps.setString(1, usuario);
	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    id = rs.getLong("idusuario"); 
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return id;
	}


	
	//Metodo login
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
	        long idUsuario = obtenerIdUsuario(usuario);
	        if (idUsuario != -1) {
	            SesionUsuario.setIdUsuario(idUsuario);
	            User.iniciarSesion(usuario);
	        }

	        JOptionPane.showMessageDialog(this, "Inicio de sesión correcto");
	        C_MenuPrincipalScreen menu = new C_MenuPrincipalScreen(usuario);
	        menu.setVisible(true);
	        dispose();
	    } else {
	        JOptionPane.showMessageDialog(this, "Contraseña incorrecta");
	    }
	}


}
