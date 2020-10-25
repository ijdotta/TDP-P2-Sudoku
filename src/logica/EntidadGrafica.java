package logica;

import java.net.URL;

import javax.swing.ImageIcon;

/**
 * Class EntidadGrafica - Modela la parte gráfica de una Celda.
 * @author Ignacio Joaquín Dotta
 *
 */
public class EntidadGrafica {
	
	protected ImageIcon grafico;
	protected URL[] imagenes;
	
	/**
	 * Crea una entidad gráfica y define el conjunto de imágenes posibles.
	 */
	public EntidadGrafica() {
		grafico = new ImageIcon();
		imagenes = new URL[10];
		
		URL path = getClass().getResource("/resources/img/Numeros/blank.png");
		
		imagenes[0] = path;
		for (int i = 1; i <= 9; i++) {
			path = getClass().getResource("/resources/img/Numeros/n" + i + ".png");
			imagenes[i] = path;
		}
		
	}
	
	/**
	 * Actualiza la imagen de la entidad gráfica según el valor.
	 * @param valor Valor nuevo.
	 */
	public void actualizar(int valor) {
		if (valor < 0 || valor > 9)
			valor = valor % 9; 
		/*
		 * Esto lo agrego solo para evitar errores en ejecución cuando se elije un tablero de sudoku de más
		 * de 9 números (16x16, 25x25, 36x36, ...) y no están disponibles las imágenes correspondientes.
		 */
		
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
