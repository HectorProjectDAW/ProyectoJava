// DesafioScreen.java
package com.proyecto.mi_proyecto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DesafioScreen extends JFrame {

    private ConexionJuego conexion;
    private boolean esServidor;
    private String palabra;
    private String ip;
    private int puerto;

    private String palabraSecreta;
    private String palabraOculta;
    private int intentosFallidos = 0;
    private final int MAX_INTENTOS = 6;

    private JTextField letraField;
    private JButton enviarLetraBtn;
    private JButton rendirseBtn;
    private JLabel palabraOcultaLabel;
    private JTextArea logArea;
    private PanelAhorcado panelAhorcado;

    public DesafioScreen(String palabra, int puerto, String ip, boolean esServidor) {
        this.palabra = palabra.toUpperCase();
        this.puerto = puerto;
        this.ip = ip;
        this.esServidor = esServidor;

        setTitle("Desafío 1 vs 1");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        iniciarUI();
        iniciarConexion();
    }

    private void iniciarUI() {
        palabraOcultaLabel = new JLabel();
        palabraOcultaLabel.setFont(new Font("Monospaced", Font.BOLD, 28));

        letraField = new JTextField(5);
        enviarLetraBtn = new JButton("Enviar letra/palabra");

        rendirseBtn = new JButton("Rendirse");
        rendirseBtn.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres rendirte?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                cerrarConexion();
                System.exit(0);
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.add(palabraOcultaLabel);
        topPanel.add(new JLabel("Letra/Palabra:"));
        topPanel.add(letraField);
        topPanel.add(enviarLetraBtn);
        topPanel.add(rendirseBtn);

        logArea = new JTextArea(10, 50);
        logArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(logArea);

        panelAhorcado = new PanelAhorcado();

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(panelAhorcado, BorderLayout.CENTER);
        getContentPane().add(scroll, BorderLayout.SOUTH);

        enviarLetraBtn.addActionListener(e -> enviarLetra());
        letraField.addActionListener(e -> enviarLetra());
    }

    private void iniciarConexion() {
        conexion = new ConexionJuego(esServidor);
        conexion.setOnMensajeRecibido(this::procesarMensaje);

        if (esServidor) {
            palabraSecreta = palabra;
            palabraOculta = ocultarPalabra(palabraSecreta);
            palabraOcultaLabel.setText(palabraSecreta + " (Palabra seleccionada)");
            letraField.setEnabled(false);
            enviarLetraBtn.setEnabled(false);

            try {
                conexion.iniciarServidor(puerto);
                log("Servidor iniciado en puerto " + puerto);
                new Thread(() -> {
                    try {
                        Thread.sleep(500);
                        conexion.enviarMensaje("PALABRA:" + palabraOculta);
                        conexion.enviarMensaje("TITULO:Jugador 2");
                        SwingUtilities.invokeLater(() -> {
                            letraField.setEnabled(true);
                            enviarLetraBtn.setEnabled(true);
                        });
                    } catch (Exception ignored) {}
                }).start();
            } catch (Exception e) {
                log("Error iniciando servidor: " + e.getMessage());
            }
        } else {
            palabraSecreta = null;
            palabraOcultaLabel.setText("Esperando palabra...");
            letraField.setEnabled(false);
            enviarLetraBtn.setEnabled(false);

            try {
                conexion.conectarCliente(ip, puerto);
                log("Conectado al servidor " + ip + ":" + puerto);
                conexion.enviarMensaje("TITULO:Jugador 1");
            } catch (Exception e) {
                log("Error conectando cliente: " + e.getMessage());
            }
        }
    }

    private void enviarLetra() {
        String texto = letraField.getText().trim().toUpperCase();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduce una letra o palabra válida", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        conexion.enviarMensaje(texto);
        log("Enviado: " + texto);
        letraField.setText("");
    }

    private void procesarMensaje(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            log("Recibido: " + mensaje);

            if (mensaje.startsWith("TITULO:")) {
                setTitle("Desafío - " + mensaje.substring(7));
            } else if (mensaje.startsWith("PALABRA:")) {
                palabraOculta = mensaje.substring(8);
                palabraOcultaLabel.setText(palabraOculta + " (Adivina)");
                letraField.setEnabled(true);
                enviarLetraBtn.setEnabled(true);
            } else if (mensaje.startsWith("ACTUALIZAR:")) {
                palabraOculta = mensaje.substring(10);
                palabraOcultaLabel.setText(palabraOculta + " (Adivina)");
            } else if (mensaje.startsWith("INTENTOS:")) {
                try {
                    intentosFallidos = Integer.parseInt(mensaje.substring(9));
                    panelAhorcado.setIntentosFallidos(intentosFallidos);
                    panelAhorcado.repaint();
                } catch (NumberFormatException e) {
                    log("Error al parsear INTENTOS");
                }
            } else if ("GANASTE".equals(mensaje)) {
                JOptionPane.showMessageDialog(this, "¡Ganaste!", "Fin", JOptionPane.INFORMATION_MESSAGE);
                letraField.setEnabled(false);
                enviarLetraBtn.setEnabled(false);
            } else if ("PERDISTE".equals(mensaje)) {
                JOptionPane.showMessageDialog(this, "Perdiste. La palabra era: " + palabraSecreta, "Fin", JOptionPane.INFORMATION_MESSAGE);
                letraField.setEnabled(false);
                enviarLetraBtn.setEnabled(false);
            } else {
                if (esServidor) {
                    if (mensaje.length() == 1) {
                        procesarLetra(mensaje.charAt(0));
                    } else {
                        procesarPalabra(mensaje);
                    }
                }
            }
        });
    }

    private void procesarLetra(char letra) {
        letra = Character.toUpperCase(letra);
        boolean acierto = false;

        StringBuilder nuevaPalabraOculta = new StringBuilder(palabraOculta);
        for (int i = 0; i < palabraSecreta.length(); i++) {
            if (palabraSecreta.charAt(i) == letra && palabraOculta.charAt(i) == '_') {
                nuevaPalabraOculta.setCharAt(i, letra);
                acierto = true;
            }
        }

        palabraOculta = nuevaPalabraOculta.toString();
        conexion.enviarMensaje("ACTUALIZAR:" + palabraOculta);
        palabraOcultaLabel.setText(palabraSecreta + " (Palabra seleccionada)");

        if (!acierto) {
            intentosFallidos++;
            conexion.enviarMensaje("INTENTOS:" + intentosFallidos);
            panelAhorcado.setIntentosFallidos(intentosFallidos);
            panelAhorcado.repaint();
        }

        comprobarFinJuego();
    }

    private void procesarPalabra(String intento) {
        intento = intento.toUpperCase();
        if (intento.equals(palabraSecreta)) {
            palabraOculta = palabraSecreta;
            conexion.enviarMensaje("ACTUALIZAR:" + palabraOculta);
            conexion.enviarMensaje("GANASTE");
            palabraOcultaLabel.setText(palabraSecreta + " (Palabra seleccionada)");
            letraField.setEnabled(false);
            enviarLetraBtn.setEnabled(false);
        } else {
            intentosFallidos++;
            conexion.enviarMensaje("INTENTOS:" + intentosFallidos);
            panelAhorcado.setIntentosFallidos(intentosFallidos);
            panelAhorcado.repaint();
            if (intentosFallidos >= MAX_INTENTOS) {
                conexion.enviarMensaje("PERDISTE");
                JOptionPane.showMessageDialog(this, "Perdiste. La palabra era: " + palabraSecreta, "Fin", JOptionPane.INFORMATION_MESSAGE);
                letraField.setEnabled(false);
                enviarLetraBtn.setEnabled(false);
            }
        }
    }

    private void comprobarFinJuego() {
        if (!palabraOculta.contains("_")) {
            conexion.enviarMensaje("GANASTE");
            JOptionPane.showMessageDialog(this, "¡Ganaste!", "Fin", JOptionPane.INFORMATION_MESSAGE);
            letraField.setEnabled(false);
            enviarLetraBtn.setEnabled(false);
        } else if (intentosFallidos >= MAX_INTENTOS) {
            conexion.enviarMensaje("PERDISTE");
            JOptionPane.showMessageDialog(this, "Perdiste. La palabra era: " + palabraSecreta, "Fin", JOptionPane.INFORMATION_MESSAGE);
            letraField.setEnabled(false);
            enviarLetraBtn.setEnabled(false);
        }
    }

    private String ocultarPalabra(String palabra) {
        StringBuilder oculta = new StringBuilder();
        for (char c : palabra.toCharArray()) {
            if (Character.isLetter(c)) {
                oculta.append('_');
            } else {
                oculta.append(c);
            }
        }
        return oculta.toString();
    }

    private void log(String texto) {
        logArea.append(texto + "\n");
    }

    private void cerrarConexion() {
        if (conexion != null) {
            conexion.cerrar();
        }
    }
}
