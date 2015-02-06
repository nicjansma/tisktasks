package com.nicjansma.tisktasks;

import android.app.Application;

import com.nicjansma.library.android.AndroidUtils;

/**
 * TiskTasksApplication application.
 *
 * @author Nic Jansma
 */
public class TiskTasksApplication extends Application
{
    @Override
    public final void onCreate()
    {
        long startTime = System.currentTimeMillis();

        //
        // Initialize the Service Locator
        //
        ServiceLocator.initialize(this.getApplicationContext());

        //
        // If Keep Logged In is false, clear the API Token and require them to log in again
        //
        if (ServiceLocator.userManager().isLoggedIn()
            && !ServiceLocator.prefs().keepLoggedIn())
        {
            ServiceLocator.userManager().logout();
        }

        // disable Analytics if running in the emulator
        if (!AndroidUtils.isRunningInEmulator() && ServiceLocator.prefs().analyticsEnabled())
        {
            ServiceLocator.tracker().init(this.getApplicationContext(), R.xml.google_analytics_tracker);
        }

        ServiceLocator.tracker().trackUserTiming("App", System.currentTimeMillis() - startTime, "onCreate", "");
    }
}
