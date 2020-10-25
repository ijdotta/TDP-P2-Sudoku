package logica;

/**
 * Class Celda - Modela una celda del juego Sudoku.
 * @author Ignacio Joaquín Dotta
 *
 */
public class Celda {
	
	protected int valor;
	protected boolean correcta;
	protected boolean editable;
	protected boolean modificada;
	protected EntidadGrafica entidadGrafica;

	/**
	 * Crea una Celda con el valor recibido y la imagen correspondiente. El valor 0 representa una celda vacía.
	 * @param valor Valor de la celda.
	 */
	public Celda(int valor) {
		this.valor = valor;
		this.editable = false;
		this.correcta = true;
		this.modificada = true;
		this.entidadGrafica = new EntidadGrafica();
		this.entidadGrafica.actualizar(valor);
	}
	
	/**
	 * Actualiza el valor actual de la celda, su imagen y pasa a estar modificada.
	 * @param valor Valor nuevo.
	 */
	public void setValor(int valor) {
		this.valor = valor;
		this.entidadGrafica.actualizar(valor);
		this.modificada = true;
	}
	
	/**
	 * Define si el valor de la celda es correcto o no.
	 * @param correcta Valor de validez.
	 */
	public void setCorrecta(boolean correcta) {
		this.correcta = correcta;
	}

	/**
	 * Define si la celda puede ser modificada.
	 * @param editable Valor de validez.
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * Define si una celda fue modificada (y por lo tanto se debe refrescar su imagen).
	 * @param modificada Valor de validez.
	 */
	public void setModificada(boolean modificada) {
		this.modificada = modificada;
	}

	/**
	 * Consulta el valor de la celda.
	 * @return Valor de la celda.
	 */
	public int getValor() {
		return valor;
	}
	
	/**
	 * Consulta si la celda es correcta.
	 * @return true si es correcta.
	 */
	public boolean isCorrecta() {
		return correcta;
	}

	/**
	 * Consulta si la celda puede ser editada.
	 * @return true si es editable.
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * Consulta si la celda ha sido modificada.
	 * @return true si la celda fue modificada.
	 */
	public boolean isModificada() {
		return modificada;
	}

	/**
	 * Consulta la entidad gráfica asociada a la celda.
	 * @return entidad gráfica asociada.
	 */
	public EntidadGrafica getEntidadGrafica() {
		return entidadGrafica;
	}
}
