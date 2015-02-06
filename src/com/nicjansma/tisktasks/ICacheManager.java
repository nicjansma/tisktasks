package com.nicjansma.tisktasks;

import com.nicjansma.library.net.IJsonObjectCache;
import com.nicjansma.tisktasks.models.TodoistProjects;
import com.nicjansma.tisktasks.models.TodoistQueryResults;
import com.nicjansma.tisktasks.models.TodoistUser;

/**
 * Cache manager interface.
 */
public interface ICacheManager
    extends IJsonObjectCache
{
    /**
     * Clear all items in the cache.
     */
    void clearAll();

    /**
     * Gets all projects from the cache.
     *
     * @return Todoist projects, or null if not available.
     */
    TodoistProjects getProjectsCache();

    /**
     * Sets the projects cache.
     *
     * @param projects Projects cache.
     */
    void setProjectCache(TodoistProjects projects);

    /**
     * Gets the start page results.
     *
     * @return Start page results, or null if not available.
     */
    TodoistQueryResults getStartPageQueries();

    /**
     * Sets the start page query results.
     *
     * @param results Start page query results.
     */
    void setStartPageQueries(TodoistQueryResults results);

    /**
     * Gets the current user.
     *
     * @return Current user, or null if not available.
     */
    TodoistUser getCurrentUser();

    /**
     * Sets the current user.
     *
     * @param user Current user
     */
    void setCurrentUser(TodoistUser user);
}
