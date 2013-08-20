package it.ott.simplepwgenerator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class SettingsFragment extends Fragment {
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.settings_main, container, false);
		AdView mAdView = (AdView) rootView.findViewById(R.id.ad1);
		AdRequest adRequest = new AdRequest();
		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);// test
		adRequest.addTestDevice("D0D84498FE8CF049FE62412B36D052BD");// emulator
		adRequest.addTestDevice("B0997F3050B92E20B96CBCE492478703");// emulator
		adRequest.addTestDevice("AFF5B720A3DE95AF11B67816CAB376AE");// galaxy star
		mAdView.loadAd(adRequest);
		// pref
		SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		int length = sharedPref.getInt(getString(R.string.value16), 16);
		EditText lunghezza = (EditText) rootView.findViewById(R.id.editText1);
		lunghezza.setText(String.valueOf(length));
		boolean sc = sharedPref.getBoolean(getString(R.id.toggleButton1), false);
		ToggleButton scBtn = (ToggleButton) rootView.findViewById(R.id.toggleButton1);
		scBtn.setChecked(sc);
		boolean num = sharedPref.getBoolean(getString(R.id.toggleButton2), false);
		ToggleButton numBtn = (ToggleButton) rootView.findViewById(R.id.toggleButton2);
		numBtn.setChecked(num);
		//
		return rootView;
	}
}
