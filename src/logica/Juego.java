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
	
	/**
	 * Crea e inicializa un Sudoku con valores desde 1 hasta size. Requiere size un cuadrado perfecto.
	 * @param path Ruta al archivo de texto.
	 * @param size Cantidad de números.
	 * @
	 */
	public Juego(String path, int size) {
		BufferedReader br;
		String line;
		String [] row;
		
		tablero = new Celda[9][9];
		celdasRestantes = 81;
		
		try {
			br = new BufferedReader(new FileReader(path));
			
			for (int i = 0; i < 9; i++) {
				line = br.readLine();
				
				if (line == null) //TODO Preguntar si usamos excepciones o qué
					break;
				
				row = line.trim().split(" ");
				
				if (row.length < 9) //TODO Preguntar si usamos excepciones o qué
					break;
				
				for (int j = 0; j < 9; j++) {
					tablero[i][j] = new Celda(Integer.parseInt(row[j]), i, j);
				}
				
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (controlGeneral()) {
			System.out.println("BIENNNNNNNNNN");
		}
		else {
			System.out.println("MALLLLLLLLLLL");
		}
		
		//For debugging
		printTablero();
		
		eliminarCeldas(5);
		
		printTablero();
//		tablero = null;
	}
	
	private boolean isPerfectSquare(int n) {
		int i = 0;
		boolean perfectSquare = false;
		
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
	
	private boolean controlGeneralOLD() {
		boolean correcto = true;
		
		for (int i = 0; i < 9; i++) {
			controlFila(i);
			controlColumna(i);
			controlPanel(i);
		}
		
		return correcto;
	}
	
	public void restore() {
		for (int i = 0; i < tablero.length; i++)
			for (int j = 0; j < tablero[i].length; j++)
				tablero[i][j].setCorrecta(true);
	}
	
	private boolean controlGeneral() {
		boolean correcto = true;
		
		restore();
		
		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero[0].length; j++) {
				controlar(i, j);
			}
		}
		
		return correcto;
	}

	private void controlar(int i, int j) {
		Celda celda_control = tablero[i][j];
		Celda actual;
		int valor = celda_control.getValor();
		boolean hayError;
		
		for (int k = 0; k < 9; k++) {
			actual = tablero[i][k];
			if (valor != -1 && actual != celda_control && actual.getValor() == valor) {
				celda_control.setCorrecta(false);
			}
			
			actual = tablero[k][j];
			if (valor != -1 && actual != celda_control && actual.getValor() == valor) {
				celda_control.setCorrecta(false);
			}
			
		}
		
		int f_panel = ((int)i/3) * 3;
		int c_panel = ((int)j/3) * 3;
		
		for (int f = f_panel; f < f_panel+3; f++) {
			for (int c = c_panel; c < c_panel+3; c++) {
				actual = tablero[f][c];
				if (valor != -1 && actual != celda_control && actual.getValor() == valor) {
					celda_control.setCorrecta(false);
				}
			}
		}
		
	}

	private boolean controlFila(int i) {
		Map<Integer, List<Celda>> values = new HashMap<Integer, List<Celda>>();
		List<Celda> cellList;
		Celda current;
		int value;
		
		for (int j = 0; j < 9; j++) {
			current = tablero[i][j];
			value = current.getValor();
			
			cellList = values.get(value);
			
			if (cellList == null) {
				cellList = new ArrayList<Celda>();
				values.put(value, cellList);
			}
			
//			System.out.println("Celda ("+i+", "+j+") = " + value);
			
			cellList.add(current);
		}
		
		return corregirCeldas(values);
	}
	

	private boolean controlColumna(int i) {
		Map<Integer, List<Celda>> values = new HashMap<Integer, List<Celda>>();
		List<Celda> cellList;
		Celda current;
		int value;
		
		for (int j = 0; j < 9; j++) {
			current = tablero[j][i];
			value = current.getValor();
			
			cellList = values.get(value);
			if (cellList == null) {
				cellList = new ArrayList<Celda>();
				values.put(value, cellList);
			}
			
			cellList.add(current);
		}
		
		return corregirCeldas(values);
	}

	private boolean controlPanel(int i) {
		Map<Integer, List<Celda>> values = new HashMap<Integer, List<Celda>>();
		List<Celda> cellList;
		Celda current;
		int value;
		int fila_ini = ((int) i/3) * 3, col_ini = i%3 * 3;
		int long_panel = 3;
		
		for (int f = fila_ini; f < fila_ini+long_panel; f++) {
			for (int c = col_ini; c < col_ini+long_panel; c++) {
				current = tablero[f][c];
				value = current.getValor();
				
				cellList = values.get(value);
				if (cellList == null) {
					cellList = new ArrayList<Celda>();
					values.put(value, cellList);
				}
				
				cellList.add(current);
			}
		}
		
		return corregirCeldas(values);
	}
	
	private boolean corregirCeldas(Map<Integer, List<Celda>> values) {
		List<Celda> cellList;
		int cellValue;
		boolean correctness, correcto = true;
		
		for (Entry<Integer, List<Celda>> e : values.entrySet()) {
			cellValue = e.getKey();
			cellList = e.getValue();
			
			if (cellValue == -1) {
				for (Celda c : cellList) {
					if (!c.isCorrecta()) {
						c.setCorrecta(true);
					}
				}
			}
			else {
//				correctness = cellList.size() == 1;
//				for (Celda c : cellList) {
//					if (c.isCorrecta() != correctness) {
//						c.setCorrecta(correctness);
//					}
//				}
				
				if (cellList.size() == 1) {
					cellList.get(0).setCorrecta(true);
				}
				else {
					for (Celda c : cellList)
						c.setCorrecta(false);
				}
				
//				if (!correctness)
//					correcto = false;
				
			}
		}
		return correcto;
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
	
	/**
	 * Retorna la celda en la posición (f,c)
	 * @param f Fila de la celda
	 * @param c Columna de la celda
	 * @return ...
	 */
	public Celda getCelda(int f, int c) {
		if (0 <= f && f < 9 && 0 <= c && c < 9)
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
		return tablero.length;
	}
	
	public int cantidadPanelesLinea() {
		return (int) Math.sqrt(tablero.length);
	}
	
}
 