package com.proyecto.mi_proyecto;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.border.EmptyBorder;

public class B_RegisterScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField correoField;
    private JPasswordField passwordField;
    private JPasswordField repeatPasswordField;
    private JTextField nombreUsuarioField;
    private JLabel lblRegistro;
    private JLabel lblNombreUsuario;
    private JLabel lblCorreo;
    private JLabel lblContrasena;
    private JLabel lblRepetir;
    private JCheckBox consentimientoCheck;
    private JButton btnPolitica;
    private JButton btnRegistrar;
    private JButton btnVolver;
    private JComboBox<String> comboIdioma;
    private Locale currentLocale;


    @SuppressWarnings("deprecation")
	public B_RegisterScreen() {
        
    	//Manera bonita de chatgpt para los botones
        this.currentLocale = Messages.getCurrentLocale();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new GridBagLayout());
        contentPane.setBackground(Color.LIGHT_GRAY);
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        lblRegistro = new JLabel("REGISTRO");
        lblRegistro.setFont(new Font("Dialog", Font.BOLD, 26));
        lblRegistro.setForeground(Color.BLACK);
        contentPane.add(lblRegistro, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        lblNombreUsuario = new JLabel("Nombre de usuario:");
        lblNombreUsuario.setFont(new Font("Dialog", Font.PLAIN, 16));
        contentPane.add(lblNombreUsuario, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        nombreUsuarioField = new JTextField(20);
        contentPane.add(nombreUsuarioField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        lblCorreo = new JLabel("Correo electrónico:");
        lblCorreo.setFont(new Font("Dialog", Font.PLAIN, 16));
        contentPane.add(lblCorreo, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        correoField = new JTextField(20);
        contentPane.add(correoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(new Font("Dialog", Font.PLAIN, 16));
        contentPane.add(lblContrasena, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(20);
        contentPane.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        lblRepetir = new JLabel("Repetir contraseña:");
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
        consentimientoCheck = new JCheckBox("Acepto la política de privacidad");
        consentimientoCheck.setBackground(Color.LIGHT_GRAY);
        contentPane.add(consentimientoCheck, gbc);

        
        gbc.gridy = 6;
        btnPolitica = new JButton("Ver política de privacidad");
        btnPolitica.addActionListener(e -> mostrarPolitica());
        contentPane.add(btnPolitica, gbc);

        
        gbc.gridy = 7;
        btnRegistrar = new JButton("Registrarse");
        btnRegistrar.addActionListener(this::registroAction);
        contentPane.add(btnRegistrar, gbc);

        
        gbc.gridy = 8;
        btnVolver = new JButton("¿Ya tienes cuenta?");
        btnVolver.addActionListener(e -> {
            A_LoginScreen login = new A_LoginScreen();
            login.setVisible(true);
            dispose();
        });
        contentPane.add(btnVolver, gbc);
        
        comboIdioma = new JComboBox<>(new String[]{"Español", "English"});
        comboIdioma.setSelectedIndex(currentLocale.getLanguage().equals("en") ? 1 : 0);
        comboIdioma.addActionListener(e -> {
            String seleccionado = (String) comboIdioma.getSelectedItem();
            Locale nuevoLocale = "English".equals(seleccionado) ? new Locale("en", "US") : new Locale("es", "ES");
            cambiarIdioma(nuevoLocale);
        });

        GridBagConstraints gbcComboIdioma = new GridBagConstraints();
        gbcComboIdioma.gridx = 1;
        gbcComboIdioma.gridy = 0;
        gbcComboIdioma.anchor = GridBagConstraints.NORTHEAST;
        gbcComboIdioma.insets = new Insets(20, 0, 0, 20);
        contentPane.add(comboIdioma, gbcComboIdioma);

        
        actualizarTextos();
    }
    private void cambiarIdioma(Locale locale) {
        this.currentLocale = locale;
        Messages.loadLocale(locale);
        actualizarTextos();
    }


    private void registroAction(ActionEvent e) {
        ResourceBundle bundle = Messages.labels();

        //Guardar los datos
        String correo = correoField.getText().trim();
        String pass1 = new String(passwordField.getPassword());
        String pass2 = new String(repeatPasswordField.getPassword());
        String nombreUsuario = nombreUsuarioField.getText().trim();

        //Tiene q aceptar antes
        if (!consentimientoCheck.isSelected()) {
            JOptionPane.showMessageDialog(null, bundle.getString("message.accept_policy"));
            return;
        }
        //Q no esté vacío
        if (correo.isEmpty() || pass1.isEmpty() || pass2.isEmpty() || nombreUsuario.isEmpty()) {
            JOptionPane.showMessageDialog(null, bundle.getString("message.fill_fields"));
            return;
        }
        //Q las contraseñas coincidan
        if (!pass1.equals(pass2)) {
            JOptionPane.showMessageDialog(null, bundle.getString("message.password_mismatch"));
            return;
        }
        //Minimo 6 caracteres
        if (pass1.length() < 6) {
            JOptionPane.showMessageDialog(null, bundle.getString("message.password_short"));
            return;
        }
        //Que tenga una @ y un . en ese orden
        if (!correo.contains("@") || !correo.contains(".") || correo.startsWith("@") || correo.endsWith(".")) {
            JOptionPane.showMessageDialog(null, bundle.getString("message.invalid_email"));
            return;
        }
        //Que no exista el nombre usuario
        if (User.existeUsuario(nombreUsuario)) {
            JOptionPane.showMessageDialog(null, bundle.getString("message.username_exists"));
            return;
        }

        //Mete al usuario en la tabla
        boolean exito = User.registrarUsuario(nombreUsuario, correo, pass1);

        if (exito) {
            JOptionPane.showMessageDialog(null, bundle.getString("message.register_success"));
            A_LoginScreen login = new A_LoginScreen();
            login.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, bundle.getString("message.register_error"));
        }
    }

    

    private void mostrarPolitica() {
        ResourceBundle bundle = Messages.labels(); 

        String titulo = bundle.getString("policy.title");
        String contenido = bundle.getString("policy.content");

        JTextArea textArea = new JTextArea(contenido);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 250));

        JOptionPane.showMessageDialog(this, scrollPane, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    
    private void actualizarTextos() {
        ResourceBundle bundle = Messages.labels();
        lblRegistro.setText(bundle.getString("register.title"));
        lblNombreUsuario.setText(bundle.getString("register.username"));
        lblCorreo.setText(bundle.getString("register.email"));
        lblContrasena.setText(bundle.getString("register.password"));
        lblRepetir.setText(bundle.getString("register.repeat_password"));
        consentimientoCheck.setText(bundle.getString("register.consent"));
        btnPolitica.setText(bundle.getString("register.view_policy"));
        btnRegistrar.setText(bundle.getString("register.button_register"));
        btnVolver.setText(bundle.getString("register.button_back"));
    }

}
