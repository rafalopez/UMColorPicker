package net.rlo.umcolorpicker;

import net.rlo.umcolorpicker.colormode.ColorModeHelper;
import net.rlo.umcolorpicker.colormode.HSL;
import net.rlo.umcolorpicker.colormode.HSV;
import net.rlo.umcolorpicker.colormode.RGB;
import net.rlo.umcolorpicker.util.Color;
import net.rlo.umcolorpicker.util.ColorPickerData;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity para manejar toda la lógica de la selección de color por parte del usuario.
 * 
 * @author rafa
 * 
 */
public class ColorPickerActivity extends Activity {

	private static final String TAG = ColorPickerActivity.class.getSimpleName();

	// ID's de AlertDialog usados por la Activity
	private static final int SAVE_DIALOG_ID = 1;

	// Posibles valores del spinner con los modelos de color
	private static final int RGB = 0;
	private static final int HSV = 1;
	private static final int HSL = 2;

	// Propiedades para almacenar la información del selector de color
	private int selectedMode;
	private String hex;
	private RGB rgb;
	private HSV hsv;
	private HSL hsl;

	private MainActivity mainActivity;
	private String beforeStr;
	private String afterStr;
	private boolean editingInput = false;

	// Todos los controles de la vista
	private Spinner colorModelSpinner;
	private Button saveButton;
	private View colorView;
	private TextView hexVal;
	private TextView rgbVal;
	private TextView hsvVal;
	private TextView hslVal;
	private TextView label1;
	private TextView label2;
	private TextView label3;
	private SeekBar bar1;
	private SeekBar bar2;
	private SeekBar bar3;
	private EditText input1;
	private EditText input2;
	private EditText input3;

	// Elementos del AlertDialog para guardar el color
	private View saveColorPreview;
	private EditText saveColorName;
	private TextView saveHexValue;
	private RatingBar saveRatingBar;

	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "Creando Activity");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.color_picker);

		setUpViews();
		initValues();
	}

	public Object onRetainNonConfigurationInstance() {
		final ColorPickerData data = collectMyLoadedData();
		return data;
	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
		case SAVE_DIALOG_ID:
			dialog = createSaveDialog(id);
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case SAVE_DIALOG_ID:
			prepareSaveDialog(id, dialog);
			break;
		}
	}

	/*
	 * Carga los datos de un color seleccionado desde alguna de las listas
	 */
	protected void editFromColorList(String colorHexVal) {
		Log.d(TAG, "colorHexVal = " + colorHexVal);

		hex = colorHexVal;
		rgb = ColorModeHelper.Hex2RGB(hex);
		hsv = ColorModeHelper.RGB2HSV(rgb);
		hsl = ColorModeHelper.RGB2HSL(rgb);
		
		changeColorMode(selectedMode);
		updateViews();

	}

	/*
	 * Cada vez que hay un cambio de orientación (portrait/landscape)
	 * se guardan los datos actuales
	 */
	private ColorPickerData collectMyLoadedData() {
		return new ColorPickerData(selectedMode, hex, rgb, hsv, hsl);
	}

	/*
	 * Inicializa el diálogo que permite introducir nombre y puntuación de un
	 * color antes de añadirlo a la lista de favoritos
	 */
	private Dialog createSaveDialog(int id) {
		LayoutInflater li = LayoutInflater.from(this);
		View saveColorView = li.inflate(R.layout.save_color_dialog, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(saveColorView);
		builder.setTitle(R.string.save_dialog_title_str);
		builder.setPositiveButton(R.string.ok_str, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				saveColor();
			}
		});
		builder.setNegativeButton(R.string.cancel_str, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		return builder.create();
	}

	/*
	 * Vuelve a establecer los campos del diálogo para guardar el color a sus valores iniciales
	 */
	private void prepareSaveDialog(int id, Dialog dialog) {
		AlertDialog saveColorDialog = (AlertDialog) dialog;

		saveColorPreview = saveColorDialog.findViewById(R.id.color_view);
		saveColorName = (EditText) saveColorDialog.findViewById(R.id.save_color_name);
		saveHexValue = (TextView) saveColorDialog.findViewById(R.id.save_hex_val);
		saveRatingBar = (RatingBar) saveColorDialog.findViewById(R.id.save_rating_bar);

		saveColorPreview.setBackgroundColor(0xff000000 + Integer.valueOf(hex, 16));
		saveColorName.setText("");
		saveHexValue.setText("#" + hex);
		saveRatingBar.setRating(0.0f);
	}

	/*
	 * Obtiene las vistas del layout que posteriormente van a ser usadas.
	 * Establece todos los liseners necesarios para esas vistas.
	 */
	private void setUpViews() {
		mainActivity = (MainActivity) getParent();
		
		colorView = findViewById(R.id.color_view);
		hexVal = (TextView) findViewById(R.id.hex_value_txt);
		rgbVal = (TextView) findViewById(R.id.model_rgb_value_txt);
		hsvVal = (TextView) findViewById(R.id.model_hsv_value_txt);
		hslVal = (TextView) findViewById(R.id.model_hsl_value_txt);

		saveButton = (Button) findViewById(R.id.save_btn);
		saveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(SAVE_DIALOG_ID);
			}
		});

		colorModelSpinner = (Spinner) findViewById(R.id.color_model_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.color_model_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		colorModelSpinner.setAdapter(adapter);
		colorModelSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				changeColorMode(pos);
			}
			public void onNothingSelected(AdapterView<?> parent) {}
		});

		label1 = (TextView) findViewById(R.id.label1);
		label2 = (TextView) findViewById(R.id.label2);
		label3 = (TextView) findViewById(R.id.label3);

		bar1 = (SeekBar) findViewById(R.id.bar1);
		bar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStartTrackingTouch(SeekBar seekBar) {}
			public void onStopTrackingTouch(SeekBar seekBar) {}
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
				if (fromTouch) {
					seekBarChanged(bar1, input1, progress);
				}
			}
		});

		bar2 = (SeekBar) findViewById(R.id.bar2);
		bar2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {}
			public void onStartTrackingTouch(SeekBar seekBar) {}
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
				if (fromTouch) {
					seekBarChanged(bar2, input2, progress);
				}
			}
		});

		bar3 = (SeekBar) findViewById(R.id.bar3);
		bar3.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {}
			public void onStartTrackingTouch(SeekBar seekBar) {}
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
				if (fromTouch) {
					seekBarChanged(bar3, input3, progress);
				}
			}
		});

		input1 = (EditText) findViewById(R.id.input1);
		input1.addTextChangedListener(new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				beforeStr = s.toString();
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				afterStr = s.toString();
			}
			public void afterTextChanged(Editable s) {
				if (!editingInput) {
					inputChanged(input1, bar1);
				}
			}
		});

		input2 = (EditText) findViewById(R.id.input2);
		input2.addTextChangedListener(new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				beforeStr = s.toString();
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				afterStr = s.toString();
			}
			public void afterTextChanged(Editable s) {
				if (!editingInput) {
					inputChanged(input2, bar2);
				}
			}
		});

		input3 = (EditText) findViewById(R.id.input3);
		input3.addTextChangedListener(new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				beforeStr = s.toString();
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				afterStr = s.toString();
			}
			public void afterTextChanged(Editable s) {
				if (!editingInput) {
					inputChanged(input3, bar3);
				}
			}
		});

	}

	/*
	 * Modifica el modelo de color actual
	 */
	private void changeColorMode(int colorModeId) {
		Log.d(TAG, "colorModeId = " + colorModeId);

		switch (colorModeId) {
		case RGB:
			setRGBMode();
			break;
		case HSV:
			setHSVMode();
			break;
		case HSL:
			setHSLMode();
			break;
		}
	}

	/*
	 * Realiza las actualizaciones necesarias para establecer el modelo de color actual a RGB
	 */
	private void setRGBMode() {
		selectedMode = RGB;

		label1.setText(getString(R.string.model_rgb_r_str));
		label2.setText(getString(R.string.model_rgb_g_str));
		label3.setText(getString(R.string.model_rgb_b_str));

		bar1.setMax(255); bar1.setProgress(rgb.getR());
		bar2.setMax(255); bar2.setProgress(rgb.getG());
		bar3.setMax(255); bar3.setProgress(rgb.getB());

		editingInput = true;
		input1.setText(String.valueOf(rgb.getR()));
		input2.setText(String.valueOf(rgb.getG()));
		input3.setText(String.valueOf(rgb.getB()));
		editingInput = false;
	}

	/*
	 * Realiza las actualizaciones necesarias para establecer el modelo de color actual a HSV
	 */
	private void setHSVMode() {
		selectedMode = HSV;

		label1.setText(getString(R.string.model_hsv_h_str));
		label2.setText(getString(R.string.model_hsv_s_str));
		label3.setText(getString(R.string.model_hsv_v_str));

		bar1.setMax(360); bar1.setProgress((int) hsv.getH());
		bar2.setMax(100); bar2.setProgress((int) (hsv.getS() * 100.0));
		bar3.setMax(100); bar3.setProgress((int) (hsv.getV() * 100.0));

		editingInput = true;
		input1.setText(String.valueOf((int) hsv.getH()));
		input2.setText(String.valueOf((int) (hsv.getS() * 100.0)));
		input3.setText(String.valueOf((int) (hsv.getV() * 100.0)));
		editingInput = false;
	}

	/*
	 * Realiza las actualizaciones necesarias para establecer el modelo de color actual a HSL
	 */
	private void setHSLMode() {
		selectedMode = HSL;

		label1.setText(getString(R.string.model_hsl_h_str));
		label2.setText(getString(R.string.model_hsl_s_str));
		label3.setText(getString(R.string.model_hsl_l_str));

		bar1.setMax(360); bar1.setProgress((int) hsl.getH());
		bar2.setMax(100); bar2.setProgress((int) (hsl.getS() * 100.0));
		bar3.setMax(100); bar3.setProgress((int) (hsl.getL() * 100.0));

		editingInput = true;
		input1.setText(String.valueOf((int) hsl.getH()));
		input2.setText(String.valueOf((int) (hsl.getS() * 100.0)));
		input3.setText(String.valueOf((int) (hsl.getL() * 100.0)));
		editingInput = false;
	}

	/*
	 * Actualiza los datos según lo que haya seleccionado en los controles (SeekBar, Spinner, etc.)
	 */
	private void updateValues() {
		switch (selectedMode) {
		case RGB:
			rgb.setR(bar1.getProgress());
			rgb.setG(bar2.getProgress());
			rgb.setB(bar3.getProgress());
			hsv = ColorModeHelper.RGB2HSV(rgb);
			hsl = ColorModeHelper.RGB2HSL(rgb);
			hex = ColorModeHelper.RGB2Hex(rgb);
			break;
		case HSV:
			hsv.setH(bar1.getProgress());
			hsv.setS(bar2.getProgress() / 100.0);
			hsv.setV(bar3.getProgress() / 100.0);
			rgb = ColorModeHelper.HSV2RGB(hsv);
			hsl = ColorModeHelper.RGB2HSL(rgb);
			hex = ColorModeHelper.RGB2Hex(rgb);
			break;
		case HSL:
			hsl.setH(bar1.getProgress());
			hsl.setS(bar2.getProgress() / 100.0);
			hsl.setL(bar3.getProgress() / 100.0);
			rgb = ColorModeHelper.HSL2RGB(hsl);
			hsv = ColorModeHelper.RGB2HSV(rgb);
			hex = ColorModeHelper.RGB2Hex(rgb);
			break;
		}
	}

	/*
	 * Realiza las acciones necesarias cada vez que se modifica un SeekBar de
	 * alguno de los espacios de color del modelo actual
	 */
	private void seekBarChanged(SeekBar bar, EditText input, int progress) {
		input.setText(String.valueOf(progress));
		updateValues();
		updateViews();
	}

	/*
	 * Realiza las acciones necesarias cada vez que se modifica un EditText de
	 * alguno de los espacios de color del modelo actual
	 */
	private void inputChanged(EditText input, SeekBar bar) {
		if (TextUtils.isEmpty(afterStr)) {
			input.setText(beforeStr);
		} else {
			int inputVal = Integer.valueOf(afterStr);
			if (inputVal > bar.getMax()) {
				input.setText(beforeStr);
			} else {
				bar.setProgress(inputVal);
				updateValues();
				updateViews();
			}
		}
	}

	/*
	 * Muestra por pantalla los valores del color actual modificando las vistas necesarias.
	 */
	private void updateViews() {
		colorView.setBackgroundColor(0xff000000 + Integer.valueOf(hex, 16));
		hexVal.setText("#" + hex);
		rgbVal.setText(rgb.toString());
		hsvVal.setText(hsv.toString());
		hslVal.setText(hsl.toString());
	}

	/*
	 * Añade un nuevo color a la lista de favoritos Se ejecuta después de que el
	 * usuario pulse el botón OK en el diálogo de guardar color
	 */
	private void saveColor() {
		String colorName = saveColorName.getText().toString();
		if (TextUtils.isEmpty(colorName)) {
			// Si el usuario no introduce un nombre de color, el sistema asigna uno automáticamente
			colorName = getString(R.string.save_color_name_default_str, mainActivity.getFavouriteColorLst().size() + 1);
		}
		mainActivity.addFavouriteColor(new Color(colorName, saveHexValue.getText().toString().substring(1), saveRatingBar.getRating()));
		Toast.makeText(getApplicationContext(), R.string.save_color_ok_str, Toast.LENGTH_SHORT).show();
	}

	/*
	 * Establece los valores iniciales necesarios para el cálculo de cada modelo de color
	 */
	private void initValues() {
		ColorPickerData data = (ColorPickerData) getLastNonConfigurationInstance();

		if (data == null) { // Es la primera vez que se crea la Activity
			selectedMode = RGB;
			hex = "FFFFFF";
			rgb = new RGB(255, 255, 255);
			hsv = new HSV(0.0, 0.0, 1.0);
			hsl = new HSL(0.0, 0.0, 1.0);
		} else { // Se produjo un cambio de orientación de la pantalla previamente
			selectedMode = data.getSelectedMode();
			hex = data.getHex();
			rgb = data.getRgb();
			hsv = data.getHsv();
			hsl = data.getHsl();
			changeColorMode(selectedMode);
			updateViews();
		}
	}
}
