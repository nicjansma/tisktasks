package com.nicjansma.tisktasks.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.nicjansma.tisktasks.R;
import com.nicjansma.tisktasks.ServiceLocator;

/**
 * Application preferences Activity.
 */
public final class AppPreferencesActivity
    extends PreferenceActivity
{
    /**
     * Android Intent.
     */
    public static final String INTENT = "com.nicjansma.tisktasks.action.PREFERENCES";

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        long startTime = System.currentTimeMillis();

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        ServiceLocator.tracker().trackScreenStartup("AppPreferences", startTime);
    }
}
