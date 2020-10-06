package logica;

import javax.swing.ImageIcon;

public class EntidadGrafica {
	
	protected ImageIcon grafico;
	protected String[][] imagenes;
	
	/**
	 * 
	 */
	public EntidadGrafica() {
		grafico = new ImageIcon();
		imagenes = new String[10][2];
		for (int i = 0; i < 9; i++) {
			imagenes[i][0] = "src/img/n" + (i+1) + ".png";
			imagenes[i][1] = "src/img/r" + (i+1) + ".png";
		}
		imagenes[9][0] = "src/img/blank.png";
	}
	
	/**
	 * 
	 * @param valor
	 * @param correcto
	 */
	public void actualizar(int valor, boolean correcto) {
		int type;
		if (1 <= valor && valor <= 9) {
			type = correcto ? 0 : 1;
			grafico.setImage( (new ImageIcon(imagenes[valor-1][type])).getImage() );
		}
		if (valor == -1) {
			grafico.setImage( (new ImageIcon(imagenes[9][0])).getImage() );
		}
	}
	
	public void setGrafico(ImageIcon grafico) {
		this.grafico = grafico;
	}
	
	public ImageIcon getGrafico() {
		return grafico;
	}
	
}
