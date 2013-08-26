package it.ott.simplepwgenerator;

import it.ott.simplepwgenerator.utility.PWUtils;
import it.ott.simplepwgenerator.utility.Utils;

import java.util.ArrayList;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends FragmentActivity {
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will keep every loaded fragment in memory. If this becomes too
	 * memory intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_settings:
			mViewPager.setCurrentItem(1);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void generate(View v) {
		TextView lunghezza = (TextView) findViewById(R.id.editText1);
		ToggleButton spChars = (ToggleButton) findViewById(R.id.toggleButton1);
		ToggleButton numbers = (ToggleButton) findViewById(R.id.toggleButton2);
		ToggleButton memorable = (ToggleButton) findViewById(R.id.isMemorableTB);
		boolean sc = spChars.isChecked();
		boolean num = numbers.isChecked();
		boolean memo = memorable.isChecked();
		int length = Integer.parseInt(lunghezza.getText().toString());
		// pref
		Utils.loadPref(this, length, sc, num, memo);
		String password = "";
		if (!memo) {
			password = PWUtils.generatePassword(length, sc, num);
		} else {
			ArrayList<String> quotes = PWUtils.preparePasswordFamiliar();
			password = PWUtils.generatePasswordFamiliar(length, sc, num, quotes);
		}
		TextView out = (TextView) findViewById(R.id.textView1);
		out.setText(password);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new GenerateFragment();
			case 1:
				return new SettingsFragment();
			case 2:
				return new AboutFragment();
			default:
				return null;
			}
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}
}
