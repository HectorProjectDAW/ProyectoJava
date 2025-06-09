package com.proyecto.mi_proyecto;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.ResourceBundle;

public class A_LoginScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JPasswordField passwordField;

    
    private JLabel lblUsuario;
    private JLabel lblContrasena;
    private JLabel lblNoTienesCuenta;
    private JButton btnContinuar;
    private JButton btnCrearCuenta;
    private JButton btnInvitado;
    private JPanel contentPane;

    
    private JComboBox<String> comboIdiomas;

    
    private Locale currentLocale;

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

    @SuppressWarnings("deprecation")
	public A_LoginScreen() {
        // Obtener locale actual de Messages (que carga idioma por defecto o el último seleccionado)
        this.currentLocale = Messages.getCurrentLocale();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setLocationRelativeTo(null);

        contentPane = new JPanel(new GridBagLayout());
        contentPane.setBackground(Color.LIGHT_GRAY);
        setContentPane(contentPane);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Combo selector idioma
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        comboIdiomas = new JComboBox<>(new String[]{"Español", "English"});
        
        // Inicializar el combo según locale actual
        if ("en".equals(currentLocale.getLanguage())) {
            comboIdiomas.setSelectedIndex(1);
        } else {
            comboIdiomas.setSelectedIndex(0);
        }

        comboIdiomas.addActionListener(e -> {
            String seleccionado = (String) comboIdiomas.getSelectedItem();
            Locale nuevoLocale;
            if ("English".equals(seleccionado)) {
                nuevoLocale = new Locale("en", "US");
            } else {
                nuevoLocale = new Locale("es", "ES");
            }
            cambiarIdioma(nuevoLocale);
        });
        contentPane.add(comboIdiomas, gbc);

        // Label Login
        gbc.gridy = 1;
        JLabel lblLogin = new JLabel("LOGIN");
        lblLogin.setFont(new Font("Dialog", Font.BOLD, 26));
        lblLogin.setForeground(Color.BLACK);
        contentPane.add(lblLogin, gbc);

        // Campos usuario y contraseña
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridy = 2;
        gbc.gridx = 0;
        lblUsuario = new JLabel("Nombre de usuario:");
        lblUsuario.setFont(new Font("Dialog", Font.PLAIN, 16));
        contentPane.add(lblUsuario, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        textField = new JTextField(20);
        contentPane.add(textField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(new Font("Dialog", Font.PLAIN, 16));
        contentPane.add(lblContrasena, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(20);
        contentPane.add(passwordField, gbc);

        // Botón continuar
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        btnContinuar = new JButton("Continuar");
        btnContinuar.addActionListener(this::loginAction);
        contentPane.add(btnContinuar, gbc);

        // Label "¿No tienes cuenta?"
        gbc.gridy = 5;
        lblNoTienesCuenta = new JLabel("¿No tienes cuenta?");
        contentPane.add(lblNoTienesCuenta, gbc);

        // Botón crear cuenta
        gbc.gridy = 6;
        btnCrearCuenta = new JButton("Crear cuenta");
        btnCrearCuenta.addActionListener(e -> {
            B_RegisterScreen registerScreen = new B_RegisterScreen();
            registerScreen.setVisible(true);
            dispose();
        });
        contentPane.add(btnCrearCuenta, gbc);

        // Botón continuar sin iniciar sesión
        gbc.gridy = 7;
        btnInvitado = new JButton("Continuar sin iniciar sesión");
        btnInvitado.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Has iniciado como invitado. No se guardarán datos.");
            // No pasamos locale porque C_MenuPrincipalScreen leerá de Messages
            C_MenuPrincipalScreen menu = new C_MenuPrincipalScreen("Invitado");
            menu.setVisible(true);
            dispose();
        });
        contentPane.add(btnInvitado, gbc);

        // Actualizar textos con locale actual
        actualizarTextos();
    }

    private void cambiarIdioma(Locale locale) {
        this.currentLocale = locale;
        // Actualizamos Messages para que sea global
        Messages.loadLocale(locale);
        actualizarTextos();
    }

    private void actualizarTextos() {
        try {
            ResourceBundle bundle = Messages.labels();

            lblUsuario.setText(bundle.getString("label.nombreUsuario"));
            lblContrasena.setText(bundle.getString("label.contrasena"));
            lblNoTienesCuenta.setText(bundle.getString("label.noCuenta"));
            btnContinuar.setText(bundle.getString("button.iniciarSesion"));
            btnCrearCuenta.setText(bundle.getString("button.registrarse"));
            btnInvitado.setText(bundle.getString("button.continuarInvitado"));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error cargando los textos del idioma: " + currentLocale);
        }
    }

    // Método login
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
            // No pasamos locale, C_MenuPrincipalScreen lee de Messages
            C_MenuPrincipalScreen menu = new C_MenuPrincipalScreen(usuario);
            menu.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Contraseña incorrecta");
        }
    }

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
}
