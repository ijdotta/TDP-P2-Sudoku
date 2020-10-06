package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import logica.Celda;
import logica.Juego;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.Color;
import java.awt.Dimension;

public class GUI_OLD extends JFrame {

	private JPanel contentPane;
	private JButton selectedButton;
	private Celda selectedCelda;
	private Juego juego;
	private JButton[][] tableroGrafico;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_OLD frame = new GUI_OLD();
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
	public GUI_OLD() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//		contentPane.setLayout(new AbsoluteLayout());
		setContentPane(contentPane);
		
		juego = new Juego("src/files/sk.txt");
		
		setTitle("Sudoku");
//		setResizable(false);
		
		crearPaneles();
	}

	private void crearPaneles() {
		
		crearPanelInformacion();
		crearPanelTablero();
		crearPanelBotonera();
		
	}

	private void crearPanelInformacion() {
		JPanel infoPanel = new JPanel();
		
		infoPanel.setVisible(true);
		infoPanel.setPreferredSize(new Dimension(600,50));
		infoPanel.setBackground(Color.RED);

		contentPane.add(infoPanel);

		
	}

	private void crearPanelTablero() {
		JPanel tablero = new JPanel();
		tablero.setVisible(true);
		tablero.setBackground(Color.PINK);
		tablero.setLayout(new GridLayout(3, 3, 3, 3));
		tablero.setPreferredSize(new Dimension(450,450));
		
		contentPane.add(tablero, BorderLayout.CENTER);
		
		tableroGrafico = new JButton[9][9];
		
		for (int i = 0; i < 9; i++) {
			JPanel panel = new JPanel();
			panel.setVisible(true);
			panel.setBackground(new Color(i,i*10,i*20));
			tablero.add(panel);
			
			panel.setLayout(new GridLayout(3, 3, 1, 1));
			
			for (int j = 0; j < 9; j++) {
				JButton cell = new JButton();
				cell.setBackground(Color.WHITE);
				
				tableroGrafico[i][j] = cell;

//				cell.setBackground(Color.LIGHT_GRAY);
				
				
//				cell.setIcon(juego.getCelda(i, j).getEntidadGrafica().getGrafico());
				
				if (!juego.getCelda(i, j).isEditable())
					cell.setEnabled(false);
				
				cell.setVisible(true);
				cell.setPreferredSize(new Dimension(3,3));
				panel.add(cell);
				
//				cell.setText(i + "," + j);
				cell.setText((i+1)+""+(j+1));
				
//				Image img = juego.getCelda(i, j).getEntidadGrafica().getGrafico().getImage();
//				Image newimg = img.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
//				juego.getCelda(i, j).getEntidadGrafica().getGrafico().setImage(newimg);

				
				int ii = i;
				int jj = j;
				
				cell.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (selectedButton != null && selectedButton.getBackground() == Color.LIGHT_GRAY)
							selectedButton.setBackground(Color.WHITE);
						
						cell.setBackground(Color.LIGHT_GRAY);
						
//						ImageIcon imgIcn = new ImageIcon("src/img/bad-n3.png");
//						
//						cell.setIcon(imgIcn);
//						
//						Image img = imgIcn.getImage();
//						Image newImg = img.getScaledInstance(cell.getWidth(), cell.getHeight(), java.awt.Image.SCALE_SMOOTH);
//						
//						imgIcn.setImage(newImg);
//						
//						cell.repaint();
						
						selectedButton = cell;
						selectedCelda = juego.getCelda(ii, jj);
						actualizarGUI();
					}

					private void actualizarGUI() {
						int i = selectedCelda.getFila(), j = selectedCelda.getColumna();
						
						for (int k = 0; k < 9; k++) {
//							juego.getCelda(i, k).getEntidadGrafica().getCell().setBackground(Color.RED);
//							juego.getCelda(k, j).getEntidadGrafica().getCell().setBackground(Color.RED);
							
							tableroGrafico[i][k].setBackground(Color.GREEN);
							tableroGrafico[k][j].setBackground(Color.GREEN);
							
						}
					}
					
				});
			}
		}
	}

	private void crearPanelBotonera() {
		JPanel botonera = new JPanel();
		botonera.setVisible(true);
		botonera.setBackground(Color.BLUE);
		contentPane.add(botonera, BorderLayout.SOUTH);
		botonera.setPreferredSize(new Dimension(600,50));
		
		JPanel botones = new JPanel();
		botones.setVisible(true);
		botones.setPreferredSize(new Dimension(450, 50));
		botones.setLayout(new GridLayout(1, 9, 1, 1));
		botonera.add(botones);
		
		
		
		for(int i = 1; i <= 10; i++) {
			JButton boton = new JButton();
			boton.setVisible(true);
			boton.setSize(new Dimension(50,50));
			boton.setBackground(new Color(i*20, i*10, i));
//			boton.setBackground(Color.WHITE);
			botones.add(boton);
//			boton.setText(Integer.toString(i));
			boton.setActionCommand(Integer.toString(i));
			
			if (i != 10) {
				ImageIcon icon = new ImageIcon("src/img/n" + i + ".png");
				boton.setIcon(icon);
				Image tmp = icon.getImage();
				icon.setImage(tmp.getScaledInstance(10, 10, java.awt.Image.SCALE_SMOOTH));
				
				boton.repaint();
			}
			else {
				boton.setText("X");
			}
			
			/*
			 * Image image = grafico.getImage();
				if (image != null) {  
					Image newimg = image.getScaledInstance(label.getWidth(), label.getHeight(),  java.awt.Image.SCALE_SMOOTH);
					grafico.setImage(newimg);
					label.repaint();
				}
			 */
			
			boton.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
//					selectedButton.setText(boton.getActionCommand());
					
					ImageIcon icon = new ImageIcon("src/img/n"+boton.getActionCommand()+".png");
					selectedButton.setIcon(icon);
					Image tmp = icon.getImage();
					icon.setImage(tmp.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
					
					selectedButton.repaint();
					selectedButton.setBackground(Color.WHITE);
					selectedButton = null;
					
				}
				
			});
		}
	}

}
