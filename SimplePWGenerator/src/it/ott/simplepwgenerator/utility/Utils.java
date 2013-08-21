package it.ott.simplepwgenerator.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;

public final class Utils {
	public static void loadPref(FragmentActivity act, int length, boolean sc, boolean num) {
		SharedPreferences sharedPref = act.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("len", length);
		editor.putBoolean("sc", sc);
		editor.putBoolean("num", num);
		editor.commit();
	}
}