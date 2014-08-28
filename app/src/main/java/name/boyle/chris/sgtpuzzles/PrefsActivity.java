package name.boyle.chris.sgtpuzzles;

import name.boyle.chris.sgtpuzzles.compat.PrefsSaver;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

@SuppressWarnings("WeakerAccess")
public class PrefsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
	private PrefsSaver prefsSaver;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		prefsSaver = PrefsSaver.get(this);
		addPreferencesFromResource(R.xml.preferences);
		updateSummary((ListPreference)findPreference(SGTPuzzles.ARROW_KEYS_KEY));
		updateSummary((ListPreference)findPreference(SGTPuzzles.ORIENTATION_KEY));
		findPreference("about_content").setSummary(
				String.format(getString(R.string.about_content),
						SGTPuzzles.getVersion(this)));
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		prefsSaver.backup();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key)
	{
		Preference p = findPreference(key);
		if (p != null && p instanceof ListPreference) updateSummary((ListPreference)p);
	}

	void updateSummary(ListPreference lp)
	{
		lp.setSummary(lp.getEntry());
		getListView().postInvalidate();
	}
}
