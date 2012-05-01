package net.rlo.umcolorpicker;

import net.rlo.umcolorpicker.util.Color;
import net.rlo.umcolorpicker.util.ColorAdapter;
import net.rlo.umcolorpicker.util.ColorHexComparator;
import net.rlo.umcolorpicker.util.ColorNameComparator;
import net.rlo.umcolorpicker.util.ColorRatingComparator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Activity que muestra por pantalla una lista con información de los colores
 * seleccionados como "Favoritos" por el usuario
 * 
 * @author rafa
 *
 */
public class FavouritesActivity extends Activity {
	
	private static final String TAG = FavouritesActivity.class.getSimpleName();
	
	private static final int DELETE_DIALOG_ID = 1;
	
	private ListView favouritesListView;
	private ColorAdapter colorAdapter;
	
	private MainActivity mainActivity;
	private Color selectedColor;
	
	
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "Creando activity");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favourites);
		
		setUpViews();
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.favourites_menu, menu);
	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case R.id.sort_by_name:
	    	item.setChecked(true);
	    	colorAdapter.sort(new ColorNameComparator());
	        return true;
	    case R.id.sort_by_hex_value:
	    	item.setChecked(true);
	    	colorAdapter.sort(new ColorHexComparator());
	        return true;
	    case R.id.sort_by_rating:
	    	item.setChecked(true);
	    	colorAdapter.sort(new ColorRatingComparator());
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
		case DELETE_DIALOG_ID:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.delete_color_title_str);
			builder.setMessage(R.string.delete_color_message_1_str);
			builder.setPositiveButton(R.string.ok_str, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					colorAdapter.remove(selectedColor);
					Toast.makeText(getApplicationContext(), getString(R.string.color_deleted_str, selectedColor.getName()), Toast.LENGTH_SHORT).show();
				}
			});
			builder.setNegativeButton(R.string.cancel_str, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			dialog = builder.create();
			break;
		default:
			dialog = null;
		}
		return dialog;
	}
	
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DELETE_DIALOG_ID:
			AlertDialog deleteColorDialog = (AlertDialog) dialog;
			deleteColorDialog.setMessage(getString(R.string.delete_color_message_2_str, selectedColor.getName()));
			break;
		}
	}
	
    protected void onResume() {
        super.onResume();
        // La lista de colores puede haber sido actualizada desde otras Activities (ColorPickerActivity)
        colorAdapter.notifyDataSetChanged();
    }
    
    /*
     * Este método es llamado cada vez que se quiere notificar al manejador de
     * la lista de colores favoritos que dicha lista ha cambiado.
     */
    protected void notifyFavouriteColorListChanged() {
    	colorAdapter.notifyDataSetChanged();
    }
	
	/*
	 * Obtiene las vistas del layout que posteriormente van a ser usadas.
	 * Establece todos los liseners necesarios para esas vistas.
	 */
	private void setUpViews() {
		mainActivity = (MainActivity) getParent();
		favouritesListView = (ListView) findViewById(R.id.favourites_list_view);
		colorAdapter = new ColorAdapter(this, R.layout.color, mainActivity.getFavouriteColorLst());
		favouritesListView.setAdapter(colorAdapter);
		favouritesListView.setEmptyView(findViewById(R.id.favourites_list_empty));
		favouritesListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				selectedColor = colorAdapter.getItem(position);
				showDialog(DELETE_DIALOG_ID);
				return true;
			}
		});
		favouritesListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				((MainActivity)getParent()).editColor(colorAdapter.getItem(position).getHex());
			}
		});
	}
	
}

