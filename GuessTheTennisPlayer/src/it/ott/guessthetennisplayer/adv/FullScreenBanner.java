package it.ott.guessthetennisplayer.adv;

import it.ott.guessthetennisplayer.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.InterstitialAd;

public class FullScreenBanner extends Activity implements AdListener {
	private InterstitialAd interstitial;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play);
		// Create the interstitial
		interstitial = new InterstitialAd(this, "ca-app-pub-6205760010010665/7930429146");
		// Create ad request
		AdRequest adRequest = new AdRequest();
		// Begin loading your interstitial
		interstitial.loadAd(adRequest);
		// Set Ad Listener to use the callbacks below
		interstitial.setAdListener(this);
	}

	@Override
	public void onReceiveAd(Ad ad) {
		Log.d("OK", "Received ad");
		if (ad == interstitial) {
			interstitial.show();
		}
	}

	@Override
	public void onDismissScreen(Ad arg0) {
	}

	@Override
	public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
	}

	@Override
	public void onLeaveApplication(Ad arg0) {
	}

	@Override
	public void onPresentScreen(Ad arg0) {
	}
}