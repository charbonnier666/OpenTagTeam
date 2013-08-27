package it.ott.guessthetennisplayer;

import it.ott.guessthetennisplayer.gamesutils.BaseGameActivity;

import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.games.Player;

public class MainActivity extends BaseGameActivity implements MainMenuFragment.Listener, GameplayFragment.Listener, WinFragment.Listener {
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
	// Fragments
	MainMenuFragment mMainMenuFragment;
	GameplayFragment mGameplayFragment;
	WinFragment mWinFragment;
	// request codes we use when invoking an external activity
	final int RC_RESOLVE = 5000, RC_UNUSED = 5001;
	// tag for debug logging
	final boolean ENABLE_DEBUG = true;
	final String TAG = "TanC";
	// playing on hard mode?
	boolean mHardMode = false;
	// achievements and scores we're pending to push to the cloud
	// (waiting for the user to sign in, for instance)
	AccomplishmentsOutbox mOutbox = new AccomplishmentsOutbox();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		enableDebugLog(ENABLE_DEBUG, TAG);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		// create fragments
		mMainMenuFragment = new MainMenuFragment();
		mGameplayFragment = new GameplayFragment();
		mWinFragment = new WinFragment();
		// listen to fragment events
		mMainMenuFragment.setListener(this);
		mGameplayFragment.setListener(this);
		mWinFragment.setListener(this);
		// add initial fragment (welcome fragment)
		// getSupportFragmentManager().beginTransaction().add(R.id.frag_container, mMainMenuFragment).commit();
		// IMPORTANT: if this Activity supported rotation, we'd have to be
		// more careful about adding the fragment, since the fragment would
		// already be there after rotation and trying to add it again would
		// result in overlapping fragments. But since we don't support rotation,
		// we don't deal with that for code simplicity.
		// load outbox from file
		mOutbox.loadLocal(this);
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

	public void retrievePlayers(View v) {
		Resources res = getResources();
		String[] names = res.getStringArray(R.array.names);
		for (int i = 0; i < names.length; i++) {
			System.out.println(names[i]);
		}
	}

	@Override
	public void onSignInFailed() {
		// Sign-in failed, so show sign-in button on main menu
		mMainMenuFragment.setGreeting(getString(R.string.signed_out_greeting));
		mMainMenuFragment.setShowSignInButton(true);
		mWinFragment.setShowSignInButton(true);
	}

	@Override
	public void onSignInSucceeded() {
		// Show sign-out button on main menu
		mMainMenuFragment.setShowSignInButton(false);
		// Show "you are signed in" message on win screen, with no sign in button.
		mWinFragment.setShowSignInButton(false);
		// Set the greeting appropriately on main menu
		Player p = getGamesClient().getCurrentPlayer();
		String displayName;
		if (p == null) {
			Log.w(TAG, "mGamesClient.getCurrentPlayer() is NULL!");
			displayName = "???";
		} else {
			displayName = p.getDisplayName();
		}
		mMainMenuFragment.setGreeting("Hello, " + displayName);
		// if we have accomplishments to push, push them
		if (!mOutbox.isEmpty()) {
			pushAccomplishments();
			Toast.makeText(this, getString(R.string.your_progress_will_be_uploaded), Toast.LENGTH_LONG).show();
		}
	}

	void pushAccomplishments() {
		if (!isSignedIn()) {
			// can't push to the cloud, so save locally
			mOutbox.saveLocal(this);
			return;
		}
		if (mOutbox.mPrimeAchievement) {
			getGamesClient().unlockAchievement(getString(R.string.achievement_prime));
			mOutbox.mPrimeAchievement = false;
		}
		if (mOutbox.mArrogantAchievement) {
			getGamesClient().unlockAchievement(getString(R.string.achievement_arrogant));
			mOutbox.mArrogantAchievement = false;
		}
		if (mOutbox.mHumbleAchievement) {
			getGamesClient().unlockAchievement(getString(R.string.achievement_humble));
			mOutbox.mHumbleAchievement = false;
		}
		if (mOutbox.mLeetAchievement) {
			getGamesClient().unlockAchievement(getString(R.string.achievement_leet));
			mOutbox.mLeetAchievement = false;
		}
		if (mOutbox.mBoredSteps > 0) {
			getGamesClient().incrementAchievement(getString(R.string.achievement_really_bored), mOutbox.mBoredSteps);
			getGamesClient().incrementAchievement(getString(R.string.achievement_bored), mOutbox.mBoredSteps);
		}
		if (mOutbox.mEasyModeScore >= 0) {
			getGamesClient().submitScore(getString(R.string.leaderboard_easy), mOutbox.mEasyModeScore);
			mOutbox.mEasyModeScore = -1;
		}
		if (mOutbox.mHardModeScore >= 0) {
			getGamesClient().submitScore(getString(R.string.leaderboard_hard), mOutbox.mHardModeScore);
			mOutbox.mHardModeScore = -1;
		}
		mOutbox.saveLocal(this);
	}

	@Override
	public void onWinScreenDismissed() {
		mViewPager.setCurrentItem(0);
	}

	@Override
	public void onWinScreenSignInClicked() {
		beginUserInitiatedSignIn();
	}

	@Override
	public void onEnteredScore(int requestedScore) {
		// Compute final score (in easy mode, it's the requested score; in hard mode, it's half)
		int finalScore = mHardMode ? requestedScore / 2 : requestedScore;
		mWinFragment.setFinalScore(finalScore);
		mWinFragment.setExplanation(mHardMode ? getString(R.string.hard_mode_explanation) : getString(R.string.easy_mode_explanation));
		// check for achievements
		checkForAchievements(requestedScore, finalScore);
		// update leaderboards
		updateLeaderboards(finalScore);
		// push those accomplishments to the cloud, if signed in
		pushAccomplishments();
		// switch to the exciting "you won" screen
		mViewPager.setCurrentItem(2);
	}

	@Override
	public void onStartGameRequested(boolean hardMode) {
		startGame(hardMode);
	}

	@Override
	public void onShowAchievementsRequested() {
		if (isSignedIn()) {
			startActivityForResult(getGamesClient().getAchievementsIntent(), RC_UNUSED);
		} else {
			showAlert(getString(R.string.achievements_not_available));
		}
	}

	@Override
	public void onShowLeaderboardsRequested() {
		if (isSignedIn()) {
			startActivityForResult(getGamesClient().getAllLeaderboardsIntent(), RC_UNUSED);
		} else {
			showAlert(getString(R.string.leaderboards_not_available));
		}
	}

	@Override
	public void onSignInButtonClicked() {
		// check if developer read the documentation!
		// (Note: in a production application, this code should NOT exist)
		if (!verifyPlaceholderIdsReplaced()) {
			showAlert("Sample not set up correctly. See README.");
			return;
		}
		// start the sign-in flow
		beginUserInitiatedSignIn();
	}

	@Override
	public void onSignOutButtonClicked() {
		signOut();
		mMainMenuFragment.setGreeting(getString(R.string.signed_out_greeting));
		mMainMenuFragment.setShowSignInButton(true);
		mWinFragment.setShowSignInButton(true);
	}

	/**
	 * Start gameplay. This means updating some status variables and switching to the "gameplay" screen (the screen where the user types the
	 * score they want).
	 * 
	 * @param hardMode
	 *            whether to start gameplay in "hard mode".
	 */
	void startGame(boolean hardMode) {
		mHardMode = hardMode;
		mViewPager.setCurrentItem(1);
	}

	/**
	 * Check for achievements and unlock the appropriate ones.
	 * 
	 * @param requestedScore
	 *            the score the user requested.
	 * @param finalScore
	 *            the score the user got.
	 */
	void checkForAchievements(int requestedScore, int finalScore) {
		// Check if each condition is met; if so, unlock the corresponding
		// achievement.
		if (isPrime(finalScore)) {
			mOutbox.mPrimeAchievement = true;
			achievementToast(getString(R.string.achievement_prime_toast_text));
		}
		if (requestedScore == 9999) {
			mOutbox.mArrogantAchievement = true;
			achievementToast(getString(R.string.achievement_arrogant_toast_text));
		}
		if (requestedScore == 0) {
			mOutbox.mHumbleAchievement = true;
			achievementToast(getString(R.string.achievement_humble_toast_text));
		}
		if (finalScore == 1337) {
			mOutbox.mLeetAchievement = true;
			achievementToast(getString(R.string.achievement_leet_toast_text));
		}
		mOutbox.mBoredSteps++;
	}

	/**
	 * Update leaderboards with the user's score.
	 * 
	 * @param finalScore
	 *            The score the user got.
	 */
	void updateLeaderboards(int finalScore) {
		if (mHardMode && mOutbox.mHardModeScore < finalScore) {
			mOutbox.mHardModeScore = finalScore;
		} else if (!mHardMode && mOutbox.mEasyModeScore < finalScore) {
			mOutbox.mEasyModeScore = finalScore;
		}
	}

	// Checks if n is prime. We don't consider 0 and 1 to be prime.
	// This is not an implementation we are mathematically proud of, but it gets the job done.
	boolean isPrime(int n) {
		int i;
		if (n == 0 || n == 1)
			return false;
		for (i = 2; i <= n / 2; i++) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}

	void achievementToast(String achievement) {
		// Only show toast if not signed in. If signed in, the standard Google Play
		// toasts will appear, so we don't need to show our own.
		if (!isSignedIn()) {
			Toast.makeText(this, getString(R.string.achievement) + ": " + achievement, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Checks that the developer (that's you!) read the instructions.
	 * 
	 * IMPORTANT: a method like this SHOULD NOT EXIST in your production app! It merely exists here to check that anyone running THIS PARTICULAR
	 * SAMPLE did what they were supposed to in order for the sample to work.
	 */
	boolean verifyPlaceholderIdsReplaced() {
		final boolean CHECK_PKGNAME = true; // set to false to disable check (not recommended!)
		// Did the developer forget to change the package name?
		if (CHECK_PKGNAME && (getPackageName().startsWith("com.google.example.")))
			return false;
		// Did the developer forget to replace a placeholder ID?
		int res_ids[] = new int[] { R.string.app_id, R.string.achievement_arrogant, R.string.achievement_bored, R.string.achievement_humble,
				R.string.achievement_leet, R.string.achievement_prime, R.string.leaderboard_easy, R.string.leaderboard_hard };
		for (int i : res_ids) {
			if (getString(i).equalsIgnoreCase("ReplaceMe"))
				return false;
		}
		return true;
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
				return mMainMenuFragment;
			case 1:
				return mGameplayFragment;
			case 2:
				return mWinFragment;
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

	class AccomplishmentsOutbox {
		boolean mPrimeAchievement = false;
		boolean mHumbleAchievement = false;
		boolean mLeetAchievement = false;
		boolean mArrogantAchievement = false;
		int mBoredSteps = 0;
		int mEasyModeScore = -1;
		int mHardModeScore = -1;

		boolean isEmpty() {
			return !mPrimeAchievement && !mHumbleAchievement && !mLeetAchievement && !mArrogantAchievement && mBoredSteps == 0
					&& mEasyModeScore < 0 && mHardModeScore < 0;
		}

		public void saveLocal(Context ctx) {
			/*
			 * : This is left as an exercise. To make it more difficult to cheat, this data should be stored in an encrypted file! And remember
			 * not to expose your encryption key (obfuscate it by building it from bits and pieces and/or XORing with another string, for
			 * instance).
			 */
		}

		public void loadLocal(Context ctx) {
			/*
			 * : This is left as an exercise. Write code here that loads data from the file you wrote in saveLocal().
			 */
		}
	}
}
