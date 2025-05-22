package com.proyecto.mi_proyecto;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public E_JuegoScreen(String tematicaSeleccionada) {
        this.tematica = tematicaSeleccionada;

        setTitle("Ahorcado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        getContentPane().setBackground(new Color(245, 245, 245));
        getContentPane().setLayout(new BorderLayout());

        // Botón cerrar (X)
        JButton botonCerrar = new JButton("X");
        botonCerrar.setFont(new Font("Arial", Font.BOLD, 18));
        botonCerrar.setForeground(Color.RED);
        botonCerrar.setFocusPainted(false);
        botonCerrar.addActionListener(e -> dispose());

        JPanel panelCerrar = new JPanel();
        panelCerrar.setBackground(new Color(245, 245, 245));
        panelCerrar.add(botonCerrar);
        getContentPane().add(panelCerrar, BorderLayout.NORTH);

        // Barra menú
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Opciones");
        menuBar.add(menuArchivo);

        JMenuItem itemSalirMenu = new JMenuItem("Salir al menú principal");
        itemSalirMenu.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Estás seguro de que quieres salir al menú principal?", "Confirmar salida",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirmacion == JOptionPane.YES_OPTION) {
                C_MenuPrincipalScreen menu = new C_MenuPrincipalScreen();
                menu.setVisible(true);
                dispose();
            }
        });

        JMenuItem itemSalirApp = new JMenuItem("Salir de la aplicación");
        itemSalirApp.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(this,
                    "¿Estás seguro de que quieres salir de la aplicación?", "Confirmar salida",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (res == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        menuArchivo.add(itemSalirMenu);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalirApp);

        setJMenuBar(menuBar);

        // Panel superior con etiquetas
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

        // Panel central para dibujo
        panelDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarAhorcado(g, partida != null ? partida.getLetrasFallidas().size() : 0);
            }
        };
        panelDibujo.setBackground(Color.WHITE);
        getContentPane().add(panelDibujo, BorderLayout.CENTER);

        // Panel inferior para input y botón
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(230, 230, 250));
        panelInferior.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel labelIntento = new JLabel("Letra o palabra:");
        labelIntento.setFont(new Font("Arial", Font.PLAIN, 20));

        textFieldIntento = new JTextField(20);
        textFieldIntento.setFont(new Font("Arial", Font.PLAIN, 24));

        JButton btnProbar = new JButton("Probar");
        btnProbar.setFont(new Font("Arial", Font.BOLD, 20));
        btnProbar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String intento = textFieldIntento.getText().toLowerCase().trim();
                textFieldIntento.setText("");
                if (intento.isEmpty()) {
                    return;
                }
                if (intento.length() > 1) {
                    if (intento.equalsIgnoreCase(partida.getPalabra())) {
                        JOptionPane.showMessageDialog(E_JuegoScreen.this, "¡Adivinado!");
                        rachaPalabras++;
                        labelRacha.setText("Racha: " + rachaPalabras);
                        if (rachaPalabras == 5) {
                            cambiarTematica();
                        } else {
                            cargarNuevaPalabra();
                        }
                    } else {
                        JOptionPane.showMessageDialog(E_JuegoScreen.this,
                                "Palabra incorrecta. Has perdido.\nLa palabra era: " + partida.getPalabra());
                        dispose();
                        new F_PartidaPerdidaScreen().setVisible(true);
                    }
                } else {
                    char letra = intento.charAt(0);
                    int erroresAntes = partida.getLetrasFallidas().size();
                    partida.intentarLetra(letra);
                    actualizarLabel();
                    int erroresDespues = partida.getLetrasFallidas().size();

                    if (erroresDespues > erroresAntes) {
                        labelErrores.setText("Errores: " + erroresDespues);
                        labelFallidas.setText("Letras fallidas: " + partida.getLetrasFallidas().toString().replaceAll("[\\[\\],]", ""));
                        panelDibujo.repaint();
                        if (erroresDespues >= 6) {
                            JOptionPane.showMessageDialog(E_JuegoScreen.this,
                                    "Has perdido. La palabra era: " + partida.getPalabra());
                            dispose();
                            new F_PartidaPerdidaScreen().setVisible(true);
                        }
                    } else {
                        if (partida.palabraCompleta()) {
                            JOptionPane.showMessageDialog(E_JuegoScreen.this, "¡Adivinado!");
                            rachaPalabras++;
                            labelRacha.setText("Racha: " + rachaPalabras);
                            if (rachaPalabras == 5) {
                                cambiarTematica();
                            } else {
                                cargarNuevaPalabra();
                            }
                        }
                    }
                }
            }
        });

        panelInferior.add(labelIntento);
        panelInferior.add(textFieldIntento);
        panelInferior.add(btnProbar);

        getContentPane().add(panelInferior, BorderLayout.SOUTH);

        cargarNuevaPalabra();
    }

    protected void cargarNuevaPalabra() {
        String nuevaPalabra = TematicaMongo.palabraRandom(tematica);
        if (nuevaPalabra == null || nuevaPalabra.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay palabras para la temática: " + tematica);
            dispose();
            return;
        }
        partida = new Partida(tematica, nuevaPalabra, "Usuario", new ArrayList<>(), new ArrayList<>(), 0);
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
                estado.append(c);
            } else if (letrasAdivinadas.contains(c)) {
                estado.append(Character.toUpperCase(c));
            } else {
                estado.append("_");
            }
            estado.append(" ");
        }

        labelPalabra.setText(estado.toString());
    }

    protected void cambiarTematica() {
    	List<String> tematicas = TematicaMongo.obtenerTodas();
        if (tematicas.size() < 2)
            return;

        Random r = new Random();

        String nuevaTematica;
        do {
            nuevaTematica = tematicas.get(r.nextInt(tematicas.size()));
        } while (nuevaTematica.equalsIgnoreCase(tematica));

        tematica = nuevaTematica;
        rachaPalabras = 0;
        labelTematica.setText("Temática: " + tematica);

        cargarNuevaPalabra();
    }

    protected void dibujarAhorcado(Graphics g, int errores) {
        // Dibujo

        int baseX = 50, baseY = 350;

        g.setColor(Color.BLACK);

        // Base
        g.drawLine(baseX, baseY, baseX + 200, baseY);

        // Palitroque
        g.drawLine(baseX + 50, baseY, baseX + 50, baseY - 300);

        // Palitroque horizontal
        g.drawLine(baseX + 50, baseY - 300, baseX + 150, baseY - 300);

        // Soga
        g.drawLine(baseX + 150, baseY - 300, baseX + 150, baseY - 250);

        if (errores > 0) // Cabeza
            g.drawOval(baseX + 125, baseY - 250, 50, 50);

        if (errores > 1) // Cuerpo
            g.drawLine(baseX + 150, baseY - 200, baseX + 150, baseY - 100);

        if (errores > 2) // Brazo izquierdo
            g.drawLine(baseX + 150, baseY - 180, baseX + 110, baseY - 140);

        if (errores > 3) // Brazo derecho
            g.drawLine(baseX + 150, baseY - 180, baseX + 190, baseY - 140);

        if (errores > 4) // Pierna izquierda
            g.drawLine(baseX + 150, baseY - 100, baseX + 110, baseY - 60);

        if (errores > 5) // Pierna derecha
            g.drawLine(baseX + 150, baseY - 100, baseX + 190, baseY - 60);
    }
}
