package com.proyecto.mi_proyecto;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class C_MenuPrincipalScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					C_MenuPrincipalScreen frame = new C_MenuPrincipalScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public C_MenuPrincipalScreen() {
		setBackground(new Color(128, 0, 0));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 350);
		setTitle("Menú Principal");

		contentPane = new JPanel();
		contentPane.setBackground(new Color(192, 192, 192));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnPartidaNueva = new JButton("Nueva partida");
		btnPartidaNueva.setForeground(Color.BLACK);
		btnPartidaNueva.setBounds(160, 40, 180, 30);
		contentPane.add(btnPartidaNueva);

		JButton btnContinuarPartida = new JButton("Continuar partida");
		btnContinuarPartida.setForeground(Color.BLACK);
		btnContinuarPartida.setBounds(160, 80, 180, 30);
		contentPane.add(btnContinuarPartida);

		JButton btnDesafio = new JButton("Desafío");
		btnDesafio.setForeground(Color.BLACK);
		btnDesafio.setBounds(160, 120, 180, 30);
		contentPane.add(btnDesafio);

		JButton btnTienda = new JButton("Tienda");
		btnTienda.setForeground(Color.BLACK);
		btnTienda.setBounds(160, 160, 180, 30);
		contentPane.add(btnTienda);

		JButton btnRanking = new JButton("Ranking");
		btnRanking.setForeground(Color.BLACK);
		btnRanking.setBounds(160, 200, 180, 30);
		contentPane.add(btnRanking);
		
		JButton btnSalirAlEscritorio = new JButton("Salir al escritorio");
		btnSalirAlEscritorio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnSalirAlEscritorio.setBounds(339, 259, 141, 46);
		contentPane.add(btnSalirAlEscritorio);
		
		JButton btnDeslogearte = new JButton("Cerrar sesión");
		btnDeslogearte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				A_LoginScreen login = new A_LoginScreen();
				login.setVisible(true);
				dispose();
			}
		});
		btnDeslogearte.setBounds(12, 259, 141, 46);
		contentPane.add(btnDeslogearte);
	}
}
