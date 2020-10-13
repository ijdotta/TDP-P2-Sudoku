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

/**
 * 
 * @author Ignacio Joaquín Dotta
 *
 */
public class Juego {
	
	protected int celdasRestantes;
	protected Celda [][] tablero;
	
	public Juego(String path, int size, int cantEliminar) {
		BufferedReader br;
		String line;
		String [] row;
		
		try {
			
			if (!isPerfectSquare(size)) {
				throw new Exception("Error en la creación del tablero (size no es cuadrado perfecto). ");
			}
			
			tablero = new Celda[size][size];
			celdasRestantes = size * size;
			
			br = new BufferedReader(new FileReader(path));
			
			for (int i = 0; i < size; i++) {
				line = br.readLine();
				
				if (line == null) {
					br.close();
					throw new Exception("Error en la creación del tablero (faltan filas). ");
				}
				
				row = line.trim().split(" ");
				
				if (row.length < size) {
					br.close();
					throw new Exception("Error en la creación del tablero (faltan columnas). ");
				}
				
				for (int j = 0; j < size; j++) {
					tablero[i][j] = new Celda(Integer.parseInt(row[j]), i, j);
				}
				
			}
			
			br.close();
			
			if (controlGeneral()) {
				System.out.println("BIENNNNNNNNNN");
			}
			else {
				System.out.println("MALLLLLLLLLLL");
				throw new Exception("Error en la carga del juego (la solución original es incorrecta).");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			tablero = null;
			return;
		}

		
		
		//For debugging
		printTablero();
//		
		eliminarCeldas(10);
//		
		printTablero();
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
	
	/**
	 * Eliminar celdas al azar.
	 * @param cant Cantidad de celdas distintas a eliminar.
	 */
	private void eliminarCeldas(int cant) {
		Random r = new Random();
		
		if (cant >= 81)
			cant = 81;
		
		this.celdasRestantes -= cant;
		
		int j = 0;
		while (j < cant) {
			int f = r.nextInt(9), c = r.nextInt(9);
			if (tablero[f][c].getValor() != -1) {
				tablero[f][c].setValor(-1);
				tablero[f][c].setEditable(true);
				j++;
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
				
						//Previene de actualizar dos veces la misma celda
						if (celda_anterior.isCorrecta()) {
							celda_anterior.setCorrecta(false);
						}
					}
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
						}
				
						if (celda_anterior.isCorrecta()) {
							celda_anterior.setCorrecta(false);
						}
					}
				}
			}
		}
		
		//Control por panel
		cantPanelesLinea = cantidadPanelesLinea();
		cantCeldasLineaPanel = cantidadCeldasLineaPanel();
		
		for (int i = 0; i < cantPanelesLinea; i++) {
			for (int j = 0; j < cantPanelesLinea; j++) {
				
				//Control del panel(i,j)
				for (int f = i; f < i+cantCeldasLineaPanel; f++) {
					map = new HashMap<Integer, Celda>();
					
					for (int c = j; c < j+cantCeldasLineaPanel; c++) {
						celda_actual = tablero[f][c];
						valor = celda_actual.getValor();
						
						if (valor != -1) {
							celda_anterior = map.put(valor, celda_actual);
						
							if (celda_anterior != null) {
								todasCorrectas = false;
								
								if (celda_actual.isCorrecta()) {
									celda_actual.setCorrecta(false);
								}
						
								if (celda_anterior.isCorrecta()) {
									celda_anterior.setCorrecta(false);
								}
							}
						}
					}
				}
			}
		}
		
		return todasCorrectas;
	}
	
	/******************/
	
	/**
	 * Retorna la celda en la posición (f,c)
	 * @param f Fila de la celda
	 * @param c Columna de la celda
	 * @return ...
	 */
	public Celda getCelda(int f, int c) {
		if (0 <= f && f < tablero.length && 0 <= c && c < tablero[0].length)
			return tablero[f][c];
		else
			return null;
	}
	
	public void accionar(Celda c, int valor_nuevo) {
		c.setValor(valor_nuevo);
		controlGeneral();
	}
	
	public Celda[][] getTablero() {
		return tablero;
	}
	
	public int cantidadCeldasLinea() {
		System.out.println(tablero.length);
		return tablero.length;
	}
	
	public int cantidadCeldasLineaPanel() {
		return (int) Math.sqrt(tablero.length);
	}
	
	public int cantidadPanelesLinea() {
		return (int) Math.sqrt(tablero.length);
	}

	/**
	 * FOR DEBUGGING - DELETE
	 */
	private void printTablero() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.print(tablero[i][j].getValor() + " ");
			}
			System.out.println();
		}
	}
	
}
 