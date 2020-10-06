package logica;

public class Celda {
	
	protected int valor; 
	protected boolean editable;
	protected int fila;
	protected int columna;
	protected EntidadGrafica entidadGrafica;
	
	/**
	 * 
	 * @param valor
	 */
	public Celda(int valor, int f, int j) {
		this.valor = valor;
		editable = false;
		fila = f;
		columna = j;
		entidadGrafica = new EntidadGrafica();
		actualizarImagen(true);
	}
	
	/**
	 * 
	 * @param valor
	 */
	public void actualizarValor(int valor) {
		this.valor = valor;
		entidadGrafica.actualizar(valor, true);
	}
	
	/**
	 * 
	 * @param correcto
	 */
	public void actualizarImagen(boolean correcto) {
		entidadGrafica.actualizar(valor, correcto);
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * 
	 * @return
	 */
	public int getValor() {
		return valor;
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	public int getFila() {
		return fila;
		
	}
	
	public int getColumna() {
		return columna;
	}
	
	public EntidadGrafica getEntidadGrafica() {
		return entidadGrafica;
	}
	
}
