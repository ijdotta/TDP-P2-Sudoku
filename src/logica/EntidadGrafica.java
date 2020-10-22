package logica;

import java.net.URL;

import javax.swing.ImageIcon;

public class EntidadGrafica {
	
	protected ImageIcon grafico;
//	protected String[] imagenes;
	protected URL[] imagenes;
	
	/**
	 * Crea una entidad gráfica y define el conjunto de imágenes posibles.
	 */
//	public EntidadGrafica() {
//		grafico = new ImageIcon();
//		imagenes = new String[10];
//		
//		URL path = getClass().getResource("/img/Numeros/blank.png");
//		
//		imagenes[0] = path.getFile();
//		for (int i = 1; i <= 9; i++) {
//			path = getClass().getResource("/img/Numeros/n" + i + ".png");
//			imagenes[i] = path.getFile();
//		}
//		
//	}
	public EntidadGrafica() {
		grafico = new ImageIcon();
		imagenes = new URL[10];
		
		URL path = getClass().getResource("/img/Numeros/blank.png");
		
		imagenes[0] = path;
		for (int i = 1; i <= 9; i++) {
			path = getClass().getResource("/img/Numeros/n" + i + ".png");
			imagenes[i] = path;
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
