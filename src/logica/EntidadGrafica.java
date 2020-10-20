package logica;

import javax.swing.ImageIcon;

public class EntidadGrafica {
	
	protected ImageIcon grafico;
	protected String[] imagenes;
	
	/**
	 * Crea una entidad gráfica y define el conjunto de imágenes posibles.
	 */
	public EntidadGrafica() {
		grafico = new ImageIcon();
		imagenes = new String[10];
		
		imagenes[0] = "src/img/Numeros/blank.png";
		for (int i = 1; i <= 9; i++) {
			imagenes[i] = "src/img/Numeros/n" + i + ".png";
		}
		
	}
	
	/**
	 * Actualiza la imagen de la entidad gráfica según el valor.
	 * @param valor Valor nuevo.
	 */
	public void actualizar(int valor) {
		if (valor < 0 || valor > 9)
			valor = 0;
		
		grafico.setImage( (new ImageIcon(imagenes[valor])).getImage() );
		
	}
	
	/**
	 * Consulta el gráfico que representa la entidad gráfica.
	 * @return gráfico de la entidad gráfica.
	 */
	public ImageIcon getGrafico() {
		return grafico;
	}
	
}
