package logica;

public class Celda {
	
	private int valor;
	private boolean activa;
	private EntidadGrafica entidadGrafica;

	public Celda() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Actualiza el valor de la celda y su gráfico.
	 * @param v Valor de la celda.
	 */
	public void setValor(int v) {
		if (activa) {
			valor = v;
			entidadGrafica.actualizar(v-1);
		}
	}
	
}
