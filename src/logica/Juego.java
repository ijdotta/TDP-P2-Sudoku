package logica;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
	
	protected LocalTime start;
	protected int cantCeldasCompletasCorrectas;
	protected Celda [][] tablero;
	
	/**
	 * Crea un nuevo juego Sudoku a partir de un archivo según la cantidad de valores permitidos indicados y elimina la
	 * cantidad de celdas indicada.
	 * @param path Ruta al archivo de texto que contiene la solución inicial.
	 * @param size Cantidad de valores por fila (requiere size un cuadrado perfecto).
	 * @param cantEliminar Cantidad de celdas que se deben eliminar para ser completadas por el jugador.
	 */
	public Juego(String path, int size, int cantEliminar) {
		
		inicializarLogger();
		
		BufferedReader br;
		String line;
		String [] row;
		
		
		try {
			
			if (!isPerfectSquare(size)) {
				logger.severe("Error en la creación del tablero (size no es cuadrado perfecto). ");
				throw new Exception();
			}
			
			this.tablero = new Celda[size][size];
			this.cantCeldasCompletasCorrectas = size * size;
			
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
					this.tablero[i][j] = new Celda(Integer.parseInt(row[j]));
				}
				
			}
			
			br.close();
			
			if (controlGeneral()) {
				logger.fine("La solución inicial es correcta. ");
			}
			else {
				logger.severe("Error en la carga del juego (la solución inicial es incorrecta).");
				throw new Exception();
			}
			
		} catch (Exception e) {
			this.tablero = null;
			return;
		}

		eliminarCeldas(cantEliminar);
		
		start = LocalTime.now();
	}
	
	/**
	 * Crea un juego con la solución en el archivo de texto y cantidad de valores por fila indicados.
	 * @param path Ruta al archivo de texto con la solución inicial.
	 * @param size Cantidad de valores por fila (requiere size un cuadrado perfecto).
	 */
	public Juego(String path, int size) {
		this(path, size, 40);
	}
	
	/**
	 * Crea un nuevo juego con una solución predefinida y tablero de 9x9.
	 */
	public Juego() {
		this("src/files/sk_bien.txt", 9);
	}
	
	/**
	 * Inicializa el logger de la clase actual.
	 */
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

	/**
	 * Determina si el valor recibido es un cuadrado perfecto.
	 * @param n Numero a controlar.
	 * @return true si n es un cuadrado perfecto.
	 */
	private boolean isPerfectSquare(int n) {
		int i = 0;
		boolean perfectSquare = n == 1;
		
		while (i <= (n/2) && !perfectSquare) {
			perfectSquare = i*i == n;
			i++;
		}
		
		return perfectSquare;
	}
	
	/**
	 * Elimina celdas al azar.
	 * @param cantEliminar Cantidad de celdas a eliminar.
	 */
	private void eliminarCeldas(int cantEliminar) {
		Random r = new Random();
		Celda celda;
		int contador;
		
		int cantCeldasLinea = cantidadCeldasLinea();
		int cantMax = (int) Math.pow(cantCeldasLinea, 2);
		
		if (cantEliminar > cantMax) {
			logger.info(cantEliminar + " supera la cantidad de celdas del tablero. Se eliminarán " + cantMax);
			cantEliminar = cantMax;
		}
		
		this.cantCeldasCompletasCorrectas -= cantEliminar;
		
		contador = 0;
		while (contador < cantEliminar) {
			int f = r.nextInt(cantCeldasLinea), c = r.nextInt(cantCeldasLinea);
			celda = tablero[f][c];
			
			if (celda.getValor() != 0) {
				celda.setValor(0);
				celda.setEditable(true);
				contador++;
				
				logger.finest("Se eliminó la celda (" + f + ", " + c + ")");
			}
		}
	}
	
	/**
	 * Control completo del tablero.
	 * @return true si no se encuentran errores.
	 */
	private boolean controlGeneral() {
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
		
		return controlCeldasEnConflicto();
	}
	
	/**
	 * Busca celdas en conflicto y las marca como incorrectas.
	 * @return true si no hay celdas incorrectas.
	 */
	private boolean controlCeldasEnConflicto() {
		boolean todasCorrectas = true;
		Map<Integer, Celda> map; //Lo uso para controlar valores repetidos en un determinado área de búsqueda.
		Celda celda_actual, celda_anterior;
		int valor, cantCeldasLinea, cantPanelesLinea, cantCeldasLineaPanel;
		
		cantCeldasLinea = cantidadCeldasLinea();
		
		//Control por fila
		for (int i = 0; i < cantCeldasLinea; i++) {
			map = new HashMap<Integer, Celda>();
			
			//Control por columna de la fila i
			for (int j = 0; j < cantCeldasLinea; j++) {
				celda_actual = tablero[i][j];
				valor = celda_actual.getValor();
				
				//Valor 0 representa celda vacía.
				if (valor != 0) {
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
			
			//Control por fila de la columna i
			for (int j = 0; j < cantCeldasLinea; j++) {
				celda_actual = tablero[j][i];
				valor = celda_actual.getValor();
				
				if (valor != 0) {
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
				for (int f = i*cantCeldasLineaPanel; f < (i+1)*cantCeldasLineaPanel; f++) {
					
					for (int c = j*cantCeldasLineaPanel; c < (j+1)*cantCeldasLineaPanel; c++) {
						celda_actual = tablero[f][c];
						valor = celda_actual.getValor();
						
						if (valor != 0) {
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
	
	/**
	 * Consulta la celda en el índice recibido.
	 * @param f Fila de la celda.
	 * @param c Columna de la celda.
	 * @return Celda en el ínfice (f,c) o null si está fuera del tablero.
	 */
	public Celda getCelda(int f, int c) {
		if (0 <= f && f < tablero.length && 0 <= c && c < tablero[0].length) {
			return tablero[f][c];
		}
		else {
			logger.warning("Indice (" + f + ", " + c + ") fuera de los límites del tablero. ");
			return null;
		}
	}
	
	/**
	 * Inserta el valor recibido en la celda recibida y controla la correctitud de la inserción.
	 * @param c Celda objetivo.
	 * @param valor Valor a insertar.
	 */
	public void accionar(Celda c, int valor) {
		c.setValor(valor);
		logger.fine("Se actualizó el valor de una celda. ");
		controlGeneral();
	}
	
	/**
	 * Consulta el tablero del juego.
	 * @return el tablero o null si hubo un fallo en el juego.
	 */
	public Celda[][] getTablero() {
		return tablero;
	}
	
	/**
	 * Consulta la cantidad de celdas por línea del tablero (filas y columnas).
	 * @return Cantidad de celdas por línea.
	 */
	public int cantidadCeldasLinea() {
		return tablero.length;
	}
	
	/**
	 * Determina la cantidad de celdas por cada subpanel del juego.
	 * @return Cantidad de celdas por subpanel.
	 */
	public int cantidadCeldasLineaPanel() {
		return (int) Math.sqrt(tablero.length);
	}
	
	/**
	 * Determina la cantidad de subpaneles por línea del tablero.
	 * @return Cantidad de subpaneles por línea.
	 */
	public int cantidadPanelesLinea() {
		return (int) Math.sqrt(tablero.length);
	}

	/**
	 * Consulta si se ha completado el tablero correctamente.
	 * @return true si se terminó el juego.
	 */
	public boolean isVictoria() {
		boolean victoria = cantCeldasCompletasCorrectas == Math.pow(cantidadCeldasLinea(), 2);  
		if (victoria) {
			logger.info("Se detectó victoria en el juego. ");
		}
		return victoria;
	}
	
	/**
	 * Devuelve el tiempo transcurrido entre el inicio del juego y la llamada al método.
	 * @return Tiempo transcurrido.
	 */
	public Duration tiempoTranscurrido() {
		return Duration.between(start, LocalTime.now());
	}
	
}
 