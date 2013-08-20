package it.ott.simplepwgenerator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class GenerateFragment extends Fragment {
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.generate_main, container, false);
		AdView mAdView = (AdView) rootView.findViewById(R.id.ad);
		// mAdView.setAdListener(new MyAdListener());
		AdRequest adRequest = new AdRequest();
		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);// test
		adRequest.addTestDevice("D0D84498FE8CF049FE62412B36D052BD");// emulator
		adRequest.addTestDevice("B0997F3050B92E20B96CBCE492478703");// emulator
		adRequest.addTestDevice("AFF5B720A3DE95AF11B67816CAB376AE");// galaxy star
		mAdView.loadAd(adRequest);
		return rootView;
	}
	/*
	 * public class MyAdListener implements AdListener {
	 * 
	 * @Override public void onDismissScreen(Ad arg0) { }
	 * 
	 * @Override public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) { }
	 * 
	 * @Override public void onLeaveApplication(Ad arg0) { }
	 * 
	 * @Override public void onPresentScreen(Ad arg0) { }
	 * 
	 * @Override public void onReceiveAd(Ad arg0) { } }
	 */
}
