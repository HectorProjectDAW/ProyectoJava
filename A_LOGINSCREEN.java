package com.proyecto.mi_proyecto;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;


public class A_LoginScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					A_LoginScreen frame = new A_LoginScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public A_LoginScreen() {
		setBackground(SystemColor.controlDkShadow);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCorreoElectrnico = new JLabel("Correo electrónico:");
		lblCorreoElectrnico.setForeground(new Color(0, 0, 0));
		lblCorreoElectrnico.setBounds(55, 67, 120, 17);
		contentPane.add(lblCorreoElectrnico);
		
		textField = new JTextField();
		textField.setBounds(193, 65, 201, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblContrasea = new JLabel("Contraseña:");
		lblContrasea.setForeground(new Color(0, 0, 0));
		lblContrasea.setBounds(55, 96, 120, 17);
		contentPane.add(lblContrasea);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(193, 94, 201, 21);
		contentPane.add(passwordField);
		
		JLabel lblnoTienesCuenta = new JLabel("¿No tienes cuenta?");
		lblnoTienesCuenta.setForeground(new Color(0, 0, 0));
		lblnoTienesCuenta.setBounds(72, 233, 120, 17);
		contentPane.add(lblnoTienesCuenta);
		
		JButton btnCrearCuenta = new JButton("Crear cuenta");
		btnCrearCuenta.setForeground(new Color(0, 0, 0));
		btnCrearCuenta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				B_RegisterScreen registerScreen = new B_RegisterScreen();
				registerScreen.setVisible(true);
				dispose();
			}
		});
		btnCrearCuenta.setBounds(210, 228, 141, 27);
		contentPane.add(btnCrearCuenta);
		
		JLabel lblhasOlvidadoTu = new JLabel("¿Has olvidado tu contraseña?");
		lblhasOlvidadoTu.setForeground(new Color(0, 153, 255));
		lblhasOlvidadoTu.setBounds(23, 180, 183, 17);
		contentPane.add(lblhasOlvidadoTu);
		
		JButton btnRecuperarContrasea = new JButton("Recuperar contraseña");
		btnRecuperarContrasea.setForeground(new Color(0, 0, 0));
		btnRecuperarContrasea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnRecuperarContrasea.setBounds(210, 175, 164, 27);
		contentPane.add(btnRecuperarContrasea);
		
		JLabel lblLogin = new JLabel("LOGIN");
		lblLogin.setForeground(new Color(0, 0, 0));
		lblLogin.setFont(new Font("Dialog", Font.BOLD, 16));
		lblLogin.setBounds(179, 12, 60, 17);
		contentPane.add(lblLogin);
		
		JButton btnContinuar = new JButton("Continuar");
		btnContinuar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Obtenemos correo y contraseña
				String correo = textField.getText();
				 String contrasenya = new String(passwordField.getPassword());
				 
				 //Si hay correo o contraseña en blanco sale Error
				 if (correo .isEmpty() || contrasenya.isEmpty()) {
					 JOptionPane.showMessageDialog(null, "Complete todos los campos porfavor");
					 return;
				 }
				 String hashedPassword = hashPassword(contrasenya);
				 
				 //Intenta conexión con la BDD local
				 try (Connection conexion = DriverManager.getConnection(
							"jdbc:mysql://localhost:33306/Login", "root", "alumnoalumno")) {

					    //Hace Select para ver si el correo está en la BDD. Si la contraseña tmb es la misma, inicia sesión
						String checkQuery = "SELECT * FROM Usuarios WHERE correo = ?";
						try (PreparedStatement statement = conexion.prepareStatement(checkQuery)) {
							statement.setString(1, correo);
							try (ResultSet rs = statement.executeQuery()) {
								if (rs.next()) {
									
									String contrasenyaBD = rs.getString("contrasenya");
									if (hashedPassword.equals(contrasenyaBD)) {
										JOptionPane.showMessageDialog(null, "¡Inicio de sesión exitoso!");
								
									} else {
										JOptionPane.showMessageDialog(null, "Contraseña incorrecta.");
										return;
									}
								} else {
									JOptionPane.showMessageDialog(null, "Usuario no encontrado.");
									return;
								}
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "Error al iniciar sesión.");
					}
				
				 
				C_MenuPrincipalScreen menu = new C_MenuPrincipalScreen();
				menu.setVisible(true);
				dispose();
			}
		});
		btnContinuar.setForeground(new Color(0, 0, 0));
		btnContinuar.setBackground(new Color(204, 204, 204));
		btnContinuar.setBounds(160, 130, 120, 27);
		contentPane.add(btnContinuar);
	}
	private String hashPassword(String password) {
	    try {
	        // Crear el objeto con el algoritmo SHA-256
	        MessageDigest md = MessageDigest.getInstance("SHA-256");

	        // Generar el hash de la contraseña
	        byte[] hashBytes = md.digest(password.getBytes());

	        // Convertir el array de bytes a una cadena hexadecimal
	        StringBuilder hexString = new StringBuilder();
	        for (byte b : hashBytes) {
	            hexString.append(String.format("%02x", b));
	        }

	        return hexString.toString();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}
