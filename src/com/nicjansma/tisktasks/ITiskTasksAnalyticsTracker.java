package com.nicjansma.tisktasks;

import com.nicjansma.library.analytics.IAnalyticsTracker;

/**
 * TiskTasks Analytics Tracker interface.
 */
public interface ITiskTasksAnalyticsTracker
    extends IAnalyticsTracker
{
    /**
     * Tracks Screen startup time.
     *
     * @param screenName Screen name
     * @param startTime Start time
     */
    void trackScreenStartup(String screenName, long startTime);
}
