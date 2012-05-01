package net.rlo.umcolorpicker.util;

import java.util.Comparator;

public class ColorHexComparator implements Comparator<Color> {

	public int compare(Color c1, Color c2) {
		return c1.getHex().compareTo(c2.getHex());
	}
	
}

