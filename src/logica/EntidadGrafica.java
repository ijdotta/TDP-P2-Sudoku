package logica;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class EntidadGrafica {
	
	protected JButton celdaGrafica;
	protected String[] imagenes;
	
	/**
	 * Crea la entidad gráfica de la celda.
	 * Carga sus imagenes.
	 */
	public EntidadGrafica() {
		imagenes = new String[10];
		for (int i = 0; i < 9; i++) {
			imagenes[i] = "src/img/n" + (i+1) + ".png";
		}
		imagenes[9] = "src/img/blank.png";
		
		/*Necesito que cada entidad gráfica cree su propio botón para poder actualiar la imagen desde que es creada
		 * Sino, la GUI tiene que forzar la actualización a través del juego; preguntando el valor de cada celda*/
		celdaGrafica = new JButton();
	}
	
	
	public void actualizar(int valor) {
		if (valor == -1 || (1 <= valor && valor <= 9) ) {
			
			//Determinar índice de imagen
			valor = valor == -1 ? 9 : valor-1;
			
			ImageIcon icon = new ImageIcon(imagenes[valor]);
			Image img = icon.getImage();
			
			///verrrrrrrrrrrrrrrrr
			int width, height;
			width = height = celdaGrafica.getWidth();
			
			if (height == 0)
				width = height = 10;
			//verrrrrrrrrrrrrrrrr
//			Image newimg = img.getScaledInstance(celdaGrafica.getWidth(), celdaGrafica.getHeight(), java.awt.Image.SCALE_SMOOTH);
			Image newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
			icon.setImage(newimg);
			celdaGrafica.setIcon(icon);
			celdaGrafica.setBackground(Color.WHITE); //Ver si no conviene tener un método que permite pintarla directamente
			celdaGrafica.repaint();//Es necesario??
			
			//Creo que no hace falta el getScaledInstance si se crea un imgicn nuevo
			//RTA:: SÍ HACE FALTA
		}
	}
	
	public void setCorrecta(boolean esCorrecta) {
		if (esCorrecta) {
			celdaGrafica.setBackground(Color.WHITE);
		}
		else {
			celdaGrafica.setBackground(Color.RED);
		}
		celdaGrafica.repaint();
	}

	public JButton getCeldaGrafica() {
		return celdaGrafica;
	}
	
}
