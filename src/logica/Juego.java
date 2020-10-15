package logica;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Ignacio Joaquín Dotta
 *
 */
public class Juego {
	
	private static Logger logger;
	
	protected int cantCeldasCompletasCorrectas;
	protected Celda [][] tablero;
	
	public Juego(String path, int size, int cantEliminar) {
		
		inicializarLogger();
		
		//Inicializar juego
		BufferedReader br;
		String line;
		String [] row;
		
		try {
			
			if (!isPerfectSquare(size)) {
				logger.severe("Error en la creación del tablero (size no es cuadrado perfecto). ");
				throw new Exception();
			}
			
			tablero = new Celda[size][size];
			cantCeldasCompletasCorrectas = size * size;
			
			br = new BufferedReader(new FileReader(path));
			
			for (int i = 0; i < size; i++) {
				line = br.readLine();
				
				if (line == null) {
					br.close();
					logger.severe("Error en la creación del tablero (faltan filas). ");
					throw new Exception();
				}
				
				row = line.trim().split(" ");
				
				if (row.length < size) {
					br.close();
					logger.severe("Error en la creación del tablero (faltan columnas). ");
					throw new Exception();
				}
				
				for (int j = 0; j < size; j++) {
					tablero[i][j] = new Celda(Integer.parseInt(row[j]));
				}
				
			}
			
			br.close();
			
			if (controlGeneral()) {
				logger.finest("La solución inicial es correcta. ");
			}
			else {
				logger.severe("Error en la carga del juego (la solución inicial es incorrecta).");
				throw new Exception();
			}
			
		} catch (Exception e) {
			tablero = null;
			return;
		}

		eliminarCeldas(cantEliminar);
		
	}
	
	private void inicializarLogger() {
		if (logger == null) {
			
			logger = Logger.getLogger(Juego.class.getName());
			
			Handler hnd = new ConsoleHandler();
			hnd.setLevel(Level.ALL);
			logger.addHandler(hnd);
			
			logger.setLevel(Level.ALL);
			
			Logger rootLoger = logger.getParent();
			for (Handler h : rootLoger.getHandlers())
				h.setLevel(Level.OFF);
		}
	}

	public Juego(String path, int size) {
		this(path, size, 40);
	}
	
	public Juego() {
		this("src/files/sk_bien.txt", 9);
	}
	
	private boolean isPerfectSquare(int n) {
		int i = 0;
		boolean perfectSquare = n == 1; //Si es 1 no entra al while
		
		while (i <= (n/2) && !perfectSquare) {
			perfectSquare = i*i == n;
			i++;
		}
		
		return perfectSquare;
	}
	
	private void eliminarCeldas(int cant) {
		Random r = new Random();
		Celda celda;
		
		int cantCeldasLinea = cantidadCeldasLinea();
		int cantMax = (int) Math.pow(cantCeldasLinea, 2);
		
		if (cant > cantMax) {
			logger.info(cant + " supera la cantidad de celdas del tablero. Se eliminarán " + cantMax);
			cant = cantMax;
		}
		
		this.cantCeldasCompletasCorrectas -= cant;
		
		int j = 0;
		while (j < cant) {
			int f = r.nextInt(cantCeldasLinea), c = r.nextInt(cantCeldasLinea);
			celda = tablero[f][c];
			
			if (celda.getValor() != -1) {
				celda.setValor(-1);
				celda.setEditable(true);
				j++;
				
				logger.finest("Se eliminó la celda (" + f + ", " + c + ")");
			}
		}
	}
	
	public boolean controlGeneral() {
		Celda c;
		
		//Restaurar
		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero[i].length; j++) {
				c = tablero[i][j];
				if (!c.isCorrecta()) {
					c.setCorrecta(true);
				}
			}
		}
		
		cantCeldasCompletasCorrectas = (int) Math.pow(cantidadCeldasLinea(), 2);
		logger.fine("Se restauró el tablero a estado correcto. ");
		
		//buscar incorrectas
		return buscarIncorrectas();
	}
	
	private boolean buscarIncorrectas() {
		boolean todasCorrectas = true;
		Map<Integer, Celda> map;
		Celda celda_actual, celda_anterior;
		int valor, cantCeldasLinea, cantPanelesLinea, cantCeldasLineaPanel;
		
		cantCeldasLinea = cantidadCeldasLinea();
		
		//Control por fila
		for (int i = 0; i < cantCeldasLinea; i++) {
			map = new HashMap<Integer, Celda>();
			
			//Control de fila i
			for (int j = 0; j < cantCeldasLinea; j++) {
				celda_actual = tablero[i][j];
				valor = celda_actual.getValor();
				
				if (valor != -1) {
					celda_anterior = map.put(valor, celda_actual);
				
					if (celda_anterior != null) {
						todasCorrectas = false;
						celda_actual.setCorrecta(false);
						
						logger.info("Se actualizó celda incorrecta. ");
				
						//Previene de actualizar dos veces la misma celda
						if (celda_anterior.isCorrecta()) {
							celda_anterior.setCorrecta(false);
							cantCeldasCompletasCorrectas--;
							
							logger.info("Se actualizó celda incorrecta. ");
						}
					}
				}
				else {
					cantCeldasCompletasCorrectas--;
				}
			}
		}
		
		//Control por columna
		for (int i = 0; i < cantCeldasLinea; i++) {
			map = new HashMap<Integer, Celda>();
			
			//Control de columna i
			for (int j = 0; j < cantCeldasLinea; j++) {
				celda_actual = tablero[j][i];
				valor = celda_actual.getValor();
				
				if (valor != -1) {
					celda_anterior = map.put(valor, celda_actual);
				
					if (celda_anterior != null) {
						todasCorrectas = false;
						
						if (celda_actual.isCorrecta()) {
							celda_actual.setCorrecta(false);
							cantCeldasCompletasCorrectas--;
							logger.info("Se actualizó celda incorrecta. ");
						}
				
						if (celda_anterior.isCorrecta()) {
							celda_anterior.setCorrecta(false);
							cantCeldasCompletasCorrectas--;
							logger.info("Se actualizó celda incorrecta. ");
						}
					}
				}
				else {
					cantCeldasCompletasCorrectas--;
				}
			}
		}
		
		//Control por panel
		cantPanelesLinea = cantidadPanelesLinea();
		cantCeldasLineaPanel = cantidadCeldasLineaPanel();
		
		for (int i = 0; i < cantPanelesLinea; i++) {
			for (int j = 0; j < cantPanelesLinea; j++) {
				map = new HashMap<Integer, Celda>();
				
				//Control del panel(i,j)
				for (int f = i*cantCeldasLineaPanel; f < i*cantCeldasLineaPanel+cantCeldasLineaPanel; f++) {
					
					for (int c = j*cantCeldasLineaPanel; c < j*cantCeldasLineaPanel+cantCeldasLineaPanel; c++) {
						celda_actual = tablero[f][c];
						valor = celda_actual.getValor();
						
						if (valor != -1) {
							celda_anterior = map.put(valor, celda_actual);
						
							if (celda_anterior != null) {
								todasCorrectas = false;
								
								if (celda_actual.isCorrecta()) {
									celda_actual.setCorrecta(false);
									cantCeldasCompletasCorrectas--;
									logger.info("Se actualizó celda incorrecta. ");
								}
						
								if (celda_anterior.isCorrecta()) {
									celda_anterior.setCorrecta(false);
									cantCeldasCompletasCorrectas--;
									logger.info("Se actualizó celda incorrecta. ");
								}
							}
						}
						else {
							cantCeldasCompletasCorrectas--;
						}
					}
				}
			}
		}
		
		return todasCorrectas;
	}
	
	public Celda getCelda(int f, int c) {
		if (0 <= f && f < tablero.length && 0 <= c && c < tablero[0].length) {
			return tablero[f][c];
		}
		else {
			logger.warning("Índice (" + f + ", " + c + ") fuera de los límites del tablero. ");
			return null;
		}
	}
	
	public void accionar(Celda c, int valor_nuevo) {
		c.setValor(valor_nuevo);
		logger.fine("Se actualizó el valor de una celda. ");
		controlGeneral();
	}
	
	public Celda[][] getTablero() {
		return tablero;
	}
	
	public int cantidadCeldasLinea() {
		return tablero.length;
	}
	
	public int cantidadCeldasLineaPanel() {
		return (int) Math.sqrt(tablero.length);
	}
	
	public int cantidadPanelesLinea() {
		return (int) Math.sqrt(tablero.length);
	}

	public boolean isVictoria() {
		boolean victoria = cantCeldasCompletasCorrectas == Math.pow(cantidadCeldasLinea(), 2);  
		if (victoria) {
			logger.info("Se detectó victoria en el juego. ");
		}
		return victoria;
	}
	
}
 