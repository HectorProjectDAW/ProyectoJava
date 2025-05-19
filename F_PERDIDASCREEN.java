package com.proyecto.mi_proyecto;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class F_PartidaPerdidaScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					F_PartidaPerdidaScreen frame = new F_PartidaPerdidaScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public F_PartidaPerdidaScreen() {
		setBackground(new Color(255, 0, 0));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(192, 192, 192));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("¡Has perdido!");
		lblNewLabel.setForeground(new Color(255, 0, 0));
		lblNewLabel.setBackground(new Color(255, 0, 0));
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		lblNewLabel.setBounds(168, 12, 116, 23);
		contentPane.add(lblNewLabel);
		
		JButton btnVolverAJugar = new JButton("Volver a jugar");
		btnVolverAJugar.setBounds(150, 78, 142, 27);
		contentPane.add(btnVolverAJugar);
		
		JButton btnTienda = new JButton("Tienda");
		btnTienda.setBounds(150, 117, 142, 27);
		contentPane.add(btnTienda);
		
		JButton btnSalir = new JButton("Salir");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				C_MenuPrincipalScreen menu = new C_MenuPrincipalScreen();
				menu.setVisible(true);
				dispose();
			}
		});
		btnSalir.setBounds(150, 156, 142, 27);
		contentPane.add(btnSalir);
	}

}
