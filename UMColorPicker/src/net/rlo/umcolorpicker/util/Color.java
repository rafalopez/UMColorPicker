package net.rlo.umcolorpicker.util;

/**
 * Representa la información necesaria de un color para mostrar
 * en alguna de la listas de la aplicación. Dicha información es:
 * <ul>
 * <li>Nombre</li>
 * <li>Valor hexadecimal</li>
 * <li>Valoración o rating</li>
 * </ul>
 * 
 * @author rafa
 *
 */
public class Color {

	private String name;
	private String hex;
	private float rating = 0.0f;
	
	public Color(String name, String hex) {
		this.name = name;
		this.hex = hex;
	}
	public Color(String name, String hex, float rating) {
		this(name, hex);
		this.rating = rating;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHex() {
		return hex;
	}
	public void setHex(String hex) {
		this.hex = hex;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	
}
