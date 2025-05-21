package com.proyecto.mi_proyecto;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class B_RegisterScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField correoField;
    private JPasswordField passwordField;
    private JPasswordField repeatPasswordField;
    private JTextField nombreUsuarioField;

    public B_RegisterScreen() {
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

        JLabel lblRegistro = new JLabel("REGISTRO");
        lblRegistro.setFont(new Font("Dialog", Font.BOLD, 26));
        lblRegistro.setForeground(Color.BLACK);
        contentPane.add(lblRegistro, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblNombreUsuario = new JLabel("Nombre de usuario:");
        lblNombreUsuario.setFont(new Font("Dialog", Font.PLAIN, 16));
        contentPane.add(lblNombreUsuario, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        nombreUsuarioField = new JTextField(20);
        contentPane.add(nombreUsuarioField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblCorreo = new JLabel("Correo electrónico:");
        lblCorreo.setFont(new Font("Dialog", Font.PLAIN, 16));
        contentPane.add(lblCorreo, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        correoField = new JTextField(20);
        contentPane.add(correoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(new Font("Dialog", Font.PLAIN, 16));
        contentPane.add(lblContrasena, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(20);
        contentPane.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblRepetir = new JLabel("Repetir contraseña:");
        lblRepetir.setFont(new Font("Dialog", Font.PLAIN, 16));
        contentPane.add(lblRepetir, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        repeatPasswordField = new JPasswordField(20);
        contentPane.add(repeatPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnRegistrar = new JButton("Registrarse");
        btnRegistrar.addActionListener(this::registroAction);
        contentPane.add(btnRegistrar, gbc);

        gbc.gridy = 6;
        JButton btnVolver = new JButton("¿Ya tienes cuenta?");
        btnVolver.addActionListener(e -> {
            A_LoginScreen login = new A_LoginScreen();
            login.setVisible(true);
            dispose();
        });
        contentPane.add(btnVolver, gbc);
    }

    private void registroAction(ActionEvent e) {
        String correo = correoField.getText().trim();
        String pass1 = new String(passwordField.getPassword());
        String pass2 = new String(repeatPasswordField.getPassword());
        String nombreUsuario = nombreUsuarioField.getText().trim();

        if (correo.isEmpty() || pass1.isEmpty() || pass2.isEmpty() || nombreUsuario.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, rellena todos los campos.");
            return;
        }

        if (!pass1.equals(pass2)) {
            JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.");
            return;
        }

        if (pass1.length() < 6) {
            JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 6 caracteres.");
            return;
        }

        if (!correo.contains("@") || !correo.contains(".") || correo.startsWith("@") || correo.endsWith(".")) {
            JOptionPane.showMessageDialog(null, "Correo no válido.");
            return;
        }

        if (User.existeCorreo(correo)) {
            JOptionPane.showMessageDialog(null, "El correo ya está registrado.");
            return;
        }

        if (User.existeUsuario(nombreUsuario)) {
            JOptionPane.showMessageDialog(null, "El nombre de usuario ya existe.");
            return;
        }

        boolean exito = User.registrarUsuario(nombreUsuario, correo, pass1);

        if (exito) {
            JOptionPane.showMessageDialog(null, "Cuenta creada correctamente.");
            A_LoginScreen login = new A_LoginScreen();
            login.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Error en el registro.");
        }
    }
}
