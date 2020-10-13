package logica;

import javax.swing.ImageIcon;

public class EntidadGrafica {
	
	protected ImageIcon grafico;
	protected String[] imagenes;
	
	public EntidadGrafica() {
		grafico = new ImageIcon();
		imagenes = new String[10];
		
		imagenes[0] = "src/img/blank.png";
		for (int i = 1; i <= 9; i++) {
			imagenes[i] = "src/img/n" + (i) + ".png";
		}
		
		
	}
	
	
	public void actualizar(int valor) {
		if (valor == -1 || valor > 9)
			valor = 0;
		
		if (0 <= valor && valor <= 9) {
			
			grafico.setImage( (new ImageIcon(imagenes[valor])).getImage() );
			
		}
		
	}
	
	public ImageIcon getGrafico() {
		return grafico;
	}
	
}
