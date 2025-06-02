package com.proyecto.mi_proyecto;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class E_JuegoScreen extends JFrame {

    protected String tematica;
    protected Partida partida;
    protected int rachaPalabras = 0;

    protected JLabel labelTematica;
    protected JLabel labelPalabra;
    protected JLabel labelErrores;
    protected JLabel labelFallidas;
    protected JLabel labelRacha;

    protected JTextField textFieldIntento;

    protected PanelAhorcado panelDibujo;

    protected String usuarioActual;

    
    public E_JuegoScreen(String tematicaSeleccionada, String usuarioActual) {
        this.tematica = tematicaSeleccionada;
        this.usuarioActual = usuarioActual;
        initUI();
        cargarNuevaPalabra();
    }

    
    public E_JuegoScreen(Partida partidaCargada) {
        this.partida = partidaCargada;
        this.tematica = partidaCargada.getTematica();
        this.usuarioActual = partidaCargada.getUser();
        this.rachaPalabras = partidaCargada.getRacha(); 
        initUI();
        actualizarLabel();
        panelDibujo.repaint();
        labelRacha.setText("Racha: " + rachaPalabras); 
    }

   
    private void initUI() {
        setTitle("Ahorcado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        getContentPane().setBackground(new Color(245, 245, 245));
        getContentPane().setLayout(new BorderLayout());

        JButton botonCerrar = new JButton("X");
        botonCerrar.setFont(new Font("Arial", Font.BOLD, 18));
        botonCerrar.setForeground(Color.RED);
        botonCerrar.setFocusPainted(false);
        botonCerrar.addActionListener(e -> dispose());

        JPanel panelCerrar = new JPanel();
        panelCerrar.setBackground(new Color(245, 245, 245));
        panelCerrar.add(botonCerrar);
        getContentPane().add(panelCerrar, BorderLayout.NORTH);

        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Opciones");
        menuBar.add(menuArchivo);

        JMenuItem itemGuardarYSalir = new JMenuItem("Guardar y salir");
        itemGuardarYSalir.addActionListener(e -> guardarYSalir());

        JMenuItem itemSalirMenu = new JMenuItem("Salir al menú principal");
        itemSalirMenu.addActionListener(e -> salirMenu());

        JMenuItem itemSalirApp = new JMenuItem("Salir de la aplicación");
        itemSalirApp.addActionListener(e -> salirApp());

        menuArchivo.add(itemGuardarYSalir);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalirMenu);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalirApp);

        setJMenuBar(menuBar);

        JPanel panelSuperior = new JPanel(new GridLayout(3, 1));
        panelSuperior.setBackground(new Color(230, 230, 250));
        panelSuperior.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 30));

        labelTematica = new JLabel("Temática: " + tematica, JLabel.CENTER);
        labelTematica.setFont(new Font("Arial", Font.BOLD, 30));

        labelPalabra = new JLabel("", JLabel.CENTER);
        labelPalabra.setFont(new Font("Courier New", Font.BOLD, 40));

        JPanel panelInfo = new JPanel(new GridLayout(1, 3));
        panelInfo.setBackground(new Color(230, 230, 250));

        labelErrores = new JLabel("Errores: 0", JLabel.CENTER);
        labelFallidas = new JLabel("Letras fallidas: ", JLabel.CENTER);
        labelRacha = new JLabel("Racha: 0", JLabel.CENTER);

        labelErrores.setFont(new Font("Arial", Font.PLAIN, 20));
        labelFallidas.setFont(new Font("Arial", Font.PLAIN, 20));
        labelRacha.setFont(new Font("Arial", Font.PLAIN, 20));

        panelInfo.add(labelErrores);
        panelInfo.add(labelFallidas);
        panelInfo.add(labelRacha);

        panelSuperior.add(labelTematica);
        panelSuperior.add(labelPalabra);
        panelSuperior.add(panelInfo);

        getContentPane().add(panelSuperior, BorderLayout.NORTH);

        panelDibujo = new PanelAhorcado();
        panelDibujo.setBackground(Color.WHITE);
        getContentPane().add(panelDibujo, BorderLayout.CENTER);

        panelDibujo.setBackground(Color.WHITE);
        getContentPane().add(panelDibujo, BorderLayout.CENTER);

        panelDibujo.setBackground(Color.WHITE);
        getContentPane().add(panelDibujo, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(230, 230, 250));
        panelInferior.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel labelIntento = new JLabel("Letra o palabra:");
        labelIntento.setFont(new Font("Arial", Font.PLAIN, 20));

        textFieldIntento = new JTextField(20);
        textFieldIntento.setFont(new Font("Arial", Font.PLAIN, 24));
        textFieldIntento.addActionListener(e -> procesarIntento());

        JButton btnProbar = new JButton("Probar");
        btnProbar.setFont(new Font("Arial", Font.BOLD, 20));
        btnProbar.addActionListener(e -> procesarIntento());

        panelInferior.add(labelIntento);
        panelInferior.add(textFieldIntento);
        panelInferior.add(btnProbar);

        getContentPane().add(panelInferior, BorderLayout.SOUTH);
    }

    // Método para guardar partida y salir al menú principal
    private void guardarYSalir() {
        if (partida != null) {
            try {
                partida.setUser(usuarioActual);
                PartidaDAO.guardarPartida(partida);
                JOptionPane.showMessageDialog(this, "Partida guardada correctamente.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar la partida: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        }
        new C_MenuPrincipalScreen(usuarioActual).setVisible(true);
        dispose();
    }

    private void salirMenu() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres salir al menú principal?", "Confirmar salida",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmacion == JOptionPane.YES_OPTION) {
        	new C_MenuPrincipalScreen(usuarioActual).setVisible(true);
            dispose();
        }
    }

    private void salirApp() {
        int res = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres salir de la aplicación?", "Confirmar salida",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void procesarIntento() {
        String intento = textFieldIntento.getText().toLowerCase().trim();
        textFieldIntento.setText("");
        if (intento.isEmpty()) {
            return;
        }

        if (intento.length() > 1) {
            procesarPalabraCompleta(intento);
        } else {
            procesarLetra(intento.charAt(0));
        }
    }

    private void procesarPalabraCompleta(String intento) {
        if (intento.equalsIgnoreCase(partida.getPalabra())) {
            JOptionPane.showMessageDialog(this, "¡Adivinado!");
            rachaTema();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Palabra incorrecta. Has perdido.\nLa palabra era: " + partida.getPalabra());
            finalizarPartidaPerdida();
        }
    }

    private void procesarLetra(char letra) {
        int erroresAntes = partida.getLetrasFallidas().size();
        partida.intentarLetra(letra);
        actualizarLabel();

        int erroresDespues = partida.getLetrasFallidas().size();

        if (erroresDespues > erroresAntes) {
            labelErrores.setText("Errores: " + erroresDespues);
            labelFallidas.setText("Letras fallidas: " + partida.getLetrasFallidas().toString().replaceAll("[\\[\\],]", ""));
            
            
            panelDibujo.setIntentosFallidos(erroresDespues);
            panelDibujo.repaint();

            if (erroresDespues >= 6) {
                JOptionPane.showMessageDialog(this,
                        "Has perdido. La palabra era: " + partida.getPalabra());
                finalizarPartidaPerdida();
            }
        } else {
            if (partida.palabraCompleta()) {
                JOptionPane.showMessageDialog(this, "¡Adivinado!");
                rachaTema();
            }
        }
    }


    private void rachaTema() {
        rachaPalabras++;
        partida.setRacha(rachaPalabras);
        labelRacha.setText("Racha: " + rachaPalabras);

        // Guardar automáticamente (opcional)
        try {
            PartidaDAO.guardarPartida(partida);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (rachaPalabras == 5) {
            cambiarTematica();
        } else {
            cargarNuevaPalabra();
        }
    }


    protected void cargarNuevaPalabra() {
        String nuevaPalabra = TematicaMongo.palabraRandom(tematica);
        if (nuevaPalabra == null || nuevaPalabra.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay palabras para la temática: " + tematica);
            dispose();
            return;
        }
        partida = new Partida(tematica, nuevaPalabra, usuarioActual, new ArrayList<>(), new ArrayList<>(), rachaPalabras);
        panelDibujo.repaint();
        actualizarLabel();
        labelErrores.setText("Errores: 0");
        labelFallidas.setText("Letras fallidas: ");
        labelRacha.setText("Racha: " + rachaPalabras);
    }

    protected void actualizarLabel() {
        String palabra = partida.getPalabra().toLowerCase();
        List<Character> letrasAdivinadas = partida.getLetrasAdivinadas();

        StringBuilder estado = new StringBuilder();

        for (int i = 0; i < palabra.length(); i++) {
            char c = palabra.charAt(i);

            if (!Character.isLetter(c)) {
                // Mostrar directamente espacios, guiones, números, etc.
                estado.append(c);
            } else if (letrasAdivinadas.contains(c)) {
                // Mostrar letras adivinadas en mayúscula
                estado.append(Character.toUpperCase(c));
            } else {
                // Mostrar guion bajo para letras no adivinadas
                estado.append("_");
            }
            estado.append(" ");  // Espacio entre caracteres para mejor legibilidad
        }

        labelPalabra.setText(estado.toString().trim());
    }


    private void cambiarTematica() {
        // Reseteamos racha y pedimos otra temática
        rachaPalabras = 0;
        JOptionPane.showMessageDialog(this, "¡Felicidades! Cambiamos de temática.");
        dispose();
        new D_SelectorTemasScreen(usuarioActual).setVisible(true);
    }

    private void finalizarPartidaPerdida() {
        
        F_PartidaPerdidaScreen pantallaPerdida = new F_PartidaPerdidaScreen(usuarioActual);
        pantallaPerdida.setVisible(true);
        dispose();  
    }

}
