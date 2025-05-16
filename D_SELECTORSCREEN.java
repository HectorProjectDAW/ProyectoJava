package com.proyecto.mi_proyecto;

package cosaproycto;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class D_SelectorTemasScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private String convertirNombreArchivo(String tematica) {
        String nombre = tematica.toLowerCase()
            .replace("á", "a")
            .replace("é", "e")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ú", "u")
            .replace("ñ", "n")
            .replaceAll("[^a-z0-9]", "_");
        return nombre + ".png";
    }

    private void ajustarEtiqueta(JLabel etiqueta, String texto) {
        Font fuente = etiqueta.getFont();
        FontMetrics fm = etiqueta.getFontMetrics(fuente);
        int textoWidth = fm.stringWidth(texto);
        int width = etiqueta.getWidth();

        if (textoWidth > width) {
            int newSize = fuente.getSize() - 1;
            while (newSize > 8 && fm.stringWidth(texto) > width) {
                fuente = fuente.deriveFont((float) newSize);
                fm = etiqueta.getFontMetrics(fuente);
                newSize--;
            }
            etiqueta.setFont(fuente);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                D_SelectorTemasScreen frame = new D_SelectorTemasScreen();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public D_SelectorTemasScreen() {
        setBackground(new Color(139, 0, 0));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 550, 350);
        setTitle("Selector de Temáticas");

        contentPane = new JPanel();
        contentPane.setBackground(new Color(192, 192, 192));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Selecciona la temática");
        lblTitulo.setForeground(Color.BLACK);
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
        lblTitulo.setBounds(155, 20, 250, 30);
        contentPane.add(lblTitulo);

        // Obtener temáticas aleatorias de MongoDB
        List<String> tematicas = TematicaMongo.Temas(3);

        int anchoBoton = 150;
        int altoBoton = 169;
        int inicioX = 50;
        int espacio = 170;
        int yImagen = 64;
        int yTexto = yImagen + altoBoton + 5;

        for (int i = 0; i < tematicas.size(); i++) {
            String tema = tematicas.get(i);
            int x = inicioX + i * espacio;

            // Crear botón con imagen
            JButton boton = crearBotonImagen(tema, x, yImagen, anchoBoton, altoBoton);
            contentPane.add(boton);

            // Crear etiqueta con el nombre debajo del botón
            JLabel etiquetaTema = new JLabel(tema, JLabel.CENTER);
            etiquetaTema.setBounds(x, yTexto, anchoBoton, 20);
            ajustarEtiqueta(etiquetaTema, tema);
            contentPane.add(etiquetaTema);
        }

        // Botón Salir
        JButton btnSalirAlMen = new JButton("Salir al menú");
        btnSalirAlMen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                C_MenuPrincipalScreen menu = new C_MenuPrincipalScreen();
                menu.setVisible(true);
                dispose();
            }
        });
        btnSalirAlMen.setBounds(200, 265, 140, 30);
        contentPane.add(btnSalirAlMen);
    }

    private JButton crearBotonImagen(String tema, int x, int y, int ancho, int alto) {
        JButton btnImagen = new JButton();

        String ruta = "/img/" + convertirNombreArchivo(tema);
        java.net.URL imgUrl = getClass().getResource(ruta);

        if (imgUrl != null) {
            ImageIcon originalIcon = new ImageIcon(imgUrl);
            Image imagenEscalada = originalIcon.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            btnImagen.setIcon(new ImageIcon(imagenEscalada));
        } else {
            System.err.println("No se encontró la imagen: " + ruta);
        }

        btnImagen.setBounds(x, y, ancho, alto);
        btnImagen.setBorderPainted(false);
        btnImagen.setContentAreaFilled(false);

        btnImagen.addActionListener(e -> {
            new E_JuegoScreen().setVisible(true);
            dispose();
        });

        return btnImagen;
    }
}
