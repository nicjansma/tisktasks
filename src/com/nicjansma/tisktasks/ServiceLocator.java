package com.nicjansma.tisktasks;

import android.content.Context;

import com.nicjansma.tisktasks.api.ITodoistApi;
import com.nicjansma.tisktasks.api.TodoistApi;
import com.nicjansma.tisktasks.models.IProjectManager;
import com.nicjansma.tisktasks.models.ProjectManager;

/**
 * Service locator.
 *
 * Sets and find static services.
 */
public abstract class ServiceLocator
{
    //
    // members
    //
    /**
     * Application context.
     */
    private static Context _context = null;

    /**
     * API.
     */
    private static ITodoistApi _api = null;

    /**
     * User manager.
     */
    private static IUserManager _userManager = null;

    /**
     * Project manager.
     */
    private static IProjectManager _projectManager = null;

    /**
     * Preferences.
     */
    private static IPreferences _prefs = null;

    /**
     * Object cache.
     */
    private static ICacheManager _cache = null;

    /**
     * Analytics Tracker.
     */
    private static ITiskTasksAnalyticsTracker _tracker = null;

    /**
     * Initialize the ServiceLocator.
     *
     * @param newContext Application context
     */
    public static void initialize(final Context newContext)
    {
        _context = newContext;
    }

    /**
     * Gets the Todoist API.
     *
     * @return Todoist API.
     */
    public static ITodoistApi todoistApi()
    {
        if (_api == null)
        {
            _api = new TodoistApi();
        }

        return _api;
    }

    /**
     * Sets the ITodoistAPI object.
     *
     * @param newApi Todoist API
     */
    public static void setTodoistApi(final ITodoistApi newApi)
    {
        _api = newApi;
    }

    /**
     * @return Gets the current preferences
     */
    public static IPreferences prefs()
    {
        if (_prefs == null)
        {
            // defaults are the app saved preferences
            setPrefs(new TiskTasksPreferences(_context));
        }

        return _prefs;
    }

    /**
     * Sets the IPreferences object.
     *
     * @param newPrefs Preferences
     */
    public static void setPrefs(final IPreferences newPrefs)
    {
        _prefs = newPrefs;
    }

    /**
     * Clear the preferences.
     */
    public static void clearPrefs()
    {
        _prefs = null;
    }

    /**
     * @return Gets the current User Manager
     */
    public static IUserManager userManager()
    {
        if (_userManager == null)
        {
            setUserManager(new UserManager());
        }

        return _userManager;
    }

    /**
     * Sets the User Manager object.
     *
     * @param newUser User Manager
     */
    public static void setUserManager(final IUserManager newUser)
    {
        _userManager = newUser;
    }

    /**
     * Clear the User Manager.
     */
    public static void clearUser()
    {
        _userManager = null;
    }

    /**
     * @return Gets the current Project Manager
     */
    public static IProjectManager projectManager()
    {
        if (_projectManager == null)
        {
            setProjectManager(new ProjectManager());
        }

        return _projectManager;
    }

    /**
     * Sets the Project Manager.
     *
     * @param newProjectManager Project Manager
     */
    public static void setProjectManager(final IProjectManager newProjectManager)
    {
        _projectManager = newProjectManager;
    }

    /**
     * Clear the Project Manager.
     */
    public static void clearProjectsManager()
    {
        _projectManager = null;
    }

    /**
     * @return Gets the current Object Cache
     */
    public static ICacheManager cache()
    {
        if (_cache == null)
        {
            setObjectCache(new CacheManager(_context));
        }

        return _cache;
    }

    /**
     * Sets the Object Cache.
     *
     * @param newObjectCache Object Cache
     */
    public static void setObjectCache(final ICacheManager newObjectCache)
    {
        _cache = newObjectCache;
    }

    /**
     * Clear the Object Cache.
     */
    public static void clearObjectCache()
    {
        _cache = null;
    }

    /**
     * @return Gets the current analytics tracker
     */
    public static ITiskTasksAnalyticsTracker tracker()
    {
        if (_tracker == null)
        {
            // defaults are the Google analytics tracker
            setTracker(new TiskTasksAnalyticsTracker());
        }

        return _tracker;
    }

    /**
     * Sets the ITiskTasksAnalyticsTracker object.
     *
     * @param newTracker Analytics tracker
     */
    public static void setTracker(final ITiskTasksAnalyticsTracker newTracker)
    {
        _tracker = newTracker;
    }
}
