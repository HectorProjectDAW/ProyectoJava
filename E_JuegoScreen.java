package com.proyecto.mi_proyecto;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class E_JuegoScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel labelPalabra;
    private JTextField inputLetra;
    private JLabel labelErrores;
    private JLabel labelTematica;
    private DibujoAhorcado panelDibujo;

    //inicializar variables
    private char[] letrasPalabra;
    private char[] estadoActual;
    private int errores = 0;
    private String palabra;
    private String tematica;
    private List<Character> letrasFallidas = new ArrayList<>();
    private JLabel labelFallidas;
    private JButton btnProbar;
    private JLabel labelRacha;

    //lo de la racha de aciertos y palabras de mongo
    private int aciertosTematica = 0;
    private List<String> todasTematicas = TematicaMongo.obtenerTodas();
    private Random random = new Random();

    public E_JuegoScreen(String tematicaInicial) {
        this.tematica = tematicaInicial;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        labelTematica = new JLabel("Temática: " + tematica);
        labelTematica.setFont(new Font("Dialog", Font.BOLD, 20));
        labelTematica.setBounds(179, 0, 300, 30);
        contentPane.add(labelTematica);

        JLabel lblTitulo = new JLabel("Adivina la palabra:");
        lblTitulo.setBounds(30, 42, 200, 20);
        contentPane.add(lblTitulo);

        labelPalabra = new JLabel("");
        labelPalabra.setFont(new Font("Monospaced", Font.BOLD, 18));
        labelPalabra.setBounds(30, 70, 500, 30);
        contentPane.add(labelPalabra);

        JLabel lblIntroduceLetra = new JLabel("Introduce una letra o palabra:");
        lblIntroduceLetra.setBounds(30, 120, 200, 20);
        contentPane.add(lblIntroduceLetra);

        inputLetra = new JTextField();
        inputLetra.setBounds(230, 120, 100, 25);
        contentPane.add(inputLetra);
        inputLetra.setColumns(10);

        btnProbar = new JButton("Probar");
        btnProbar.setBounds(350, 120, 100, 25);
        contentPane.add(btnProbar);

        labelErrores = new JLabel("Errores: 0");
        labelErrores.setBounds(30, 160, 200, 20);
        contentPane.add(labelErrores);

        labelFallidas = new JLabel("Letras fallidas: ");
        labelFallidas.setBounds(30, 190, 500, 20);
        contentPane.add(labelFallidas);

        labelRacha = new JLabel("Racha: 0");
        labelRacha.setBounds(30, 220, 200, 20);
        contentPane.add(labelRacha);

        panelDibujo = new DibujoAhorcado();
        panelDibujo.setBounds(350, 40, 200, 250);
        contentPane.add(panelDibujo);

        cargarNuevaPalabra();

        btnProbar.addActionListener(e -> {
            if (errores >= 6 || String.valueOf(estadoActual).equals(String.valueOf(letrasPalabra))) return;

            String texto = inputLetra.getText().toUpperCase().trim();
            inputLetra.setText("");

            if (texto.length() > 1) {
                if (texto.equals(palabra)) {
                    estadoActual = letrasPalabra.clone();
                    actualizarLabel();
                    JOptionPane.showMessageDialog(this, "¡Correcto! La palabra era: " + palabra);
                    aciertosTematica++; 
                    labelRacha.setText("Racha: " + aciertosTematica);
                    if (aciertosTematica >= 5) {
                        cambiarTematica();
                    } else {
                        cargarNuevaPalabra();
                    }
                } else {
                    errores = 6;
                    labelErrores.setText("Errores: " + errores);
                    panelDibujo.repaint();
                    JOptionPane.showMessageDialog(this, "Palabra incorrecta. Has perdido. La palabra era: " + palabra);
                    dispose();
                    new F_PartidaPerdidaScreen().setVisible(true);
                }
                return;
            }

            if (texto.length() != 1 || !Character.isLetter(texto.charAt(0))) {
                JOptionPane.showMessageDialog(this, "Introduce una sola letra válida o la palabra completa.");
                return;
            }

            char letra = texto.charAt(0);
            boolean acierto = false;

            for (int i = 0; i < letrasPalabra.length; i++) {
                if (letrasPalabra[i] == letra && estadoActual[i] == '_') {
                    estadoActual[i] = letra;
                    acierto = true;
                }
            }

            if (!acierto) {
                if (!letrasFallidas.contains(letra)) letrasFallidas.add(letra);
                errores++;
                labelErrores.setText("Errores: " + errores);
                labelFallidas.setText("Letras fallidas: " + letrasFallidas.toString().replaceAll("[\\[\\],]", ""));
                panelDibujo.repaint();

                

                if (errores >= 6) {
                    JOptionPane.showMessageDialog(this, "Has perdido. La palabra era: " + palabra);
                    dispose();
                    new F_PartidaPerdidaScreen().setVisible(true);
                }
            } else {
                if (String.valueOf(estadoActual).equals(String.valueOf(letrasPalabra))) {
                    JOptionPane.showMessageDialog(this, "¡Correcto! La palabra era: " + palabra);
                    aciertosTematica++; 
                    labelRacha.setText("Racha: " + aciertosTematica);
                    if (aciertosTematica >= 5) {
                        cambiarTematica();
                    } else {
                        cargarNuevaPalabra();
                    }
                }
            }
            actualizarLabel();
        });
    }

    private void actualizarLabel() {
        StringBuilder cosa = new StringBuilder();
        for (char c : estadoActual) cosa.append(c).append(" ");
        labelPalabra.setText(cosa.toString().trim());
    }

    private void cargarNuevaPalabra() {
    	
    	//Palabra random de mongodb
        palabra = TematicaMongo.palabraRandom(tematica);
        if (palabra == null || palabra.isEmpty()) {
        	//Si ya no hay, suelta esto
            JOptionPane.showMessageDialog(this, "No hay palabras para la temática: " + tematica);
            dispose();
            return;
        }

        //Lo convierte en mayuscula y en charArray
        palabra = palabra.toUpperCase();
        letrasPalabra = palabra.toCharArray();
        estadoActual = new char[letrasPalabra.length];

        //Rellenar con _ los huecos
        for (int i = 0; i < letrasPalabra.length; i++) {
            if (Character.isLetter(letrasPalabra[i])) {
                estadoActual[i] = '_';
            } else {
                estadoActual[i] = letrasPalabra[i];
            }
        }
        
        //Reinicia errores
        errores = 0;
        letrasFallidas.clear();
        labelErrores.setText("Errores: 0");
        labelFallidas.setText("Letras fallidas: ");
        labelRacha.setText("Racha: " + aciertosTematica);
        panelDibujo.repaint();
        actualizarLabel();
    }

    
    //Copiar todas las tematicas en "opciones"
    private void cambiarTematica() {
        List<String> opciones = new ArrayList<>(todasTematicas);
        //Eliminar la actual
        opciones.remove(tematica);
        //Seleccion tematica random
        tematica = opciones.get(random.nextInt(opciones.size()));
        //Actualizar txt
        labelTematica.setText("Temática: " + tematica);
        aciertosTematica = 0;
        labelRacha.setText("Racha: 0");
        cargarNuevaPalabra();
    }

    
    //Dibujo
    private class DibujoAhorcado extends JPanel {
        private static final long serialVersionUID = 1L;

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.drawLine(20, 180, 180, 180);
            g.drawLine(100, 180, 100, 20);
            g.drawLine(100, 20, 160, 20);
            g.drawLine(160, 20, 160, 40);
            if (errores > 0) g.drawOval(140, 40, 40, 40);
            if (errores > 1) g.drawLine(160, 80, 160, 120);
            if (errores > 2) g.drawLine(160, 90, 140, 110);
            if (errores > 3) g.drawLine(160, 90, 180, 110);
            if (errores > 4) g.drawLine(160, 120, 140, 150);
            if (errores > 5) g.drawLine(160, 120, 180, 150);
        }
    }
}
