package net.rlo.umcolorpicker;

import java.util.ArrayList;

import net.rlo.umcolorpicker.util.Color;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

/**
 * Clase principal que se cargará al inicar la aplicación UM Color Picker.<br/>
 * Genera las pestañas e inicializa las Activities que contendrá cada pestaña.<br/>
 * También es la encargada de la interacción entre las distintas pestañas, paso de parámetros, etc.
 * 
 * @author rafa
 *
 */
public class MainActivity extends TabActivity {
    
	private static final String TAG = MainActivity.class.getSimpleName();
	
	private static final String COLOR_PICKER_TAB = "colorpicker";
	private static final String COLOR_LIST_TAB = "colorlist";
	private static final String FAVOURITES_TAB = "favourites";
	
	private ArrayList<Color> favouriteColorLst;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initFavouritesColorLst();
        setUpTabs();
    }
    
    public Object onRetainNonConfigurationInstance() {
    	return favouriteColorLst;
    }
    
    /**
     * Inicializa los valores de la Activity encargada de la selección de color
     * según un valor de color recibido en formato hexadecimal
     * 
     * @param colorHexVal Valor hexadecimal que representa el color a editar
     * formado por 6 caracteres en el rango 0-F
     */
    public void editColor(String colorHexVal) {
    	Log.d(TAG, "colorHexVal = " + colorHexVal);
    	
    	ColorPickerActivity colorPickerActivity = (ColorPickerActivity) getLocalActivityManager().getActivity(COLOR_PICKER_TAB);
    	colorPickerActivity.editFromColorList(colorHexVal);
    	getTabHost().setCurrentTabByTag(COLOR_PICKER_TAB);
    }
    
    /**
     * Añade un nuevo color a la lista de colores favoritos.
     * En caso de que ya se haya creado la Activity de favoritos, le notifica el cambio
     * para que muestre la nueva lista por pantalla.
     * 
     * @param color Clase de tipo {@link Color} con la información del color que se quiere añadir
     * a la lista de favoritos.
     */
    public void addFavouriteColor(Color color) {
    	favouriteColorLst.add(color);
    	
    	FavouritesActivity favouritesActivity = (FavouritesActivity) getLocalActivityManager().getActivity(FAVOURITES_TAB);
    	if (favouritesActivity != null) {
    		favouritesActivity.notifyFavouriteColorListChanged();
    	}
    	
    }

	public ArrayList<Color> getFavouriteColorLst() {
		return favouriteColorLst;
	}

	public void setFavouriteColorLst(ArrayList<Color> favouriteColorLst) {
		this.favouriteColorLst = favouriteColorLst;
	}
    
    /*
	 * Inicializa las pestañas y las Activities asociadas a cada una.
	 */
    private void setUpTabs() {
    	Resources res = getResources();
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, ColorPickerActivity.class);
		spec = tabHost.newTabSpec(COLOR_PICKER_TAB)
				.setIndicator(getString(R.string.color_picker_tab_str),res.getDrawable(R.drawable.ic_tab_color_picker))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, ColorListActivity.class);
		spec = tabHost.newTabSpec(COLOR_LIST_TAB)
				.setIndicator(getString(R.string.color_list_tab_str), res.getDrawable(R.drawable.ic_tab_list))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, FavouritesActivity.class);
		spec = tabHost.newTabSpec(FAVOURITES_TAB)
				.setIndicator(getString(R.string.favourites_tab_str), res.getDrawable(R.drawable.ic_tab_favourites))
				.setContent(intent);
		tabHost.addTab(spec);
		
    }
    
    @SuppressWarnings("unchecked")
	private void initFavouritesColorLst() {
    	favouriteColorLst = (ArrayList<Color>) getLastNonConfigurationInstance();
    	if (favouriteColorLst == null) {
    		favouriteColorLst = new ArrayList<Color>();
    	}
    }
    
}