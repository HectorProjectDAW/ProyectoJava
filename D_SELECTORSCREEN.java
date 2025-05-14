package com.proyecto.mi_proyecto;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class D_SelectorTemasScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

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

		JButton btnImagen1 = new JButton("Imagen 1");
		btnImagen1.setBounds(60, 80, 120, 30);
		contentPane.add(btnImagen1);

		JButton btnImagen2 = new JButton("Imagen 2");
		btnImagen2.setBounds(190, 80, 120, 30);
		contentPane.add(btnImagen2);

		JButton btnImagen3 = new JButton("Imagen 3");
		btnImagen3.setBounds(320, 80, 120, 30);
		contentPane.add(btnImagen3);

		JLabel lblTematica1 = new JLabel("Temática 1");
		lblTematica1.setBounds(90, 120, 100, 20);
		contentPane.add(lblTematica1);

		JLabel lblTematica2 = new JLabel("Temática 2");
		lblTematica2.setBounds(220, 120, 100, 20);
		contentPane.add(lblTematica2);

		JLabel lblTematica3 = new JLabel("Temática 3");
		lblTematica3.setBounds(350, 120, 100, 20);
		contentPane.add(lblTematica3);
		
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
}
