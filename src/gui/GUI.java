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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

import logica.Celda;
import logica.Juego;
import javax.swing.border.LineBorder;

/**
 * 
 * @author ignac
 *
 * OBSERVACIÓN: el tamaño de la ventana y la creación de celdas, paneles, botones, etc. varía según la cantidad de números
 * con la que se desee jugar (siempre y cuando la cantidad x sea tal que x = n^2 para 1 <= n <= x) por sugerencia de José.
 * 
 * En los archivos fuente se incluye un tablero de 16x16 y uno de 25x25 para verlo en funcionamiento, pero no así las imagenes
 * de dichos números.
 */
public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private static Logger logger;
	
	private JPanel contentPane;
	private JButton selCeldaGrafica;
	private Juego juego;
	private JButton[][] tableroGrafico;
	private Timer timer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
//					frame.actualizarTablero();
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
		juego = new Juego("src/files/sk_bien.txt", 9, 15);
		
		if (juego.getTablero() == null) {
			logger.severe("No se puedo iniciar el juego. ");
			finalizarJuego();
		}
		
		//Informacion sobre el juego
		int cantPanelesLinea = juego.cantidadPanelesLinea();
		int cantCeldasLineaPanel = juego.cantidadCeldasLineaPanel();
		int cantCeldasLinea = juego.cantidadCeldasLinea();
		
		
		//DISEÑO GENERAL
		
		//Tamaño de la ventana segun la cantidad de celdas
		setBounds(300, 100, 500, 575 + 50*(cantCeldasLinea/10 +1));
		
		//Paleta de colores:
//		Color mainBgr = new Color(80, 150, 255);
		Color mainBgr = new Color(0, 152, 199);
		Color panelBgr = Color.WHITE; //new Color(130, 200, 255);
		Color bordeCeldasColor = Color.BLACK;
		
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
		dimTablero = new Dimension(450, 450);
		dimPanelBotones = new Dimension(450, 50 * (cantCeldasLinea / 10 + 1));
		
		panelInfo = new JPanel();
		panelInfo.setBorder(new LineBorder(Color.BLACK, 3));
		panelInfo.setPreferredSize(dimPanelInfo);
		top.add(panelInfo);
		panelInfo.setBackground(panelBgr); //Testing
		
		panelTablero = new JPanel();
		panelTablero.setBorder(new LineBorder(bordeCeldasColor));
		panelTablero.setPreferredSize(dimTablero);
		center.add(panelTablero);
		panelTablero.setBackground(bordeCeldasColor); //Testing
		
		panelBotones = new JPanel();
		panelBotones.setBorder(new LineBorder(Color.BLACK, 3));
		panelBotones.setPreferredSize(dimPanelBotones);
		bottom.add(panelBotones);
		panelBotones.setBackground(panelBgr); //Testing
		
		//FIN DISEÑO GENERAL
		
		
		//DISEÑO PANEL INFO
		
		JPanel clockPanel = new JPanel();
		JLabel [] timeDisplay = new JLabel[8];
		JLabel digit;
		Dimension clockPanelDim, digitDim;
		
		digitDim = new Dimension(44, 44);
		clockPanelDim = new Dimension( (int)digitDim.getWidth() * timeDisplay.length, (int) digitDim.getHeight());
		
		clockPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		clockPanel.setPreferredSize(clockPanelDim);
		clockPanel.setBackground(panelBgr);
		
		for (int i = 0; i < timeDisplay.length; i++) {
			digit = timeDisplay[i] = new JLabel();
			digit.setVisible(true);
			digit.setPreferredSize(digitDim);
			digit.setBackground(Color.BLUE);
			clockPanel.add(digit);
		}
		
		panelInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		clockPanel.setVisible(true);
		panelInfo.add(clockPanel);
		
		//Colons:
		ImageIcon icon = new ImageIcon("src/img/ClockNumbers/colon.png");
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		icon.setImage(newimg);
		timeDisplay[2].setIcon(icon); timeDisplay[5].setIcon(icon);
		//Colons-
		
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
				subPanel.setBackground(Color.BLACK); //Testing
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
		
		actualizarTablero();
		//FIN DISEÑO PANEL TABLERO
		
		//DISEÑO PANEL BOTONES
		panelBotones.setLayout(new GridLayout(cantCeldasLinea/10 + 1, cantCeldasLinea+1));
		for (int i = 0; i < cantCeldasLinea+1; i++) {
			JButton boton = new JButton();
			boton.setActionCommand(Integer.toString(i+1));
			boton.setBackground(Color.WHITE);
			panelBotones.add(boton);
			
			//Establecer imagen
			if (i != cantCeldasLinea) { //TODO acomodar esto
			ImageIcon newIcon = new ImageIcon("src/img/n" + (i+1) + ".png");
			Image newimage = newIcon.getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
			newIcon.setImage(newimage);
			boton.setIcon(newIcon);
			}
			if (i == cantCeldasLinea) {
				boton.setText("X");
				boton.setActionCommand("-1");
			}
			
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
	}
	
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

	private void configurarReloj(JLabel[] timeDisplay) {
		timer = new Timer();
		Clock clock = new Clock();
		
		int refreshRate = 1000; //in ms
		
		TimerTask actualizarReloj = new TimerTask() {
			
			@Override
			public void run() {
				String time = clock.getTime();
				JLabel digit;
				Dimension dim;
				
				for (int i = timeDisplay.length-1; i >= 0; i--) {
					digit = timeDisplay[i]; 
					
					if (i != 2 && i != 5) {
					
						dim = digit.getPreferredSize();
						int width = (int) dim.getWidth();
						int height = (int) dim.getHeight();
						
						ImageIcon icon = new ImageIcon("src/img/ClockNumbers/Clock (" + time.charAt(i) + ").png");
						Image img = icon.getImage();
						Image newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
						icon.setImage(newimg);
						digit.setIcon(icon);
						
					}
//					timeDisplay[i].setText(time.charAt(i)+""); TODO
				}
				
//				System.out.println("Reloj GUI :: " + clock.getTime());
				
			}
		};
		
		timer.schedule(actualizarReloj, 0, refreshRate);
		
	}

	private void finalizarJuego() {
		if (timer != null)
			timer.cancel();
		JOptionPane.showMessageDialog(this, "Ha ocurrido un error en la carga del juego. ");
		System.exit(0);
	}
	
	private void victoria() {
		for (int i = 0; i < tableroGrafico.length; i++) {
			for (int j = 0; j < tableroGrafico.length; j++) {
				tableroGrafico[i][j].setBackground(Color.GREEN);
				tableroGrafico[i][j].setEnabled(false);
			}
		}
		
		timer.cancel();
		
		JOptionPane.showMessageDialog(this, "¡Felicitaciones!\nHa completado el tablero correctamente. ");
		int res = JOptionPane.showConfirmDialog(this, "¿Desea salir?");
		
		if (res == 0)
			System.exit(0);
	}
	
	private void actualizarTablero() {
		JButton celdaGrafica;
		Celda celda;
		
		for (int i = 0; i < tableroGrafico.length; i++) {
			for (int j = 0; j < tableroGrafico[i].length; j++) {
				
				celdaGrafica = tableroGrafico[i][j];
				celda = juego.getCelda(i, j);
				Color bgr = celda.isCorrecta() ? Color.WHITE : new Color(230,90,69);
				
				celdaGrafica.setBackground(bgr);
				
				Dimension dim = celdaGrafica.getPreferredSize();
				
				if (celda.isModificada()) {
					celda.setModificada(false);
					
					int width = (int) dim.getWidth();
					int height = (int) dim.getHeight();
					ImageIcon grafico = celda.getEntidadGrafica().getGrafico();
					Image img = celda.getEntidadGrafica().getGrafico().getImage();
					Image newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
					grafico.setImage(newimg);
					
				}
				
				celdaGrafica.repaint();
				
			}
		}
	}

}
