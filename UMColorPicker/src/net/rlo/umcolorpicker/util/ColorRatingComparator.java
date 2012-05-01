package net.rlo.umcolorpicker.util;

import java.util.Comparator;

public class ColorRatingComparator implements Comparator<Color> {

	public int compare(Color c1, Color c2) {
		float rating1 = c1.getRating();
		float rating2 = c2.getRating();

		if (rating1 > rating2) {
			return -1;
		} else if (rating1 > rating2) {
			return 1;
		} else {
			return 0;
		}
	}

}
