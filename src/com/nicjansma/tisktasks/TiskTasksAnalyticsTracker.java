package com.nicjansma.tisktasks;

import android.content.Context;

import com.nicjansma.library.analytics.GoogleAnalyticsTrackerBase;

/**
 * Minifig Collector Analytics tracker.
 */
public class TiskTasksAnalyticsTracker
    extends GoogleAnalyticsTrackerBase
    implements ITiskTasksAnalyticsTracker
{
    @Override
    public final void initInternal(final Context context)
    {
    }

    @Override
    public final void trackScreenStartup(final String screenName, final long startTime)
    {
        trackScreenView(screenName);
        trackUserTiming("Activity", System.currentTimeMillis() - startTime, "onCreate", screenName);
    }
}
