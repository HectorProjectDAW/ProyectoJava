package com.proyecto.mi_proyecto;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class D_SelectorTemasScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    protected JPanel contentPane;
    protected JButton btnTema1, btnTema2, btnTema3;
    protected JLabel lblTema1, lblTema2, lblTema3;

    private String convertirImg(String tematica) {
        String nombre = tematica.toLowerCase()
            .replace("á", "a").replace("é", "e").replace("í", "i")
            .replace("ó", "o").replace("ú", "u").replace("ñ", "n")
            .replaceAll("[^a-z0-9]", "_");
        return nombre + ".png";
    }

    private void ajustarEtiqueta(JLabel etiqueta, String texto) {
    	//Fuente del Label
        Font fuente = etiqueta.getFont();
        //Saber cuanto ocupa el texto
        FontMetrics fm = etiqueta.getFontMetrics(fuente);
        //Ancho texto y alto para la etiqueta
        int textoWidth = fm.stringWidth(texto);
        int width = etiqueta.getWidth();
        //Si el texto es mas grande que la etiqueta, disminuye tamaño
        if (textoWidth > width) {
            int newSize = fuente.getSize() - 1;
            while (newSize > 8 && fm.stringWidth(texto) > width) {
                fuente = fuente.deriveFont((float) newSize);
                fm = etiqueta.getFontMetrics(fuente);
                newSize--;
            }
            //Se aplica el nuevo texto
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 550, 350);
        setTitle("Selector de Temáticas");

        contentPane = new JPanel();
        contentPane.setBackground(new Color(192, 192, 192));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Selecciona la temática");
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
        lblTitulo.setBounds(155, 20, 250, 30);
        contentPane.add(lblTitulo);

        
        btnTema1 = new JButton();
        btnTema1.setBounds(29, 64, 150, 169);
        contentPane.add(btnTema1);

        lblTema1 = new JLabel("", JLabel.CENTER);
        lblTema1.setBounds(50, 240, 150, 20);
        contentPane.add(lblTema1);

        btnTema2 = new JButton();
        btnTema2.setBounds(200, 62, 150, 169);
        contentPane.add(btnTema2);

        lblTema2 = new JLabel("", JLabel.CENTER);
        lblTema2.setBounds(210, 240, 150, 20);
        contentPane.add(lblTema2);

        btnTema3 = new JButton();
        btnTema3.setBounds(370, 64, 150, 169);
        contentPane.add(btnTema3);

        lblTema3 = new JLabel("", JLabel.CENTER);
        lblTema3.setBounds(370, 240, 150, 20);
        contentPane.add(lblTema3);

        //Tematicas de tematicamongo.java. Hacer arrays 
        List<String> tematicas = TematicaMongo.Temas(3);
        JButton[] botones = { btnTema1, btnTema2, btnTema3 };
        JLabel[] etiquetas = { lblTema1, lblTema2, lblTema3 };

        //Recorrer lista tematicas
        for (int i = 0; i < tematicas.size(); i++) {
        	//Obtiene el tema
            String tema = tematicas.get(i);
            //Qiotar border, ponerlo en el area correcta
            botones[i].setBorderPainted(false);
            botones[i].setContentAreaFilled(false);
            etiquetas[i].setText(tema);
            ajustarEtiqueta(etiquetas[i], tema);

            // Buscar imagen
            String ruta = "/img/" + convertirImg(tema);
            
            //Buscar imagen dentro del proyecto
            java.net.URL imgUrl = getClass().getResource(ruta);
            if (imgUrl != null) {
            	
            	//Crear icono a partir de la imagen
                ImageIcon iconoOriginal = new ImageIcon(imgUrl);
                
                //Escalar imagen para que salga como el boton
                Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(
                        botones[i].getWidth(), botones[i].getHeight(), Image.SCALE_SMOOTH);
                botones[i].setIcon(new ImageIcon(imagenEscalada));
            } else {
                System.err.println("No se encontró la imagen: " + ruta);
            }

            // Acción del botón
            botones[i].addActionListener(e -> {
            	// Crear ventana del juego pasando la temática seleccionada
                E_JuegoScreen juego = new E_JuegoScreen(tema);
                juego.setVisible(true);
                dispose();
            });
            
        }

        
        JButton btnSalirAlMen = new JButton("Salir al menú");
        btnSalirAlMen.addActionListener(e -> {
            C_MenuPrincipalScreen menu = new C_MenuPrincipalScreen();
            menu.setVisible(true);
            dispose();
        });
        btnSalirAlMen.setBounds(200, 280, 140, 30);
        contentPane.add(btnSalirAlMen);
    }
}
