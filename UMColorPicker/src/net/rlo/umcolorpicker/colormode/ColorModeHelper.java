package net.rlo.umcolorpicker.colormode;

/**
 * Utilidad para poder realizar la conversión entre diferentes modelos de color.
 * 
 * @author rafa
 *
 */
public class ColorModeHelper {

	/**
	 * Conversión del modelo RGB a HSV
	 * @param rgb Objeto de tipo {@link RGB} con el modelo del que se quiere realizar la conversión 
	 * @return Objeto de tipo {@link HSV} con el modelo al cual se realiza la conversión
	 */
	public static HSV RGB2HSV(RGB rgb) {
		return RGB2HSV(rgb.getR(), rgb.getG(), rgb.getB());
	}

	/**
	 * Conversión del modelo RGB a HSV
	 * @param red Canal R del modelo RGB
	 * @param green Canal G del modelo RGB
	 * @param blue Canal B del modelo RGB
	 * @return Objeto de tipo {@link HSV} con el modelo al cual se realiza la conversión
	 */
	public static HSV RGB2HSV(int red, int green, int blue) {
		// Normalizar valores red, green y blue
		double r = ((double) red / 255.0);
		double g = ((double) green / 255.0);
		double b = ((double) blue / 255.0);

		// Comenzar conversión
		double max = Math.max(r, Math.max(g, b));
		double min = Math.min(r, Math.min(g, b));

		double h = 0.0;
		if (max == r && g >= b) {
			h = 60 * (g - b) / (max - min);
		} else if (max == r && g < b) {
			h = 60 * (g - b) / (max - min) + 360;
		} else if (max == g) {
			h = 60 * (b - r) / (max - min) + 120;
		} else if (max == b) {
			h = 60 * (r - g) / (max - min) + 240;
		}

		double s = (max == 0) ? 0.0 : (1.0 - (min / max));

		return new HSV(h, s, (double) max);
	}

	/**
	 * Conversión del modelo HSV a RGB
	 * @param hsv Objeto de tipo {@link HSV} con el modelo del que se quiere realizar la conversión 
	 * @return Objeto de tipo {@link RGB} con el modelo al cual se realiza la conversión
	 */
	public static RGB HSV2RGB(HSV hsv) {
		return HSV2RGB(hsv.getH(), hsv.getS(), hsv.getV());
	}

	/**
	 * Conversión del modelo HSV a RGB
	 * @param h Canal H del modelo HSV
	 * @param s Canal S del modelo HSV
	 * @param v Canal V del modelo HSV
	 * @return Objeto de tipo {@link RGB} con el modelo al cual se realiza la conversión
	 */
	public static RGB HSV2RGB(double h, double s, double v) {
		double r = 0;
		double g = 0;
		double b = 0;

		if (s == 0) {
			r = g = b = v;
		} else {
			// The color wheel consists of 6 sectors. Figure out which sector you're in.
			double sectorPos = h / 60.0;
			int sectorNumber = (int) (Math.floor(sectorPos));
			// Get the fractional part of the sector
			double fractionalSector = sectorPos - sectorNumber;
			// Calculate values for the three axes of the color.
			double p = v * (1.0 - s);
			double q = v * (1.0 - (s * fractionalSector));
			double t = v * (1.0 - (s * (1 - fractionalSector)));

			// Assign the fractional colors to r, g, and b based on the sector the angle is in.
			switch (sectorNumber) {
			case 0:
				r = v; g = t; b = p;
				break;
			case 1:
				r = q; g = v; b = p;
				break;
			case 2:
				r = p; g = v; b = t;
				break;
			case 3:
				r = p; g = q; b = v;
				break;
			case 4:
				r = t; g = p; b = v;
				break;
			case 5:
				r = b; g = p; b = q;
				break;
			}
		}

		return new RGB((int) (r * 255.0), (int) (g * 255.0), (int) (b * 255.0));
	}

	/**
	 * Conversión del modelo RGB a HSL
	 * @param rgb Objeto de tipo {@link RGB} con el modelo del que se quiere realizar la conversión 
	 * @return Objeto de tipo {@link HSL} con el modelo al cual se realiza la conversión
	 */
	public static HSL RGB2HSL(RGB rgb) {
		return RGB2HSL(rgb.getR(), rgb.getG(), rgb.getB());
	}

	/**
	 * Conversión del modelo HSL a RGB
	 * @param red Canal R del modelo RGB
	 * @param green Canal G del modelo RGB
	 * @param blue Canal B del modelo RGB
	 * @return Objeto de tipo {@link HSL} con el modelo al cual se realiza la conversión
	 */
	public static HSL RGB2HSL(int red, int green, int blue) {
		double h = 0, s = 0, l = 0;

		// normalize red, green, blue values
		double r = (double) red / 255.0;
		double g = (double) green / 255.0;
		double b = (double) blue / 255.0;

		double max = Math.max(r, Math.max(g, b));
		double min = Math.min(r, Math.min(g, b));

		// hue
		if (max == min) {
			h = 0; // undefined
		} else if (max == r && g >= b) {
			h = 60.0 * (g - b) / (max - min);
		} else if (max == r && g < b) {
			h = 60.0 * (g - b) / (max - min) + 360.0;
		} else if (max == g) {
			h = 60.0 * (b - r) / (max - min) + 120.0;
		} else if (max == b) {
			h = 60.0 * (r - g) / (max - min) + 240.0;
		}

		// luminance
		l = (max + min) / 2.0;

		// saturation
		if (l == 0 || max == min) {
			s = 0;
		} else if (0 < l && l <= 0.5) {
			s = (max - min) / (max + min);
		} else if (l > 0.5) {
			s = (max - min) / (2 - (max + min)); // (max-min > 0)?

		}

		return new HSL(h, s, l);
	}

	/**
	 * Conversión del modelo HSL a RGB
	 * @param hsl Objeto de tipo {@link HSL} con el modelo del que se quiere realizar la conversión 
	 * @return Objeto de tipo {@link RGB} con el modelo al cual se realiza la conversión
	 */
	public static RGB HSL2RGB(HSL hsl) {
		return HSL2RGB(hsl.getH(), hsl.getS(), hsl.getL());
	}

	/**
	 * Conversión del modelo HSL a RGB
	 * @param h Canal H del modelo HSL
	 * @param s Canal S del modelo HSL
	 * @param l Canal L del modelo HSL
	 * @return Objeto de tipo {@link RGB} con el modelo al cual se realiza la conversión
	 */
	public static RGB HSL2RGB(double h, double s, double l) {
		if (s == 0) {
			// achromatic color (gray scale)
			return new RGB((int) (l * 255.0), (int) (l * 255.0), (int) (l * 255.0));
		} else {
			double q = (l < 0.5) ? (l * (1.0 + s)) : (l + s - (l * s));
			double p = (2.0 * l) - q;
			double Hk = h / 360.0;
			double[] T = new double[3];
			T[0] = Hk + (1.0 / 3.0); // Tr
			T[1] = Hk; // Tb
			T[2] = Hk - (1.0 / 3.0); // Tg

			for (int i = 0; i < 3; i++) {
				if (T[i] < 0) {
					T[i] += 1.0;
				}
				if (T[i] > 1) {
					T[i] -= 1.0;
				}

				if ((T[i] * 6) < 1) {
					T[i] = p + ((q - p) * 6.0 * T[i]);
				} else if ((T[i] * 2.0) < 1) { // (1.0/6.0)<=T[i] && T[i]<0.5
					T[i] = q;
				} else if ((T[i] * 3.0) < 2) { // 0.5<=T[i] && T[i]<(2.0/3.0)
					T[i] = p + (q - p) * ((2.0 / 3.0) - T[i]) * 6.0;
				} else
					T[i] = p;
			}

			return new RGB((int) (T[0] * 255.0), (int) (T[1] * 255.0), (int) (T[2] * 255.0));
		}
	}

	/**
	 * Conversión del modelo RGB a Hexadecimal
	 * @param rgb Objeto de tipo {@link RGB} con el modelo del que se quiere realizar la conversión 
	 * @return String con el valor hexadecimal de el color
	 */
	public static String RGB2Hex(RGB rgb) {
		return RGB2Hex(rgb.getR(), rgb.getG(), rgb.getB());
	}

	/**
	 * Conversión del modelo RGB a Hexadecimal
	 * @param r Canal R del modelo RGB
	 * @param g Canal G del modelo RGB
	 * @param b Canal B del modelo RGB
	 * @return String con el valor hexadecimal de el color
	 */
	public static String RGB2Hex(int r, int g, int b) {
		return String.format("%02X%02X%02X", r, g, b);
	}
	
	/**
	 * Conversión del modelo Hexadecimal a RGB
	 * @param hex Valor a hexadecimal del que se quiere realizar la conversión
	 * @return Objeto de tipo {@link RGB} con el modelo al cual se realiza la conversión
	 */
	public static RGB Hex2RGB(String hex) {
		return new RGB(
	            Integer.valueOf(hex.substring( 0, 2 ), 16),
	            Integer.valueOf(hex.substring( 2, 4 ), 16),
	            Integer.valueOf(hex.substring( 4, 6 ), 16));
	}
}
