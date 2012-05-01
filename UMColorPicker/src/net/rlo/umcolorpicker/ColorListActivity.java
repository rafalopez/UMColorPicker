package net.rlo.umcolorpicker;

import java.util.ArrayList;

import net.rlo.umcolorpicker.util.Color;
import net.rlo.umcolorpicker.util.ColorAdapter;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * Activity que muestra una lista de colores predefinidos junto con el
 * nombre que se le ha dado a cada uno.
 * 
 * @author rafa
 *
 */
public class ColorListActivity extends Activity {
	
	private static final String TAG = ColorListActivity.class.getSimpleName();
	
	private ArrayList<Color> colorLst;
	
	private ListView colorListView;
	private ColorAdapter colorAdapter;
	
	private MainActivity mainActivity;
	
	
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "Creando activity");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.color_list);
		
		loadColorLst();
		setUpViews();
	}
	
	/*
	 * Carga la lista de colores desde dos array ubicados en la carpeta de recursos
	 */
	private void loadColorLst() {
		colorLst = new ArrayList<Color>();
		
		Resources res = getResources();
		String[] colorNameArray = res.getStringArray(R.array.color_name_array);
		String[] colorValueArray = res.getStringArray(R.array.color_value_array);
		for (int i = 0; i < colorNameArray.length; i++) {
			colorLst.add(new Color(colorNameArray[i], colorValueArray[i]));
		}
	}
	
	/*
	 * Obtiene las vistas del layout que posteriormente van a ser usadas.
	 * Establece todos los liseners necesarios para esas vistas.
	 */
	private void setUpViews() {
		mainActivity = (MainActivity) getParent();
		colorListView = (ListView) findViewById(R.id.color_list_view);
		colorAdapter = new ColorAdapter(this, R.layout.color, colorLst);
		colorListView.setAdapter(colorAdapter);
		colorListView.setEmptyView(findViewById(R.id.color_list_empty));
		colorListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mainActivity.editColor(colorAdapter.getItem(position).getHex());
			}
		});
	}
	
}
