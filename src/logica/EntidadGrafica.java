package logica;

import javax.swing.ImageIcon;

public class EntidadGrafica {
	
	private ImageIcon grafico;
	private String [] imagenes;
	
	public EntidadGrafica() {
		grafico = new ImageIcon();
		imagenes = new String[9];
		for (int i = 0; i < 9; i++) {
			imagenes[i] = "/img/icon" + (i+1) + ".png";
		}
	}
	
	public void actualizar(int index) {
		if (0 <= index && index < imagenes.length) {
			ImageIcon imageIcon = new ImageIcon(this.getClass().getResource(this.imagenes[index]));
			this.grafico.setImage(imageIcon.getImage());
		}
	}

}
