package net.rlo.umcolorpicker.util;

import java.util.ArrayList;

import net.rlo.umcolorpicker.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * ArrayAdapter personalizado para mostrar un color, además de su nombre
 * y su valor hexadecimal, en una lista.
 * 
 * @author rafa
 *
 */
public class ColorAdapter extends ArrayAdapter<Color> {

	private int resourceId;
	
	public ColorAdapter(Context context, int resourceId, ArrayList<Color> items) {
		super(context, resourceId, items);
		this.resourceId = resourceId;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = convertView;
        if (row == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = vi.inflate(resourceId, null);
        }
        
        Color color = getItem(position);
        
        View colorView = row.findViewById(R.id.color_list_view);
        TextView name = (TextView) row.findViewById(R.id.color_name);
        TextView hex = (TextView) row.findViewById(R.id.hex_value);
        RatingBar ratingBar = (RatingBar) row.findViewById(R.id.rating_bar);
        
        colorView.setBackgroundColor(0xff000000 + Integer.valueOf(color.getHex(), 16));
        name.setText(color.getName());
        hex.setText("#"+color.getHex());
        ratingBar.setRating(color.getRating());
        
        return row;
	}
}
