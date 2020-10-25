package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import logica.Celda;
import logica.Juego;

/**
 * Class GUI - Interfaz gráfica del juego Sudoku
 * @author Ignacio Joaquín Dotta
 */
public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private static Logger logger;
	
	private JPanel contentPane;
	private JButton selCeldaGrafica;
	private Juego juego;
	private JButton[][] tableroGrafico;
	private Timer timer;
	private Color colorCorrecta, colorIncorrecta, colorSeleccionada;

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
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		inicializarLogger();
		
		setTitle("Sudoku"); 
		
		int cantCeldasEliminar = 45;
		int cantidadNumerosJugar = 9;
		juego = new Juego("/resources/soluciones/solucion_sudoku_" + cantidadNumerosJugar + ".txt", cantidadNumerosJugar, cantCeldasEliminar);
		
		if (juego.getTablero() == null) {
			logger.severe("No se puedo iniciar el juego. ");
			finalizarJuego();
		}
		
		//Informacion sobre el juego
		int cantPanelesLinea = juego.cantidadPanelesLinea();
		int cantCeldasLineaPanel = juego.cantidadCeldasLineaPanel();
		int cantCeldasLinea = juego.cantidadCeldasLinea();
		
		
		//DISEÑO GENERAL
		
		//Tamaño de la ventana según la cantidad de celdas
		setBounds(300, 100, 500, 575 + 50*(cantCeldasLinea/10 +1));
		
		//Paleta de colores:
		Color mainBgr = new Color(196, 215, 225);
		Color panelInfoBgr = Color.WHITE;
		Color borderColor = Color.BLACK; 
		Color fondoPanelesTableroColor = borderColor;
		Color panelBotonesBgr = borderColor;
		
		colorCorrecta = Color.WHITE;
		colorIncorrecta = new Color(230,90,69);
		colorSeleccionada = Color.GRAY;
		
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
		Dimension dimPanelInfo, dimTablero, dimPanelBotones; 
		dimPanelInfo = new Dimension(450, 50);
		dimTablero = new Dimension(450, 450); //Lo quiero fijo, si aumenta la cantidad de celdas, éstas serán más pequeñas
		dimPanelBotones = new Dimension(450, 50 * (cantCeldasLinea / 10 + 1));
		
		panelInfo = new JPanel();
		panelInfo.setBorder(new LineBorder(borderColor, 3));
		panelInfo.setPreferredSize(dimPanelInfo);
		top.add(panelInfo);
		panelInfo.setBackground(panelInfoBgr);
		
		panelTablero = new JPanel();
		panelTablero.setBorder(new LineBorder(fondoPanelesTableroColor));
		panelTablero.setPreferredSize(dimTablero);
		center.add(panelTablero);
		panelTablero.setBackground(fondoPanelesTableroColor);
		
		panelBotones = new JPanel();
		panelBotones.setBorder(new LineBorder(borderColor, 3));
		panelBotones.setPreferredSize(dimPanelBotones);
		bottom.add(panelBotones);
		panelBotones.setBackground(panelBotonesBgr);
		
		//FIN DISEÑO GENERAL
		
		
		//DISEÑO PANEL INFORMACION
		
		JPanel clockPanel = new JPanel();
		JLabel [] timeDisplay = new JLabel[8];
		JLabel digit;
		Dimension clockPanelDim, digitDim;
		
		digitDim = new Dimension(22, 44);
		clockPanelDim = new Dimension( (int)digitDim.getWidth() * timeDisplay.length, (int) digitDim.getHeight());
		
		clockPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		clockPanel.setPreferredSize(clockPanelDim);
		clockPanel.setBackground(panelInfoBgr);
		
		for (int i = 0; i < timeDisplay.length; i++) {
			digit = timeDisplay[i] = new JLabel();
			digit.setVisible(true);
			digit.setPreferredSize(digitDim);
			clockPanel.add(digit);
		}
		
		panelInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		clockPanel.setVisible(true);
		panelInfo.add(clockPanel);
		
		//Inserta los ':' separadores del reloj
		ImageIcon icon = new ImageIcon(GUI.class.getResource("/resources/img/Reloj/separador.png"));
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance((int)digitDim.getWidth(), (int) digitDim.getHeight(), java.awt.Image.SCALE_SMOOTH);
		icon.setImage(newimg);
		timeDisplay[2].setIcon(icon); 
		timeDisplay[5].setIcon(icon);
		
		configurarReloj(timeDisplay);
		
		//FIN DISEÑO PANEL INFO
		
		
		//DISEÑO PANEL TABLERO
		panelTablero.setLayout(new GridLayout(cantPanelesLinea, cantPanelesLinea, 1, 1));
		
		tableroGrafico = new JButton[cantCeldasLinea][cantCeldasLinea];
		JPanel[][] tmpPanel = new JPanel[cantPanelesLinea][cantPanelesLinea];
		Dimension dimCelda = new Dimension((int) dimTablero.getWidth()/cantCeldasLinea,
										   (int) dimTablero.getHeight()/cantCeldasLinea);
		
		//Añadir subpaneles
		for (int i = 0; i < cantPanelesLinea; i++) {
			for (int j = 0; j < cantPanelesLinea; j++) {
				JPanel subPanel = new JPanel();
				subPanel.setLayout(new GridLayout(cantCeldasLineaPanel, cantCeldasLineaPanel));
				subPanel.setBackground(fondoPanelesTableroColor);
				panelTablero.add(subPanel);
				tmpPanel[i][j] = subPanel;
			}
			
		}
		
		//Añadir celdas
		for (int i = 0; i < cantCeldasLinea; i++) {
			for (int j = 0; j < cantCeldasLinea; j++) {
				JPanel subPanel = tmpPanel[i/cantPanelesLinea][j/cantPanelesLinea];
				JButton celdaGrafica = new JButton();
				
				tableroGrafico[i][j] = celdaGrafica;
				
				celdaGrafica.setPreferredSize(dimCelda);
				celdaGrafica.setActionCommand(i+","+j);
				celdaGrafica.setIcon(juego.getCelda(i, j).getEntidadGrafica().getGrafico());
				subPanel.add(celdaGrafica);
				
				if (!juego.getCelda(i, j).isEditable()) {
					celdaGrafica.setEnabled(false);
				}
				
				celdaGrafica.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
						//Selecciona si no había seleccionada
						if (selCeldaGrafica == null) {
							selCeldaGrafica = celdaGrafica;
							selCeldaGrafica.setBackground(colorSeleccionada);
						}
						else {
							
							//Deselecciona la actual
							String[] index = selCeldaGrafica.getActionCommand().split(",");
							int f, c;
							
							f = Integer.parseInt(index[0]);
							c = Integer.parseInt(index[1]);
							
							//Restablecer color a "no seleccionada"
							Color bgr = juego.getCelda(f, c).isCorrecta() ? colorCorrecta : colorIncorrecta;
							selCeldaGrafica.setBackground(bgr);
							
							if (selCeldaGrafica == celdaGrafica) {
								selCeldaGrafica = null;
							}
							//Selecciona una celda nueva si no se presionó la misma
							else {
								selCeldaGrafica = celdaGrafica;
								selCeldaGrafica.setBackground(colorSeleccionada);
							}
						}
					}
				});
				
			}
		}
		
		actualizarTablero();
		//FIN DISEÑO PANEL TABLERO
		
		//DISEÑO PANEL BOTONES
		ImageIcon newIcon;
		Image newimage;
		Dimension dimBotonAccion = new Dimension(50, 50);
		
		panelBotones.setLayout(new GridLayout(cantCeldasLinea/10 + 1, cantCeldasLinea+1));
		
		for (int i = 0; i < cantCeldasLinea+1; i++) {
			JButton boton = new JButton();
			
			boton.setPreferredSize(dimBotonAccion);
			boton.setBackground(Color.WHITE);
			panelBotones.add(boton);
			
			if (i < cantCeldasLinea) {
				if (1 <= i+1 && i+1 <= 9)
					newIcon = new ImageIcon(GUI.class.getResource("/resources/img/Numeros/n" + (i+1) + ".png"));
				else {
					/*
					 * Esto solo lo hago para que el programa se ejecute y pueda verse como se adapta la GUI
					 * si se quisieran jugar con una mayor cantidad de valores, pero no se incluyen las imagenes
					 * que representan valores > 9.
					 * Claramente, algunas imágenes con valor n representaran a n y otras representaran a 9+n. 
					 */
					newIcon = new ImageIcon(GUI.class.getResource("/resources/img/Numeros/n" + ((i+1) % 9) + ".png"));
				}
				boton.setActionCommand(Integer.toString(i+1));
			}
			else {
				newIcon = new ImageIcon(GUI.class.getResource("/resources/img/Numeros/borrar.png"));
				boton.setActionCommand("0");
			}
			
			//Establecer imagen
			float scale_factor = 0.4f;
			int width, height;
			width = (int) dimBotonAccion.getWidth();
			height = (int) dimBotonAccion.getHeight();
			newimage = newIcon.getImage().getScaledInstance( width - (int) (width * scale_factor), 
															 height - (int) (height * scale_factor), 
															java.awt.Image.SCALE_SMOOTH);
			newIcon.setImage(newimage);
			boton.setIcon(newIcon);
			
			//Acción para insertar números
			boton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (selCeldaGrafica != null) {
						String[] index = selCeldaGrafica.getActionCommand().split(",");
						int i, j, valor;
						
						i = Integer.parseInt(index[0]);
						j = Integer.parseInt(index[1]);
						valor = Integer.parseInt(boton.getActionCommand());
						juego.accionar(juego.getCelda(i, j), valor);
						
						actualizarTablero();
						selCeldaGrafica = null;
						
						if (juego.isVictoria()) {
							victoria();
						}
					}
				}
			});
			
		}
		
		//FIN DISEÑO PANEL BOTONES
	}
	
	/**
	 * Método auxiliar para configurar reloj del juego.
	 * @param timeDisplay Arreglo de labels de dígitos de reloj.
	 */
	private void configurarReloj(JLabel[] timeDisplay) {
		timer = new Timer();
		
		int refreshRate = 1000;
		
		TimerTask actualizarReloj = new TimerTask() {
			
			@Override
			public void run() {
				Duration d = juego.tiempoTranscurrido();
				String time = String.format("%02d:%02d:%02d", 
		                					d.toHours(), 
		                					d.toMinutesPart(), 
		                					d.toSecondsPart());
				JLabel digit;
				Dimension dim;
				ImageIcon icon;
				Image img, newimg;
				
				for (int i = timeDisplay.length-1; i >= 0; i--) {
					digit = timeDisplay[i]; 
					
					if (i != 2 && i != 5) {
					
						dim = digit.getPreferredSize();
						int width = (int) dim.getWidth();
						int height = (int) dim.getHeight();

						icon = new ImageIcon(GUI.class.getResource("/resources/img/Reloj/d" + time.charAt(i) + ".png"));
						img = icon.getImage();
						newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
						icon.setImage(newimg);
						digit.setIcon(icon);
						
					}
				}
			}
		};
		
		timer.schedule(actualizarReloj, 0, refreshRate);
		
	}

	/**
	 * Actualiza la imagen, el tamaño y el color de las celdas del tablero.
	 */
	private void actualizarTablero() {
		JButton celdaGrafica;
		Celda celda;
		Dimension dim;
		ImageIcon grafico;
		Image img, newimg;
		Color bgr;
		int width, height;
		float scale_factor = 0.2f;
		
		for (int i = 0; i < tableroGrafico.length; i++) {
			for (int j = 0; j < tableroGrafico[i].length; j++) {
				
				celdaGrafica = tableroGrafico[i][j];
				celda = juego.getCelda(i, j);
				
				//Actualizar color según si el valor es correcto
				bgr = celda.isCorrecta() ? colorCorrecta : colorIncorrecta;
				celdaGrafica.setBackground(bgr);
				
				//Actualizar imagen del número (solo si ha cambiado)
				if (celda.isModificada()) {
					celda.setModificada(false);
					
					dim = celdaGrafica.getPreferredSize();
					width = (int) dim.getWidth();
					height = (int) dim.getHeight();
					
					grafico = celda.getEntidadGrafica().getGrafico();
					img = celda.getEntidadGrafica().getGrafico().getImage();
					newimg = img.getScaledInstance(width - (int) (width * scale_factor), 
												   height - (int) (height * scale_factor), 
												   java.awt.Image.SCALE_SMOOTH);
					grafico.setImage(newimg);
					
				}
				
				celdaGrafica.repaint();
				
			}
		}
	}

	/**
	 * Comportamiento al detectar que el jugador finalizó el juego correctamente.
	 */
	private void victoria() {
		for (int i = 0; i < tableroGrafico.length; i++) {
			for (int j = 0; j < tableroGrafico.length; j++) {
				tableroGrafico[i][j].setBackground(new Color(117, 213, 100));
				tableroGrafico[i][j].setEnabled(false);
			}
		}
		
		timer.cancel();
		
		String [] options = {"Sí", "No"};
		int res = JOptionPane.showOptionDialog(this, "¡Felicitaciones!\n" + 
													 "Ha completado el tablero correctamente.\n" + 
													 "¿Desea salir?", 
													 "Fin del juego", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		
		logger.fine("res = " + res);
		
		if (res == 0)
			System.exit(0);
	}

	/**
	 * Permite finaliar el juego cuando se detecta un error interno.
	 */
	private void finalizarJuego() {
		if (timer != null)
			timer.cancel();
		JOptionPane.showMessageDialog(this, "Ha ocurrido un error en la carga del juego. ");
		System.exit(0);
	}
	
	/**
	 * Configura el logger de la GUI.
	 */
	private void inicializarLogger() {
		if (logger == null) {
			
			logger = Logger.getLogger(GUI.class.getName());
			
			Handler hnd = new ConsoleHandler();
			hnd.setLevel(Level.ALL);
			logger.addHandler(hnd);
			
			logger.setLevel(Level.ALL);
			
			Logger rootLoger = logger.getParent();
			for (Handler h : rootLoger.getHandlers())
				h.setLevel(Level.OFF);
		}
	}

}
