package logica;

public class Celda {
	
	protected int valor;
	protected int fila;
	protected int columna;
	protected boolean correcta;
	protected boolean editable;
	protected EntidadGrafica entidadGrafica;
	
	public Celda(int valor, int f, int j) {
		this.valor = valor;
		this.editable = false;
		this.fila = f;
		this.columna = j;
		this.correcta = true;
		
		this.entidadGrafica = new EntidadGrafica();
		this.entidadGrafica.actualizar(valor);
	}
	
	public void setValor(int valor) {
		this.valor = valor;
		entidadGrafica.actualizar(valor);
		entidadGrafica.setCorrecta(correcta);
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public void setCorrecta(boolean esCorrecta) {
		this.correcta = esCorrecta;
		entidadGrafica.setCorrecta(esCorrecta);
	}

	/**
	 * 
	 * @return
	 */
	public int getValor() {
		return valor;
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
	
	public boolean isEditable() {
		return editable;
	}

	public boolean isCorrecta() {
		return correcta;
	}
}
