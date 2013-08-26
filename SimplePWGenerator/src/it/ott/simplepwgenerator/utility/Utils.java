package it.ott.simplepwgenerator.utility;

import it.ott.simplepwgenerator.MainActivity;
import it.ott.simplepwgenerator.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;

public final class Utils {
	public static void loadPref(FragmentActivity act, int length, boolean sc, boolean num, boolean memo) {
		SharedPreferences sharedPref = act.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("len", length);
		editor.putBoolean("sc", sc);
		editor.putBoolean("num", num);
		editor.putBoolean("memo", memo);
		editor.commit();
	}

	public static boolean isOnline(Context ctx) {
		ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null)
			return false;
		if (!i.isConnected())
			return false;
		if (!i.isAvailable())
			return false;
		return true;
	}

	public static void internetAlert(MainActivity mainActivity) {
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage(R.string.alert_msg).setTitle(R.string.alert_title);
		// Add the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User clicked OK button
			}
		});
		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		if (!dialog.isShowing())
			dialog.show();
	}
}