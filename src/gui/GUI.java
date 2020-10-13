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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

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
//		setBounds(100, 100, 500, 650);
//		setBounds(300, 100, 500, 625);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		setTitle("Sudoku");
		juego = new Juego("src/files/sk_bien_16.txt", 9);
		
		if (juego.getTablero() == null) {
			finalizarJuego(false);
		}
		
		//Informacion sobre el juego
		int cantPanelesLinea = juego.cantidadPanelesLinea();
		int cantCeldasLineaPanel = juego.cantidadCeldasLineaPanel();
		int cantCeldasLinea = juego.cantidadCeldasLinea();
		
		//Tamaño de la ventana segun la cantidad de celdas
		setBounds(300, 100, 500, 575 + 50*(cantCeldasLinea/10 +1));
		
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
		panelTablero.setBorder(new LineBorder(Color.BLACK));
		panelTablero.setPreferredSize(dimTablero);
		center.add(panelTablero);
		panelTablero.setBackground(Color.BLACK); //Testing
		
		panelBotones = new JPanel();
		panelBotones.setBorder(new LineBorder(Color.BLACK, 3));
		panelBotones.setPreferredSize(dimPanelBotones);
		bottom.add(panelBotones);
		panelBotones.setBackground(panelBgr); //Testing
		
		//DISEÑO PANEL INFORMACIÓN
		JPanel clockPanel = new JPanel();
		JLabel [] timeDisplay = new JLabel[8];
		JLabel digit;
		Dimension clockPanelDim, digitDim;
		
		digitDim = new Dimension(50, 50);
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
//		clockPanel.setBounds(100, 12, 250, 26);
		clockPanel.setBounds(0, 0, 450, 50);
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
		
		//DISEÑO TABLERO
		panelTablero.setLayout(new GridLayout(cantPanelesLinea, cantPanelesLinea, 1, 1));
		
		JPanel[][] tmpPanel = new JPanel[cantPanelesLinea][cantPanelesLinea]; //Solo se usa en la inicialización
		
		//Tratar de definir la cantidad de celdas en funcion del juego
		
		//Añadir 9 subpaneles
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
		panelBotones.setLayout(new GridLayout(cantCeldasLinea/10 + 1, cantCeldasLinea+1));
//		panelBotones.setLayout(new FlowLayout());
		for (int i = 0; i < cantCeldasLinea+1; i++) {
			JButton boton = new JButton();
			boton.setText((i+1)+""); //Testing
			boton.setActionCommand(Integer.toString(i+1));
			boton.setBackground(Color.WHITE);
			panelBotones.add(boton);
			
			if (i == cantCeldasLinea) {
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
//						TODO finalizarJuego(true);
						
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
				JLabel digit;
				int width, height;
				
				for (int i = timeDisplay.length-1; i >= 0; i--) {
					digit = timeDisplay[i]; 
					
					if (i == 2 || i == 5)
						continue;
					
					ImageIcon icon = new ImageIcon("src/img/ClockNumbers/Clock (" + time.charAt(i) + ").png");
					Image img = icon.getImage();
					Image newimg = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
					icon.setImage(newimg);
					digit.setIcon(icon);
					
//					timeDisplay[i].setText(time.charAt(i)+"");
				}
				
				System.out.println("Reloj GUI :: " + clock.getTime());
				
			}
		};
		
		timer.schedule(actualizarReloj, 0, refreshRate);
		
	}

	private void finalizarJuego(boolean b) {
		String msg;
		if (b) {
			msg = "¡Felicitaciones! \n Usted ha ganado el juego. ";
		}
		else {
			msg = "Ha ocurrido un error en la carga del juego. ";
		}
		
		JOptionPane.showMessageDialog(this, msg);
		
		int res = 1;
		if (b) {
			res = JOptionPane.showConfirmDialog(contentPane, "Salir?");
		}
		else {
			System.exit(0);
		}
		
		
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
