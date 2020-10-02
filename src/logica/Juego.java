package logica;

import java.util.HashMap;

public class Juego {
	
	private Celda [][] tablero;
	private HashMap<Celda, String> index;
	
	/**
	 * 
	 */
	public Juego() {
		// TODO Auto-generated constructor stub
		for (int i = 0; i < 9; i++) {
			for (int j = 0; i < 9; i++) {
				
			}
		}
	}
	
	public void insertar(Celda c, int valor) {
		c.setValor(valor);
	}
	
	public void getEstado() {
		
	}

}
 