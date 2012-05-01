package net.rlo.umcolorpicker.util;

import net.rlo.umcolorpicker.colormode.HSL;
import net.rlo.umcolorpicker.colormode.HSV;
import net.rlo.umcolorpicker.colormode.RGB;

/**
 * Almacena los datos usados por ColorPicker como son:<br/>
 * <ul>
 * <li>Modelo de color actual</li>
 * <li>Valor hexadecimal</li>
 * <li>Valor RGB</li>
 * <li>Valor HSV</li>
 * <li>Valor HSL</li>
 * </ul>
 * 
 * @author rafa
 *
 */
public class ColorPickerData {

	private int selectedMode;
	private String hex;
	private RGB rgb;
	private HSV hsv;
	private HSL hsl;
	
	public ColorPickerData(int selectedMode, String hex, RGB rgb, HSV hsv, HSL hsl) {
		this.selectedMode = selectedMode;
		this.hex = hex;
		this.rgb = rgb;
		this.hsv = hsv;
		this.hsl = hsl;
	}
	
	public int getSelectedMode() {
		return selectedMode;
	}
	public void setSelectedMode(int selectedMode) {
		this.selectedMode = selectedMode;
	}
	public String getHex() {
		return hex;
	}
	public void setHex(String hex) {
		this.hex = hex;
	}
	public RGB getRgb() {
		return rgb;
	}
	public void setRgb(RGB rgb) {
		this.rgb = rgb;
	}
	public HSV getHsv() {
		return hsv;
	}
	public void setHsv(HSV hsv) {
		this.hsv = hsv;
	}
	public HSL getHsl() {
		return hsl;
	}
	public void setHsl(HSL hsl) {
		this.hsl = hsl;
	}

	public String toString() {
		return "ColorPickerData [selectedMode=" + selectedMode + ", hex="
				+ hex + ", rgb=" + rgb + ", hsv=" + hsv + ", hsl=" + hsl + "]";
	}
	
}
