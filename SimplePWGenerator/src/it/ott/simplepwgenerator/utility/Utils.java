package it.ott.simplepwgenerator.utility;

import it.ott.simplepwgenerator.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;

public final class Utils {
	public static void loadPref(FragmentActivity act, int length, boolean sc, boolean num) {
		SharedPreferences sharedPref = act.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(act.getString(R.string.value16), length);
		editor.putBoolean(act.getString(R.id.toggleButton1), sc);
		editor.putBoolean(act.getString(R.id.toggleButton2), num);
		editor.commit();
	}
}
