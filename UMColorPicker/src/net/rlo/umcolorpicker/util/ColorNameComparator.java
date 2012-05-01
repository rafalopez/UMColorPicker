package net.rlo.umcolorpicker.util;

import java.util.Comparator;

public class ColorNameComparator implements Comparator<Color> {

	public int compare(Color c1, Color c2) {
		return c1.getName().compareTo(c2.getName());
	}
	
}

