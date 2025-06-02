package com.proyecto.mi_proyecto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class PantallaConexion extends JFrame {

    private JTextField ipField;
    private JTextField puertoField;
    private JTextField palabraField;
    private JRadioButton servidorBtn;
    private JRadioButton clienteBtn;
    private JButton conectarBtn;

    public PantallaConexion() {
        setTitle("Conexión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        servidorBtn = new JRadioButton("Servidor");
        clienteBtn = new JRadioButton("Cliente");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(servidorBtn);
        grupo.add(clienteBtn);
        servidorBtn.setSelected(true);

        palabraField = new JTextField();
        ipField = new JTextField("localhost");
        puertoField = new JTextField("5005");

        conectarBtn = new JButton("Conectar");

        panel.add(new JLabel("Modo:"));
        JPanel radioPanel = new JPanel();
        radioPanel.add(servidorBtn);
        radioPanel.add(clienteBtn);
        panel.add(radioPanel);

        panel.add(new JLabel("Palabra (solo servidor):"));
        panel.add(palabraField);

        panel.add(new JLabel("IP (solo cliente):"));
        panel.add(ipField);

        panel.add(new JLabel("Puerto:"));
        panel.add(puertoField);

        panel.add(new JLabel());
        panel.add(conectarBtn);

        add(panel);

        clienteBtn.addActionListener(e -> palabraField.setEnabled(false));
        servidorBtn.addActionListener(e -> palabraField.setEnabled(true));

        palabraField.setEnabled(true);

        conectarBtn.addActionListener(e -> conectar());
    }

    private void conectar() {
        String ip = ipField.getText().trim();
        String puertoTexto = puertoField.getText().trim();
        String palabra = palabraField.getText().trim().toUpperCase();

        boolean esServidor = servidorBtn.isSelected();

        if (puertoTexto.isEmpty() || (!esServidor && ip.isEmpty())) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos requeridos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (esServidor && (palabra.isEmpty() || !palabra.matches("[A-Z]+"))) {
            JOptionPane.showMessageDialog(this, "Introduce una palabra válida (solo letras A-Z)", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int puerto;
        try {
            puerto = Integer.parseInt(puertoTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Puerto inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!esServidor && (ip == null || ip.isEmpty())) {
            JOptionPane.showMessageDialog(this, "IP requerida para el cliente", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        dispose();

        String palabraFinal = esServidor ? palabra : "";

        SwingUtilities.invokeLater(() -> {
            DesafioScreen juego = new DesafioScreen(palabraFinal, puerto, ip, esServidor);
            juego.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PantallaConexion().setVisible(true);
        });
    }
}
