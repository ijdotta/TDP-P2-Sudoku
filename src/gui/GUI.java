package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logica.Juego;
import javax.swing.border.LineBorder;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
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
		juego = new Juego("src/files/sk_bien.txt", 9);
		
//		if (juego.getTablero() == null) {
//			finalizarJuego(false);
//		}
		
		//Paleta de colores:
		Color mainBgr = new Color(80, 150, 255);
		Color panelBgr = Color.WHITE; //new Color(130, 200, 255);
		
		//Disposición general:
		JPanel top, center, bottom;
		top = new JPanel();
		center = new JPanel();
		bottom = new JPanel();

		contentPane.setBackground(mainBgr);
		top.setBackground(mainBgr);
		center.setBackground(mainBgr);
		bottom.setBackground(mainBgr);
		
		contentPane.add(top, BorderLayout.NORTH);
		contentPane.add(center, BorderLayout.CENTER);
		contentPane.add(bottom, BorderLayout.SOUTH);
		
		//Paneles del juego:
		JPanel panelInfo, panelTablero, panelBotones;
		Dimension dimPanelesAux = new Dimension(450, 50);
		Dimension dimTablero = new Dimension(450, 450);
		
		panelInfo = new JPanel();
		panelInfo.setBorder(new LineBorder(Color.BLACK, 2));
		panelInfo.setPreferredSize(dimPanelesAux);
		top.add(panelInfo);
		panelInfo.setBackground(panelBgr); //Testing
		
		panelBotones = new JPanel();
		panelBotones.setPreferredSize(dimPanelesAux);
		bottom.add(panelBotones);
		panelBotones.setBackground(panelBgr); //Testing
		
		panelTablero = new JPanel();
		panelTablero.setBorder(new LineBorder(Color.BLACK));
		panelTablero.setPreferredSize(dimTablero);
		center.add(panelTablero);
		panelTablero.setBackground(Color.BLACK); //Testing
		
		//DISEÑO PANEL INFORMACIÓN
		JPanel clockPanel = new JPanel();
		JLabel [] timeDisplay = new JLabel[8];
		JLabel digit;
		Dimension clockPanelDim, digitDim;
		
		digitDim = new Dimension(25, 25);
		clockPanelDim = new Dimension( (int)digitDim.getWidth() * timeDisplay.length, (int) digitDim.getHeight());
		
		clockPanel.setLayout(new FlowLayout());
		clockPanel.setPreferredSize(clockPanelDim);
		clockPanel.setBackground(panelBgr);
		
		for (int i = 0; i < timeDisplay.length; i++) {
			digit = timeDisplay[i] = new JLabel();
			digit.setVisible(true);
			digit.setPreferredSize(digitDim);
			digit.setBackground(Color.BLUE);
			clockPanel.add(digit);
		}
		
		panelInfo.setLayout(null);
		clockPanel.setBounds(100, 12, 250, 26);
		clockPanel.setVisible(true);
		panelInfo.add(clockPanel);
		
		
		configurarReloj(timeDisplay);
		
		//DISEÑO TABLERO
		panelTablero.setLayout(new GridLayout(3, 3, 1, 1));
		
		JPanel[][] tmpPanel = new JPanel[3][3]; //Solo se usa en la inicialización
		
		//Tratar de definir la cantidad de celdas en funcion del juego
		
		//Añadir 9 subpaneles
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				JPanel subPanel = new JPanel();
				subPanel.setLayout(new GridLayout(3, 3));
				subPanel.setBackground(Color.BLACK); //Testing
				panelTablero.add(subPanel);
				tmpPanel[i][j] = subPanel;
			}
			
		}
		
		//Añadir celdas
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				JPanel subPanel = tmpPanel[i/3][j/3];
				JButton celdaGrafica = juego.getCelda(i, j).getEntidadGrafica().getCeldaGrafica();

				celdaGrafica.setActionCommand(i+","+j);
				subPanel.add(celdaGrafica);
				
				if (!juego.getCelda(i, j).isEditable()) {
					celdaGrafica.setEnabled(false);
				}
				
				celdaGrafica.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
						//Si no es null
							//Si es la misma -> solo eliminar seleccion
							//Si es distinta -> eliminar seleccion y seleccionar nueva
						//Sino
							//Seleccionar nueva
						
						//Selecciona si no había seleccionada
						if (selCeldaGrafica == null) {
							selCeldaGrafica = celdaGrafica;
							selCeldaGrafica.setBackground(Color.GRAY);
						}
						else {
							
							String[] index = selCeldaGrafica.getActionCommand().split(",");
							int f, c;
							
							f = Integer.parseInt(index[0]);
							c = Integer.parseInt(index[1]);
							
							//Desselecciona la anterior
							Color bgr = juego.getCelda(f, c).isCorrecta() ? Color.WHITE : Color.RED;
							selCeldaGrafica.setBackground(bgr);
							
							if (selCeldaGrafica == celdaGrafica) {
								selCeldaGrafica = null;
							}
							else {
								selCeldaGrafica = celdaGrafica;
								selCeldaGrafica.setBackground(Color.GRAY);
							}
						}
						
					}
					
				});
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
			
			boton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (selCeldaGrafica != null) {
						Color bgr;
						String[] index = selCeldaGrafica.getActionCommand().split(",");
						int i, j, valor;
						
						i = Integer.parseInt(index[0]);
						j = Integer.parseInt(index[1]);
						valor = Integer.parseInt(boton.getActionCommand());
						
						juego.accionar(juego.getCelda(i, j), valor);
						
						bgr = juego.getCelda(i, j).isCorrecta() ? Color.WHITE : Color.RED;
						selCeldaGrafica.setBackground(bgr);
						selCeldaGrafica = null;
						
						//Testing
						finalizarJuego("Victoria!");
						
					}
				}
			});
			
		}
	}
	
	private void configurarReloj(JLabel[] timeDisplay) {
		Timer timer = new Timer();
		Clock clock = new Clock();
		
		int refreshRate = 1000; //in ms
		
		TimerTask actualizarReloj = new TimerTask() {
			
			@Override
			public void run() {
				String time = clock.getTime();
				
				for (int i = timeDisplay.length-1; i >= 0; i--) {
					timeDisplay[i].setText(time.charAt(i)+"");
				}
				
				System.out.println("Reloj GUI :: " + clock.getTime());
				
			}
		};
		
		timer.schedule(actualizarReloj, 0, refreshRate);
		
	}

	private void finalizarJuego(String msg) {
		
		JOptionPane.showMessageDialog(this, msg);
		int res = JOptionPane.showConfirmDialog(contentPane, "Salir?");
		
		if (res == 0)
			System.exit(0);
		else {
			//bloquear tablero
		}
		System.out.println(res);
//		System.exit(0);
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
