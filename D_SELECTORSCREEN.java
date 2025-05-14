package com.proyecto.mi_proyecto;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
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
        return nombre + ".jpg";
    }

    // Método para ajustar la fuente del JLabel
    private void ajustarEtiqueta(JLabel etiqueta, String texto) {
        Font fuente = etiqueta.getFont();
        FontMetrics fm = etiqueta.getFontMetrics(fuente);
        int textoWidth = fm.stringWidth(texto);
        int width = etiqueta.getWidth();

        // Si el texto es demasiado grande, reducir el tamaño de la fuente
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
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    D_SelectorTemasScreen frame = new D_SelectorTemasScreen();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public D_SelectorTemasScreen() {
        setBackground(new Color(139, 0, 0));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 300);
        setTitle("Selector de Temáticas");

        contentPane = new JPanel();
        contentPane.setBackground(new Color(192, 192, 192));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Selecciona la temática");
        lblTitulo.setForeground(Color.BLACK);
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
        lblTitulo.setBounds(145, 20, 250, 30);
        contentPane.add(lblTitulo);

        // Obtener temáticas aleatorias de MongoDB
        List<String> tematicas = TematicaMongo.Temas(3);
        String tema1 = tematicas.size() > 0 ? tematicas.get(0) : "Temática 1";
        String tema2 = tematicas.size() > 1 ? tematicas.get(1) : "Temática 2";
        String tema3 = tematicas.size() > 2 ? tematicas.get(2) : "Temática 3";

        // Crear los botones con las imágenes de las temáticas
        JButton btnImagen1 = crearBotonImagen(tema1, 58, 64);
        JButton btnImagen2 = crearBotonImagen(tema2, 190, 62);
        JButton btnImagen3 = crearBotonImagen(tema3, 320, 62);

        // Añadir los botones al panel
        contentPane.add(btnImagen1);
        contentPane.add(btnImagen2);
        contentPane.add(btnImagen3);

        // Botón Salir
        JButton btnSalirAlMen = new JButton("Salir al menú");
        btnSalirAlMen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                C_MenuPrincipalScreen menu = new C_MenuPrincipalScreen();
                menu.setVisible(true);
                dispose();
            }
        });
        btnSalirAlMen.setBounds(190, 225, 120, 30);
        contentPane.add(btnSalirAlMen);
    }

    // Método para crear el botón con la imagen
    private JButton crearBotonImagen(String tema, int x, int y) {
        JButton btnImagen = new JButton();
        String ruta = "/img/" + convertirNombreArchivo(tema);
        java.net.URL img = getClass().getResource(ruta);
        if (img != null) {
            btnImagen.setIcon(new ImageIcon(img));
        } else {
            System.err.println("No se encontró la imagen: " + ruta);
        }
        btnImagen.setBounds(x, y, 120, 139);
        btnImagen.addActionListener(e -> {
            new E_JuegoScreen().setVisible(true); 
            dispose();
        });
        return btnImagen;
    }
}
