package com.proyecto.mi_proyecto;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.sql.SQLException;
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

    private String usuarioActual;

    protected JPanel contentPane;
    protected JButton btnTema1, btnTema2, btnTema3;
    protected JLabel lblTema1, lblTema2, lblTema3;

    
    //Para que las imagenes funcionen bien
    private String convertirImg(String tematica) {
        String nombre = tematica.toLowerCase()
            .replace("á", "a").replace("é", "e").replace("í", "i")
            .replace("ó", "o").replace("ú", "u").replace("ñ", "n")
            .replaceAll("[^a-z0-9]", "_");
        return nombre + ".png";
    }

    //Que se ajsuten a los botones
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
        // //Manera de chatgpt para que quede bien y centrado
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Selector de Temáticas");

        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 240, 240));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        
        JLabel lblTitulo = new JLabel("Selecciona la temática", JLabel.CENTER);
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 32));
        lblTitulo.setBounds(0, 30, getWidth(), 40);
        contentPane.add(lblTitulo);

        
        btnTema1 = new JButton();
        btnTema2 = new JButton();
        btnTema3 = new JButton();
        lblTema1 = new JLabel("", JLabel.CENTER);
        lblTema2 = new JLabel("", JLabel.CENTER);
        lblTema3 = new JLabel("", JLabel.CENTER);

        JButton[] botones = { btnTema1, btnTema2, btnTema3 };
        JLabel[] etiquetas = { lblTema1, lblTema2, lblTema3 };

        
        int anchoBoton = 200;
        int altoBoton = 220;
        int espacio = 60;
        int totalAncho = 3 * anchoBoton + 2 * espacio;
        int xInicial = (getToolkit().getScreenSize().width - totalAncho) / 2;
        int yBotones = 120;
        int yEtiquetas = yBotones + altoBoton + 10;

       
        List<String> tematicas = TematicaMongo.Temas(3);

        for (int i = 0; i < tematicas.size(); i++) {
            String tema = tematicas.get(i);

           
            int x = xInicial + i * (anchoBoton + espacio);
            botones[i].setBounds(x, yBotones, anchoBoton, altoBoton);
            botones[i].setBorderPainted(false);
            botones[i].setContentAreaFilled(false);
            contentPane.add(botones[i]);

            etiquetas[i].setText(tema);
            etiquetas[i].setBounds(x, yEtiquetas, anchoBoton, 30);
            etiquetas[i].setFont(new Font("Dialog", Font.BOLD, 18));
            ajustarEtiqueta(etiquetas[i], tema);
            contentPane.add(etiquetas[i]);

            // Buscar imagen dentro del proyecto
            String ruta = "/img/" + convertirImg(tema);
            java.net.URL imgUrl = getClass().getResource(ruta);
            if (imgUrl != null) {
                ImageIcon iconoOriginal = new ImageIcon(imgUrl);
                Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(
                    botones[i].getWidth(), botones[i].getHeight(), Image.SCALE_SMOOTH);
                botones[i].setIcon(new ImageIcon(imagenEscalada));
            } else {
                System.err.println("No se encontró la imagen: " + ruta);
            }

           
            botones[i].addActionListener(e -> {
                try {
                    UsuarioDAO dao = new UsuarioDAO();
                    dao.resetearEstadisticas(usuarioActual);
                    dao.cerrar();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    
                }

                E_JuegoScreen juego = new E_JuegoScreen(tema, usuarioActual);
                juego.setVisible(true);
                dispose();
            });


        }

       
        JButton btnSalirAlMen = new JButton("Salir al menú");
        btnSalirAlMen.setFont(new Font("Dialog", Font.PLAIN, 18));
        int anchoSalir = 200;
        int altoSalir = 40;
        btnSalirAlMen.setBounds((getToolkit().getScreenSize().width - anchoSalir) / 2,
                                yEtiquetas + 60, anchoSalir, altoSalir);
        btnSalirAlMen.addActionListener(e -> {
        	C_MenuPrincipalScreen menu = new C_MenuPrincipalScreen(usuarioActual);
            menu.setVisible(true);
            dispose();
        });
        contentPane.add(btnSalirAlMen);
    }

   
    public D_SelectorTemasScreen(String usuarioActual) {
        this();  
        this.usuarioActual = usuarioActual;
    }
}
