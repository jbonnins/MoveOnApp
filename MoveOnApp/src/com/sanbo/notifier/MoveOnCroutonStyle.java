package com.sanbo.notifier;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Gravity;

import com.actionbarsherlock.app.ActionBar.LayoutParams;
import com.sanbo.moveonapp.R;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

@SuppressLint("ResourceAsColor")
public class MoveOnCroutonStyle {


@SuppressWarnings("unused")
private static final int DURATION_INFINITE = -1;
private static final Style ALERT;
private static final Style WARN;
private static final Style FLY;
private static final Style CONFIRM;
private static final Style INFO;

private static final Configuration configXS;
private static final Configuration configS;
private static final Configuration configM;
private static final Configuration configL;
private static final Configuration configXL;

private static final int DURATION_XL = 3000;
private static final int DURATION_L  = 2700;
private static final int DURATION_M  = 2400;
private static final int DURATION_S  = 2000;
private static final int DURATION_XS = 1500;


private static final int holoRedLight = 0xFFFA6F7A;
private static final int holoOrangeLight = 0xFFFFBB00;
private static final int holoYellowLight = 0xFFFFFF00;
private static final int holoBlueLight = 0xFF00BBFF;
private static final int holoGreenLight = 0xFF99cc00;


static {
	configXS = new Configuration.Builder()
		.setDuration(DURATION_XS).build();
	configS = new Configuration.Builder()
		.setDuration(DURATION_S).build();
	configM = new Configuration.Builder()
		.setDuration(DURATION_M).build();
	configL = new Configuration.Builder()
	.setDuration(DURATION_L).build();
	configXL = new Configuration.Builder()
	.setDuration(DURATION_XL).build();

	// color AlertRed
    ALERT   = new Style.Builder()
    			.setConfiguration(configXL)
                .setBackgroundColorValue(holoRedLight)
                .setTextColor(R.color.colorTexto_Blanco)
                .setHeight(LayoutParams.WRAP_CONTENT)
                .build();
    // color AzulMoveOn
    // color TextMoveOn
    WARN    = new Style.Builder()
    			.setConfiguration(configL)
                .setBackgroundColorValue(holoOrangeLight)
                .setTextColor(R.color.colorTexto_Negro)
                .setGravity(Gravity.CENTER_HORIZONTAL)
                .setHeight(LayoutParams.WRAP_CONTENT)
                .build();   
    // color InfoYellow
    FLY    = new Style.Builder()
		.setConfiguration(configM)
	    .setBackgroundColorValue(holoYellowLight)
	    .setTextColor(R.color.colorTexto_Negro)
	    .setGravity(Gravity.CENTER_HORIZONTAL)
	    .setHeight(LayoutParams.WRAP_CONTENT)
	    .build();   

    // color InfoYellow
    INFO    = new Style.Builder()
    			.setConfiguration(configS)
                .setBackgroundColorValue(holoBlueLight)
                .setTextColor(R.color.colorTexto_Blanco)
                .setHeight(LayoutParams.WRAP_CONTENT)
                .build();

    // color ConfirmGreen
    CONFIRM = new Style.Builder()
    			.setConfiguration(configXS)
                .setBackgroundColorValue(holoGreenLight)
                .setTextColor(R.color.colorTexto_Blanco)
                .setHeight(LayoutParams.WRAP_CONTENT)
                .build();
}

/**             Crouton Wrappers                 **/
public static void croutonAlert(Activity activity, int stringId){
    Crouton.makeText(activity, stringId, MoveOnCroutonStyle.ALERT).show();
}
public static void croutonAlert(Activity activity, String text){
    Crouton.makeText(activity, text, MoveOnCroutonStyle.ALERT).show();
}

public static void croutonInfo(Activity activity, int stringId){
    Crouton.makeText(activity, stringId, MoveOnCroutonStyle.INFO).show();
}
public static void croutonInfo(Activity activity, String text){
    Crouton.makeText(activity, text, MoveOnCroutonStyle.INFO).show();
}

public static void croutonConfirm(Activity activity, int stringId){
    Crouton.makeText(activity, stringId, MoveOnCroutonStyle.CONFIRM).show();
}
public static void croutonConfirm(Activity activity, String text){
    Crouton.makeText(activity, text, MoveOnCroutonStyle.CONFIRM).show();
}
public static void croutonWarn(Activity activity, int stringId){
    Crouton.makeText(activity, stringId, MoveOnCroutonStyle.WARN).show();
}
public static void croutonWarn(Activity activity, String text){
    Crouton.makeText(activity, text, MoveOnCroutonStyle.WARN).show();
}
public static void croutonFly(Activity activity, int stringId){
    Crouton.makeText(activity, stringId, MoveOnCroutonStyle.FLY).show();
}
public static void croutonFly(Activity activity, String text){
    Crouton.makeText(activity, text, MoveOnCroutonStyle.FLY).show();
}

// in OnDestroy in your activity
// Clean crouton from activity
//Crouton.clearCroutonsForActivity();
//Clean ALL croutons
//Crouton.cancelAllCroutons();


}