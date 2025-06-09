package com.proyecto.mi_proyecto;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

public class C_MenuPrincipalScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private JLabel titulo;
    private JButton btnPartidaNueva;
    private JButton btnContinuarPartida;
    private JButton btnDesafio;
    private JButton btnRanking;
    private JButton btnDeslogearte;
    private JButton btnSalir;
    private JPanel contentPane;
    private Locale currentLocale;
    private String usuarioActual;
    private JComboBox<String> comboIdioma;

    @SuppressWarnings("deprecation")
	public C_MenuPrincipalScreen(String usuarioActual) {
        this.usuarioActual = usuarioActual;

        this.currentLocale = Messages.getCurrentLocale();

        // Configuración básica ventana
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Menú Principal");

        contentPane = new JPanel(new GridBagLayout());
        contentPane.setBackground(new Color(230, 230, 250));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        Font fuenteBoton = new Font("SansSerif", Font.BOLD, 18);

        // Título
        titulo = new JLabel();
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(new Color(60, 60, 60));
        GridBagConstraints gbcTitulo = new GridBagConstraints();
        gbcTitulo.gridx = 0;
        gbcTitulo.gridy = 0;
        gbcTitulo.insets = new Insets(20, 0, 30, 0);
        contentPane.add(titulo, gbcTitulo);

        // Panel botones
        JPanel panelBotones = new JPanel(new GridLayout(0, 1, 20, 20));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(40, 150, 40, 150));

        btnPartidaNueva = crearBoton("", fuenteBoton, e -> {
            D_SelectorTemasScreen selecTema = new D_SelectorTemasScreen(usuarioActual);
            selecTema.setVisible(true);
            dispose();
        });

        btnContinuarPartida = crearBoton("", fuenteBoton, e -> continuarPartidaGuardada());

        btnDesafio = crearBoton("", fuenteBoton, e -> {
            PantallaConexion pantallaConexion = new PantallaConexion();
            pantallaConexion.setVisible(true);
            dispose();
        });

        btnRanking = crearBoton("", fuenteBoton, e -> {
            H_RankingScreen rankingScreen = new H_RankingScreen();
            rankingScreen.setVisible(true);
        });

        if ("Invitado".equalsIgnoreCase(usuarioActual)) {
            panelBotones.add(btnDesafio);
        } else {
            panelBotones.add(btnPartidaNueva);
            panelBotones.add(btnContinuarPartida);
            panelBotones.add(btnDesafio);
            panelBotones.add(btnRanking);
        }

        GridBagConstraints gbcBotones = new GridBagConstraints();
        gbcBotones.gridx = 0;
        gbcBotones.gridy = 1;
        gbcBotones.anchor = GridBagConstraints.CENTER;
        contentPane.add(panelBotones, gbcBotones);

        // Panel inferior con botones salir y deslogear
        JPanel panelInferior = new JPanel(new BorderLayout(20, 0));
        panelInferior.setBackground(Color.WHITE);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));

        btnDeslogearte = crearBoton("", fuenteBoton, e -> {
            A_LoginScreen login = new A_LoginScreen();
            login.setVisible(true);
            dispose();
        });
        panelInferior.add(btnDeslogearte, BorderLayout.WEST);

        btnSalir = crearBoton("", fuenteBoton, e -> System.exit(0));
        panelInferior.add(btnSalir, BorderLayout.EAST);

        GridBagConstraints gbcInferior = new GridBagConstraints();
        gbcInferior.gridx = 0;
        gbcInferior.gridy = 2;
        gbcInferior.insets = new Insets(40, 0, 0, 0);
        contentPane.add(panelInferior, gbcInferior);

        // Combo para selección idioma - lo posicionamos arriba a la derecha
        comboIdioma = new JComboBox<>(new String[]{"Español", "English"});
        comboIdioma.setSelectedIndex(currentLocale.getLanguage().equals("en") ? 1 : 0);
        comboIdioma.addActionListener(e -> {
            String seleccionado = (String) comboIdioma.getSelectedItem();
            Locale nuevoLocale = "English".equals(seleccionado) ? new Locale("en", "US") : new Locale("es", "ES");
            cambiarIdioma(nuevoLocale);
        });

        GridBagConstraints gbcComboIdioma = new GridBagConstraints();
        gbcComboIdioma.gridx = 0;
        gbcComboIdioma.gridy = 3;
        gbcComboIdioma.anchor = GridBagConstraints.EAST;
        gbcComboIdioma.insets = new Insets(20, 0, 0, 20);
        contentPane.add(comboIdioma, gbcComboIdioma);

        actualizarTextos();
    }

    private void cambiarIdioma(Locale locale) {
        this.currentLocale = locale;
        Messages.loadLocale(locale); // Establece idioma global
        actualizarTextos();
    }

    private JButton crearBoton(String texto, Font fuente, ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.setFont(fuente);
        boton.setBackground(new Color(100, 149, 237));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.addActionListener(accion);
        return boton;
    }

    private void actualizarTextos() {
        ResourceBundle bundle = Messages.labels();

        titulo.setText(bundle.getString("label.tituloMenu"));
        btnPartidaNueva.setText(bundle.getString("button.nuevaPartida"));
        btnContinuarPartida.setText(bundle.getString("button.continuarPartida"));
        btnDesafio.setText(bundle.getString("button.desafio"));
        btnRanking.setText(bundle.getString("button.ranking"));
        btnDeslogearte.setText(bundle.getString("button.cerrarSesion"));
        btnSalir.setText(bundle.getString("button.salir"));
    }

    //Cargar cosas desde PartidaDAO
    private void continuarPartidaGuardada() {
        if ("Invitado".equalsIgnoreCase(usuarioActual)) {
            JOptionPane.showMessageDialog(this, "No se pueden cargar partidas guardadas en modo Invitado.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            Partida partidaGuardada = PartidaDAO.cargarPartida(usuarioActual);
            if (partidaGuardada == null) {
                JOptionPane.showMessageDialog(this, "No hay partida guardada para el usuario.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            E_JuegoScreen juego = new E_JuegoScreen(partidaGuardada);
            juego.setVisible(true);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la partida guardada: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
