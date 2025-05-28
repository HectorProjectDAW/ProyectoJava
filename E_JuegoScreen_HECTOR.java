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
    protected JPanel panelDibujo;

    protected String usuarioActual;

    // Constructor para nueva partida (temática + usuario)
    public E_JuegoScreen(String tematicaSeleccionada, String usuarioActual) {
        this.tematica = tematicaSeleccionada;
        this.usuarioActual = usuarioActual;
        initUI();
        cargarNuevaPalabra();
    }

    // Constructor para cargar partida existente
    public E_JuegoScreen(Partida partidaCargada) {
        this.partida = partidaCargada;
        this.tematica = partidaCargada.getTematica();
        this.usuarioActual = partidaCargada.getUser();
        initUI();
        actualizarLabel();
        panelDibujo.repaint();
    }

    // Inicialización común de la interfaz
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

        panelDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarAhorcado(g, partida != null ? partida.getLetrasFallidas().size() : 0);
            }
        };
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
        new C_MenuPrincipalScreen().setVisible(true);
        dispose();
    }

    private void salirMenu() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres salir al menú principal?", "Confirmar salida",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmacion == JOptionPane.YES_OPTION) {
            new C_MenuPrincipalScreen().setVisible(true);
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
        labelRacha.setText("Racha: " + rachaPalabras);

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
        partida = new Partida(tematica, nuevaPalabra, usuarioActual, new ArrayList<>(), new ArrayList<>(), 0);
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
            if (letrasAdivinadas.contains(c)) {
                estado.append(c).append(" ");
            } else {
                estado.append("_ ");
            }
        }

        labelPalabra.setText(estado.toString().trim());

        labelErrores.setText("Errores: " + partida.getLetrasFallidas().size());
        labelFallidas.setText("Letras fallidas: " + partida.getLetrasFallidas().toString().replaceAll("[\\[\\],]", ""));
        labelRacha.setText("Racha: " + rachaPalabras);
    }

    private void cambiarTematica() {
        // Reseteamos racha y pedimos otra temática
        rachaPalabras = 0;
        JOptionPane.showMessageDialog(this, "¡Felicidades! Cambiamos de temática.");
        dispose();
        new D_SelectorTemasScreen(usuarioActual).setVisible(true);
    }

    private void finalizarPartidaPerdida() {
        int opcion = JOptionPane.showOptionDialog(this,
            "Has perdido la partida. ¿Quieres intentar de nuevo?",
            "Partida perdida",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            new String[]{"Reintentar", "Salir al menú", "Salir de la aplicación"},
            "Reintentar");

        if (opcion == JOptionPane.YES_OPTION) {
            // Reintentar: reiniciar la palabra actual, resetear errores y fallidas
            rachaPalabras = 0;
            cargarNuevaPalabra();
        } else if (opcion == JOptionPane.NO_OPTION) {
            // Salir al menú principal
            new C_MenuPrincipalScreen().setVisible(true);
            dispose();
        } else if (opcion == JOptionPane.CANCEL_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
            // Salir de la aplicación
            System.exit(0);
        }
    }

    private void dibujarAhorcado(Graphics g, int errores) {
        // Dibujar el ahorcado según número de errores
        int width = panelDibujo.getWidth();
        int height = panelDibujo.getHeight();

        int baseY = height - 50;
        int baseX = width / 2;

        g.setColor(Color.BLACK);

        // Base
        g.drawLine(baseX - 100, baseY, baseX + 100, baseY);
        // Poste vertical
        g.drawLine(baseX - 50, baseY, baseX - 50, baseY - 300);
        // Poste horizontal superior
        g.drawLine(baseX - 50, baseY - 300, baseX + 50, baseY - 300);
        // Cuerda
        g.drawLine(baseX + 50, baseY - 300, baseX + 50, baseY - 250);

        if (errores > 0) {
            // Cabeza
            g.drawOval(baseX + 35, baseY - 250, 30, 30);
        }
        if (errores > 1) {
            // Cuerpo
            g.drawLine(baseX + 50, baseY - 220, baseX + 50, baseY - 150);
        }
        if (errores > 2) {
            // Brazo izquierdo
            g.drawLine(baseX + 50, baseY - 210, baseX + 20, baseY - 180);
        }
        if (errores > 3) {
            // Brazo derecho
            g.drawLine(baseX + 50, baseY - 210, baseX + 80, baseY - 180);
        }
        if (errores > 4) {
            // Pierna izquierda
            g.drawLine(baseX + 50, baseY - 150, baseX + 20, baseY - 120);
        }
        if (errores > 5) {
            // Pierna derecha
            g.drawLine(baseX + 50, baseY - 150, baseX + 80, baseY - 120);
        }
    }
}
