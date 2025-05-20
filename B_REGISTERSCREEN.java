package com.proyecto.mi_proyecto;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class B_RegisterScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField correoField;
	private JPasswordField passwordField;
	private JPasswordField repeat_passwordField;
	private JTextField nombreUsuarioField; //nuevo campo para nombre_usuario

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					B_RegisterScreen frame = new B_RegisterScreen();
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
	public B_RegisterScreen() {
		setBackground(Color.DARK_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblRegistro = new JLabel("REGISTRO");
		lblRegistro.setForeground(new Color(0, 0, 0));
		lblRegistro.setFont(new Font("Dialog", Font.BOLD, 16));
		lblRegistro.setBounds(172, 12, 83, 23);
		contentPane.add(lblRegistro);

		//label y campo para nombre de usuario
		JLabel lblNombreUsuario = new JLabel("Nombre de usuario:");
		lblNombreUsuario.setForeground(Color.BLACK);
		lblNombreUsuario.setBounds(12, 49, 140, 17);
		contentPane.add(lblNombreUsuario);

		nombreUsuarioField = new JTextField();
		nombreUsuarioField.setBounds(172, 47, 212, 21);
		contentPane.add(nombreUsuarioField);
		nombreUsuarioField.setColumns(10);

		JLabel lblCorreoElectrnico = new JLabel("Correo electrónico:");
		lblCorreoElectrnico.setForeground(Color.BLACK);
		lblCorreoElectrnico.setBounds(12, 78, 122, 17);
		contentPane.add(lblCorreoElectrnico);

		JLabel lblContrasea = new JLabel("Contraseña:");
		lblContrasea.setForeground(Color.BLACK);
		lblContrasea.setBounds(12, 115, 122, 17);
		contentPane.add(lblContrasea);

		JLabel lblRepetirContrasea = new JLabel("Repetir contraseña:");
		lblRepetirContrasea.setForeground(Color.BLACK);
		lblRepetirContrasea.setBounds(12, 144, 122, 17);
		contentPane.add(lblRepetirContrasea);

		correoField = new JTextField();
		correoField.setBounds(172, 76, 212, 21);
		contentPane.add(correoField);
		correoField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(172, 109, 212, 21);
		contentPane.add(passwordField);

		repeat_passwordField = new JPasswordField();
		repeat_passwordField.setBounds(172, 142, 212, 21);
		contentPane.add(repeat_passwordField);

		JButton btnRegistrarse = new JButton("Registrarse");
		btnRegistrarse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				//Creo los Strings para que se guarde correo y las passwords y nombre_usuario
				String correo = correoField.getText().trim();
				String pass1 = new String(passwordField.getPassword());
				String pass2 = new String(repeat_passwordField.getPassword());
				String nombreUsuario = nombreUsuarioField.getText().trim();

				//Valido que sean iguales, si no, salta un pop-up de Error
				if (!pass1.equals(pass2)) {
					JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				//Valido que contenga un @ y un .
				if (!correo.contains("@") || !correo.contains(".")) {
					JOptionPane.showMessageDialog(null, "El correo electrónico no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				//Me guardo en variable el @ y el . y sus posiciones. Miro que el @ esté antes que el .
				int arroba = correo.indexOf('@');
				int punto = correo.lastIndexOf('.');

				if (arroba <= 0 || punto <= arroba + 1 || punto == correo.length() - 1) {
					JOptionPane.showMessageDialog(null, "El correo electrónico no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				//Valido que todos los campos estén rellenados
				if (correo.isEmpty() || pass1.isEmpty() || pass2.isEmpty() || nombreUsuario.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Por favor, rellena todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				//Valido que la contraseña tenga mínimo 6 carácteres
				if (pass1.length() < 6) {
					JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 6 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// Creamos la hashedPassword como la que ha creado el user
				 String hashedPassword = hashPassword(pass1);

				//Entro a la conexión de la BDD local
				try (Connection conexion = DriverManager.getConnection(
						"jdbc:mysql://localhost:33306/Login", "root", "alumnoalumno")) {

					//Miro si hay un correo electronico ya que sea igual.
					String checkQuery = "SELECT * FROM Usuarios WHERE correo = ?";
					try (PreparedStatement statement = conexion.prepareStatement(checkQuery)) {
						statement.setString(1, correo);
						try (ResultSet rs = statement.executeQuery()) {
							if (rs.next()) {
								JOptionPane.showMessageDialog(null, "Este correo ya está registrado.");
								return;
							}
						}
					}

					//Miro si hay un nombre de usuario igual
					String checkUserQuery = "SELECT * FROM Usuarios WHERE nombre_usuario = ?";
					try (PreparedStatement statement = conexion.prepareStatement(checkUserQuery)) {
						statement.setString(1, nombreUsuario);
						try (ResultSet rs = statement.executeQuery()) {
							if (rs.next()) {
								JOptionPane.showMessageDialog(null, "Este nombre de usuario ya está en uso.");
								return;
							}
						}
					}

					//Si pasa lo anterior, creo el nuevo usuario y su contraseña y nombre_usuario
					String insertQuery = "INSERT INTO Usuarios (correo, contrasenya, nombre_usuario) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStmt = conexion.prepareStatement(insertQuery)) {
                        insertStmt.setString(1, correo);
                        insertStmt.setString(2, hashedPassword); // Guardamos el hash de la contraseña
                        insertStmt.setString(3, nombreUsuario); // Guardamos el nombre de usuario
                        insertStmt.executeUpdate();
                    }

					JOptionPane.showMessageDialog(null, "Cuenta creada con éxito.");

					//Me pasa a la pantalla de Login
					A_LoginScreen loginScreen = new A_LoginScreen();
					loginScreen.setVisible(true);
					dispose();
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error al registrar la cuenta.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnRegistrarse.setBounds(170, 188, 105, 27);
		contentPane.add(btnRegistrarse);
	}
	 private String hashPassword(String password) {
	        try {
	            // Crear el objeto MessageDigest con el algoritmo SHA-256
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
