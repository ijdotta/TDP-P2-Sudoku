package logica;

public class Celda {
	
	protected int valor;
	protected boolean correcta;
	protected boolean editable;
	protected EntidadGrafica entidadGrafica;
	protected boolean modificada;
	
	public Celda(int valor) {
		this.valor = valor;
		this.editable = false;
		this.correcta = true;
		this.modificada = true;
		
		this.entidadGrafica = new EntidadGrafica();
		this.entidadGrafica.actualizar(valor);
	}
	
	public void setValor(int valor) {
		this.valor = valor;
		this.entidadGrafica.actualizar(valor);
		this.modificada = true;
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public void setCorrecta(boolean esCorrecta) {
		this.correcta = esCorrecta;
	}
	
	public void setModificada(boolean modificada) {
		this.modificada = modificada;
	}

	public int getValor() {
		return valor;
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
	
	public boolean isModificada() {
		return modificada;
	}
}
