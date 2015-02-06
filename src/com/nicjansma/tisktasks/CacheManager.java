package com.nicjansma.tisktasks;

import android.content.Context;

import com.nicjansma.library.android.SharedPreferencesJsonObjectCache;
import com.nicjansma.tisktasks.models.TodoistProjects;
import com.nicjansma.tisktasks.models.TodoistQueryResults;
import com.nicjansma.tisktasks.models.TodoistUser;

/**
 * Todoist Cache Manager.
 *
 * Uses a Shared Preferences Json Object Cache as base.
 */
public final class CacheManager
    extends SharedPreferencesJsonObjectCache
    implements ICacheManager
{
    //
    // Constants
    //
    /**
     * Start page tasks age: 2 hours.
     */
    private static final int CACHE_START_PAGE_TASKS_AGE =  7200000;

    /**
     * Start page tasks key.
     */
    private static final String CACHE_START_PAGE_TASKS_KEY = "startPageTasks";

    /**
     * Projects age: 1 day.
     */
    private static final int CACHE_PROJECTS_AGE =  86400000;

    /**
     * Projects key.
     */
    private static final String CACHE_PROJECTS_KEY = "projects";

    /**
     * Current user key.
     */
    private static final String CACHE_CURRENT_USER_KEY = "currentUser";

    /**
     * CacheManager constructor.
     *
     * @param context Application context
     */
    public CacheManager(final Context context)
    {
        super(context);
    }

    @Override
    public void clearAll()
    {
        remove(CACHE_START_PAGE_TASKS_KEY);
        remove(CACHE_PROJECTS_KEY);
        remove(CACHE_CURRENT_USER_KEY);
    }

    @Override
    public TodoistProjects getProjectsCache()
    {
        return getJson(CACHE_PROJECTS_KEY, TodoistProjects.class, CACHE_PROJECTS_AGE);
    }

    @Override
    public void setProjectCache(final TodoistProjects projects)
    {
        set(CACHE_PROJECTS_KEY, projects);
    }

    @Override
    public TodoistQueryResults getStartPageQueries()
    {
        return getJson(CACHE_START_PAGE_TASKS_KEY, TodoistQueryResults.class, CACHE_START_PAGE_TASKS_AGE);
    }

    @Override
    public void setStartPageQueries(final TodoistQueryResults results)
    {
        set(CACHE_START_PAGE_TASKS_KEY, results);
    }

    @Override
    public TodoistUser getCurrentUser()
    {
        return getJson(CACHE_CURRENT_USER_KEY, TodoistUser.class);
    }

    @Override
    public void setCurrentUser(final TodoistUser user)
    {
        set(CACHE_CURRENT_USER_KEY, user);
    }
}
