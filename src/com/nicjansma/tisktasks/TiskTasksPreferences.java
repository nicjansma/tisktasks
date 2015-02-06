package com.nicjansma.tisktasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Application preferences.
 *
 * @author Nic Jansma
 */
public final class TiskTasksPreferences
    implements IPreferences
{
    //
    // constants
    //
    /**
     * Preferences for: Region.
     */
    public static final String PREFS_EMAIL = "email";

    /**
     * Preferences for: API Key.
     */
    public static final String PREFS_API_KEY = "apiKey";

    /**
     * Preferences for: Keep Logged In.
     */
    public static final String PREFS_KEEP_LOGGED_IN = "keepLoggedIn";

    /**
     * Preferences for: Keep Logged In.
     */
    public static final String PREFS_ANALYTICS = "analytics";

    //
    // members
    //
    /**
     * Shared preferences.
     */
    private final SharedPreferences _prefs;

    /**
     * TiskTasksPreferences constructor.
     *
     * @param context Android context
     */
    public TiskTasksPreferences(final Context context)
    {
        _prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public String email()
    {
        // load index from preferences
        return _prefs.getString(PREFS_EMAIL, "");
    }

    @Override
    public String apiToken()
    {
        // load index from preferences
        return _prefs.getString(PREFS_API_KEY, "");
    }

    @Override
    public boolean keepLoggedIn()
    {
        // load index from preferences
        return _prefs.getBoolean(PREFS_KEEP_LOGGED_IN, true);
    }

    @Override
    public void setEmail(final String email)
    {
        SharedPreferences.Editor editor = _prefs.edit();
        editor.putString(PREFS_EMAIL, email);
        editor.commit();
    }

    @Override
    public void setApiToken(final String apiKey)
    {
        SharedPreferences.Editor editor = _prefs.edit();
        editor.putString(PREFS_API_KEY, apiKey);
        editor.commit();
    }

    @Override
    public void setKeepLoggedIn(final boolean keepLoggedIn)
    {
        SharedPreferences.Editor editor = _prefs.edit();
        editor.putBoolean(PREFS_KEEP_LOGGED_IN, keepLoggedIn);
        editor.commit();
    }

    @Override
    public boolean analyticsEnabled()
    {
        return _prefs.getBoolean(PREFS_ANALYTICS, true);
    }
}
