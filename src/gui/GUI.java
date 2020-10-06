package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logica.Juego;
import javax.swing.border.LineBorder;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JButton[][] tableroGrafico;
	private JButton selCeldaGrafica;
	private Juego juego;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
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
	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		setTitle("Sudoku");
		juego = new Juego("src/files/sk_bien.txt");
		
//		if (juego.getTablero() == null) {
//			finalizarJuego();
//		}
		
		//Colores:
		Color defCeldaColor = Color.WHITE;
		Color defSelCeldaColor = Color.LIGHT_GRAY;
		
		//Disposición general:
		JPanel top, center, bottom;
		top = new JPanel();
		center = new JPanel();
		bottom = new JPanel();

		contentPane.add(top, BorderLayout.NORTH);
		contentPane.add(center, BorderLayout.CENTER);
		contentPane.add(bottom, BorderLayout.SOUTH);
		
		//Paneles del juego:
		JPanel panelInfo, panelTablero, panelBotones;
		Dimension dimPanelesAux = new Dimension(450, 50);
		Dimension dimTablero = new Dimension(450, 450);
		
		panelInfo = new JPanel();
		panelInfo.setPreferredSize(dimPanelesAux);
		top.add(panelInfo);
		panelInfo.setBackground(Color.YELLOW); //Testing
		
		panelBotones = new JPanel();
		panelBotones.setPreferredSize(dimPanelesAux);
		bottom.add(panelBotones);
		panelBotones.setBackground(Color.ORANGE); //Testing
		
		panelTablero = new JPanel();
		panelTablero.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelTablero.setPreferredSize(dimTablero);
		center.add(panelTablero);
		panelTablero.setBackground(Color.BLACK); //Testing
		
		
		//DISEÑO TABLERO
		panelTablero.setLayout(new GridLayout(3, 3, 1, 1));
		tableroGrafico = new JButton[9][9];
		
		JPanel[][] tmpPanel = new JPanel[3][3]; //Solo se usa en la inicialización
		
		//Añadir 9 subpaneles
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				JPanel subPanel = new JPanel();
				subPanel.setLayout(new GridLayout(3, 3));
//				subPanel.setBorder(new LineBorder(Color.GRAY, 1));
				subPanel.setBackground(Color.BLACK); //Testing
				panelTablero.add(subPanel);
				tmpPanel[i][j] = subPanel;
			}
			
		}
		
		//Añadir celdas
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				JPanel subPanel = tmpPanel[i/3][j/3];
				
				JButton celdaGrafica = new JButton();
				tableroGrafico[i][j] = celdaGrafica;
				celdaGrafica.setBackground(Color.WHITE);
//				celdaGrafica.setText(i+""+j); //Testing
				celdaGrafica.setActionCommand(i+","+j);
				subPanel.add(celdaGrafica);
				
				/*IMAGEN JUEGO*/
//				redimensionar(i, j);
				Image img, newimg;
				ImageIcon grafico;
				grafico = juego.getCelda(i, j).getEntidadGrafica().getGrafico();
				celdaGrafica.setIcon(grafico);
				img = grafico.getImage();
				newimg = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
				grafico.setImage(newimg);
				celdaGrafica.repaint();
				
				if (!juego.getCelda(i, j).isEditable()) {
					celdaGrafica.setEnabled(false);
				}
				else {
				
					celdaGrafica.addMouseListener(new MouseAdapter() {
						
						@Override
						public void mouseClicked(MouseEvent e) {
							if (selCeldaGrafica != null) {
								selCeldaGrafica.setBackground(defCeldaColor);
							}
							if (selCeldaGrafica == celdaGrafica) {
								selCeldaGrafica.setBackground(defCeldaColor);
								selCeldaGrafica = null;
							}
							else {
								selCeldaGrafica = celdaGrafica;
								celdaGrafica.setBackground(defSelCeldaColor);
							}
						}
						
					});
					
				}
			}
		}
		
		//DISEÑO PANEL BOTONES
		panelBotones.setLayout(new GridLayout(1, 10));
		for (int i = 0; i < 10; i++) {
			JButton boton = new JButton();
			boton.setText((i+1)+""); //Testing
			boton.setActionCommand(Integer.toString(i+1));
			boton.setBackground(Color.WHITE);
			panelBotones.add(boton);
			
			if (i == 9) {
				boton.setText("X");
				boton.setActionCommand("-1");
			}
			
			boton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (selCeldaGrafica != null) {
						String[] index = selCeldaGrafica.getActionCommand().split(",");
						int i, j, valor;
						i = Integer.parseInt(index[0]);
						j = Integer.parseInt(index[1]);
						valor = Integer.parseInt(boton.getActionCommand());
						
						juego.accionar(juego.getCelda(i, j), valor);
						actualizarImagenes(i, j);
						
						selCeldaGrafica.setBackground(defCeldaColor);
						selCeldaGrafica = null;
					}
				}
			});
		}
	}

	protected void actualizarImagenes(int f, int c) {
		
		
		for (int k = 0; k < 9; k++) {
			//Modificar por fila
			redimensionar(f, k);
			
			//Modificar por columna
			redimensionar(k, c);
		}
		
		//Modificar por panel
		f = f/3; f = f*3;
		c = c/3; c = c*3;
		for (int i = f; i < f+3; i++) {
			for (int j = c; j < c+3; j++) {
				redimensionar(i, j);
			}
		}
		
	}
	
	private void redimensionar(int i, int j) {
		ImageIcon grafico;
		Image img, newimg;
		int width, height;
		
//		width = tableroGrafico[0][0].getWidth();
//		height = tableroGrafico[0][0].getHeight();
		
		width = height = 50;
		
		grafico = juego.getCelda(i, j).getEntidadGrafica().getGrafico();
		img = grafico.getImage();
		newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		grafico.setImage(newimg);
		tableroGrafico[i][j].repaint();
	}

	//actualizarReloj(String s)
	/*Reloj {
	 * 		gui.actualizarReloj(...) ¿?
	 * }
	 */
	
	/*
	 * Error en la carga del archivo: la logica falla, la gráfica de alguna manera lo identifica y luego termina el juego
	 * Pienso con el tablero NULL
	 */

}
