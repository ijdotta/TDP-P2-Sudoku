package logica;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Ignacio Joaquín Dotta
 *
 */
public class Juego {
	
	protected int celdasRestantes;
	protected Celda [][] tablero;
	
	public Juego(String path) {
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
		
		controlarSolucion();
		
		//For debugging
		printTablero();
		
		eliminarCeldas(10);
		
		printTablero();
	}
	
	/**
	 * Controla la solución cargada en el tablero.
	 * Únicamente debe utilizarse al momento de la inicialización.
	 */
	private void controlarSolucion() {
		boolean esCorrecto = true;
		
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				esCorrecto = checkPanel(i*3, j*3);
		
		esCorrecto = esCorrecto && checkFilasCols();
		
		if (!esCorrecto)
			System.out.println("ERRORES EN LA SOLUCION CARGADA");
		else
			System.out.println("SOLUCION BIEN!");
	}

	/**
	 * Controla que no haya valores repetidos en filas y columnas.
	 * @return true si no hay valores repetidos.
	 */
	private boolean checkFilasCols() {
		HashSet<Integer> valores_fil, valores_col;
		int val_fil, val_col;
		boolean hayError = false;
		
		for (int k = 0; k < 9 && !hayError; k++) {
			valores_fil = new HashSet<Integer>();
			valores_col = new HashSet<Integer>();
			
			for (int i = 0; i < 9 && !hayError; i++) {
				val_fil = tablero[k][i].getValor();
				val_col = tablero[i][k].getValor();
				
				hayError = valores_fil.contains(val_fil) || valores_col.contains(val_col);
				valores_fil.add(val_fil);
				valores_col.add(val_col);
			}
		}
		
		return !hayError;
	}
	
	private boolean checkFilasCols(int f, int c) {
		HashSet<Integer> valores_fil, valores_col;
		int val_fil, val_col;
		boolean hayError = false;
		
		valores_fil = new HashSet<Integer>();
		valores_col = new HashSet<Integer>();
		
		for (int k = 0; k < 9 && !hayError; k++) {
			
			
				val_fil = tablero[f][k].getValor();
				val_col = tablero[k][c].getValor();
				
				hayError = valores_fil.contains(val_fil) || valores_col.contains(val_col);
				valores_fil.add(val_fil);
				valores_col.add(val_col);
				
		}
		
		return !hayError;
	}

	/**
	 * Controla que no haya valores repetidos en el panel cuya celda superior izquierda está
	 * dada por los índices f y c.
	 * @param f Fila inicial del panel
	 * @param c Columna inicial del panel
	 * @return true si el panel no contiene valores repetidos.
	 */
	private boolean checkPanel(int f, int c) {
		HashSet<Integer> valores = new HashSet<Integer>();
		int v_aux;
		boolean hayError = false;
		
		for (int i = f; i < f+3 && !hayError; i++) {
			for (int j = c; j < c+3 && !hayError; j++) {
				v_aux = tablero[i][j].getValor();
				
				hayError = valores.contains(v_aux);
				valores.add(v_aux);
			}
		}
		
		return !hayError;
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
				tablero[f][c].actualizarValor(-1);
				tablero[f][c].setEditable(true);
				j++;
			}
		}
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
		/*
		 * c.actualizarValor(valor);
		 * 
		 * if (valor != -1) { this.celdasRestantes--; checkInsertion(c); }
		 * 
		 * if (celdasRestantes == 0) ;//Chequear victoria
		 */
		
		int valor_viejo = c.getValor();
		c.actualizarValor(valor_nuevo);
		
		controlValorRemovido(c, valor_viejo);
		controlValorInsertado(c, valor_nuevo);
		
	}

	private void controlValorRemovido(Celda celda, int valor_viejo) {
		List<Celda> repeticiones;
		int fila = celda.getFila(), col = celda.getColumna();
		
		repeticiones = repeticionesFila(fila, valor_viejo);
		if (repeticiones.size() == 1)
			repeticiones.get(0).actualizarImagen(true);
		
		repeticiones = repeticionesColumna(col, valor_viejo);
		if (repeticiones.size() == 1)
			repeticiones.get(0).actualizarImagen(true);
		
		repeticiones = repeticionesPanel(fila, col, valor_viejo);
		if (repeticiones.size() == 1)
			repeticiones.get(0).actualizarImagen(true);
	}

	private void controlValorInsertado(Celda celda, int valor_nuevo) {
		List<Celda> repeticiones;
		int fila = celda.getFila(), col = celda.getColumna();
	
		repeticiones = repeticionesFila(fila, valor_nuevo);
		if (repeticiones.size() > 1)
			for (Celda c : repeticiones)
				c.actualizarImagen(false);
		
		repeticiones = repeticionesColumna(col, valor_nuevo);
		if (repeticiones.size() > 1)
			for (Celda c : repeticiones)
				c.actualizarImagen(false);
		
		repeticiones = repeticionesPanel(fila, col, valor_nuevo);
		if (repeticiones.size() > 1)
			for (Celda c : repeticiones)
				c.actualizarImagen(false);
	}

	private List<Celda> repeticionesFila(int fila, int valor) {
		List<Celda> repeticiones = new ArrayList<Celda>();
		Celda c;
		
		for (int i = 0; i < 9; i++) {
			c = tablero[fila][i];
			if (c.getValor() == valor)
				repeticiones.add(c);
		}
		
		return repeticiones;
	}

	private List<Celda> repeticionesColumna(int col, int valor) {
		List<Celda> repeticiones = new ArrayList<Celda>();
		Celda c;
		
		for (int i = 0; i < 9; i++) {
			c = tablero[i][col];
			if (c.getValor() == valor)
				repeticiones.add(c);
		}
		
		return repeticiones;
	}

	private List<Celda> repeticionesPanel(int fila, int col, int valor) {
		List<Celda> repeticiones = new ArrayList<Celda>();
		Celda c;

		int fila_panel = ((int) fila/3) * 3;
		int col_panel = ((int) col/3) * 3;
		
		for (int i = fila_panel; i < fila_panel+3; i++) {
			for (int j = col_panel; j < col_panel+3; j++) {
				c = tablero[i][j];
				if (c.getValor() == valor)
					repeticiones.add(c);
			}
		}
		
		return repeticiones;
	}

	private void checkInsertion(Celda celda) {
		int valor = celda.getValor();
		int fila = celda.getFila();
		int col = celda.getColumna();
		boolean error = false;
		
		
		//Chequear intersección
		for (int i = 0; i < 9; i++) {
			Celda c_aux;
			
			c_aux = tablero[fila][i];
			if (c_aux.getValor() == valor && c_aux != celda) {
				c_aux.actualizarImagen(false);
				error = true;
				System.out.println("Conflicto con " + valor);
			}
			
			c_aux = tablero[i][col];
			if (c_aux.getValor() == valor && c_aux != celda) {
				c_aux.actualizarImagen(false);
				error = true;
				System.out.println("Conflicto con " + valor);
			}
			
		}
		
		//Chequear panel
		int fila_panel = fila/3; fila_panel *= 3;
		int col_panel = col/3; col_panel *= 3;
		for (int i = fila_panel; i < fila_panel+3; i++) {
			for (int j = col_panel; j < col_panel+3; j++) {
				Celda c_aux = tablero[i][j];
				if (c_aux.getValor() == valor && c_aux != celda) {
					c_aux.actualizarImagen(false);
					error = true;
					System.out.println("Conflicto con " + valor);
				}
			}
		}
		
		if (error) {
			celda.actualizarImagen(false);
		}
		else {
			celda.actualizarImagen(true);
		}
//		else {
//			//Si la inserción fue correcta, restablece por correcto por si antes hubiera habido incorrecto
//			//NO SIRVE HACER ESTO
//			for (int i = 0; i < 9; i++) {
//				tablero[fila][i].actualizarImagen(true);
//				tablero[i][col].actualizarImagen(true);
//			}
//			for (int i = fila_panel; i < fila_panel+3; i++) {
//				for (int j = col_panel; j < col_panel+3; j++) {
//					tablero[i][j].actualizarImagen(true);
//				}
//			}
//		}
	}

}
 